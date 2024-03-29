import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.PI
import kotlin.math.abs

class ComplexTest {
    private val delta = 0.0001

    @Test
    fun initialization() {
        checkInitValues(Complex.fromCartesian(), 0.0, 0.0)
        checkInitValues(Complex.fromCartesian(2.0, 3.4), 2.0, 3.4)
        checkInitValues(Complex.fromPolarRadian(5.4, 6.02), 5.214057, -1.404850)
        checkInitValues(Complex.fromPolarDegree(4.3, 82.6), 0.553821, 4.264185)
        checkInitValues(Complex.fromPolarDegree(0.0, 82.6), 0.0, 0.0)

        val complex1 = Complex.fromCartesian(5.0, 4.3)
        var complex2 = Complex(complex1)
        checkInitValues(complex1, 5.0, 4.3)
        checkInitValues(complex2, 5.0, 4.3)
        complex2 += 1
        checkInitValues(complex1, 5.0, 4.3)
        checkInitValues(complex2, 6.0, 4.3)
    }

    private fun checkInitValues(complex: Complex, expectedReal: Double, expectedImaginary: Double) {
        assertEquals(complex.real, expectedReal, delta)
        assertEquals(complex.imaginary, expectedImaginary, delta)
    }

    @Test
    fun properties() {
        val complex = Complex.fromCartesian(2.0, 3.4)
        assertEquals(complex.r, 3.944616, delta)
        assertEquals(complex.abs, 3.944616, delta)
        assertEquals(complex.phiInRadian, 1.039072, delta)
        assertEquals(complex.phiInDegree, 59.534455, delta)
        checkInitValues(complex.conjugate, complex.real, -complex.imaginary)
        assertEquals(complex.derivative, 1.7, delta)
        assertEquals(complex.integral, 3.4, delta)
    }

    @Test(expected = IllegalArgumentException::class)
    fun exceptionThrown() {
        Complex.fromPolarDegree(-2.0, 3.4)
    }

    @Test(expected = IllegalArgumentException::class)
    fun exceptionThrown2() {
        Complex.fromPolarRadian(-5.4, 9.8)
    }

    @Test
    fun equality() {
        val complex1 = Complex.fromCartesian(2.0, 3.4)
        val complex2 = Complex.fromCartesian(2.0, 3.4)
        val complex3 = Complex.fromCartesian(2.1, 3.3)
        val complex4 = Complex.fromCartesian(20 * 0.1, 34 * 0.1)
        assert(complex1.equals(complex2))
        assert(complex1 == complex2)
        assert(complex1 != complex3)
        assert(complex1 == complex4)
    }

    @Test
    fun hashcode() {
        val complex1 = Complex.fromCartesian(2.0, 3.4)
        val complex2 = Complex.fromCartesian(2.0, 3.4)
        val complex3 = Complex.fromCartesian(2.1, 3.3)
        val complex4 = Complex.fromCartesian(20 * 0.1, 34 * 0.1)
        assert(complex1.hashCode() == complex2.hashCode())
        assert(complex1.hashCode() != complex3.hashCode())

        // It is a bit tricky,
        // equality works with complex1 and complex4, but
        // in reality the imaginary part of the two Complex numbers are not the same,
        // so the hashCode will not be the same!
        assert(complex1.hashCode() != complex4.hashCode())
    }

    @Test
    fun to_string_printing() {
        val complex = Complex.fromCartesian(2.0, 3.4)
        assert(complex.toString().contains(complex.real.toString()))
        assert(complex.toString().contains(complex.imaginary.toString()))

        assert(complex.printInCartesian().contains(complex.real.toString()))
        assert(complex.printInCartesian().contains(complex.imaginary.toString()))

        assert(complex.printInPolarDegree().contains(complex.r.toString()))
        assert(complex.printInPolarDegree().contains(complex.phiInDegree.toString()))

        assert(complex.printInPolarRadian().contains(complex.phiInRadian.toString()))
        assert(complex.printInPolarRadian().contains(complex.r.toString()))
    }

    @Test
    fun compare_to() {
        val complex1 = Complex.fromCartesian(2.0, 3.4)
        val complex2 = Complex.fromCartesian(2.0, 3.4)
        val complex3 = Complex.fromCartesian(1.5, 30.3)

        assert(abs(complex1.compareTo(complex2)) < delta)
        assert(complex1.compareTo(complex3) < 0)
        assert(complex1 < complex3)

        Complex.sortingState = Complex.SortingState.Length
        assert(abs(complex1.compareTo(complex2)) < delta)
        assert(complex1.compareTo(complex3) < 0)
        assert(complex1 < complex3)

        Complex.sortingState = Complex.SortingState.Real
        assert(abs(complex1.compareTo(complex2)) < delta)
        assert(complex1.compareTo(complex3) > 0)
        assert(complex1 > complex3)

        Complex.sortingState = Complex.SortingState.Imaginary
        assert(abs(complex1.compareTo(complex2)) < delta)
        assert(complex1.compareTo(complex3) < 0)
        assert(complex1 < complex3)

        Complex.sortingState = Complex.SortingState.Phi
        assert(abs(complex1.compareTo(complex2)) < delta)
        assert(complex1.compareTo(complex3) < 0)
        assert(complex1 < complex3)
    }

    @Test
    fun copy() {
        val complex1 = Complex.fromCartesian(2.0, 3.4)
        val complex2 = Complex.fromCartesian(2.1, 3.4)
        assert(complex1 == complex1.copy())
        assert(complex1 != complex2.copy())

        assert(complex1.copy() == Complex.fromPolarDegree(3.944616, 59.534455))
    }

    @Test
    fun unaryOperators() {
        val complex = Complex.fromCartesian(2.0, 3.4)
        assert(complex.unaryMinus() == Complex.fromCartesian(-2.0, -3.4))
        assert(complex.unaryPlus() == complex)

        assert(complex.inc() == Complex.fromCartesian(3.0, 3.4))
        // Immutability.
        assert(complex.inc() == Complex.fromCartesian(3.0, 3.4))
        assert(complex.dec() == Complex.fromCartesian(1.0, 3.4))

        // Immutability.
        assert(complex == Complex.fromCartesian(2.0, 3.4))

        assert(complex.component1() == complex.real)
        assert(complex.component2() == complex.imaginary)
    }

    @Test
    fun plusOperators() {
        val complex1 = Complex.fromCartesian(2.0, 3.4)
        val complex2 = Complex.fromCartesian(19.5, -7.8)

        assert(complex1 + complex2 == Complex.fromCartesian(21.5, -4.4))
        assert(complex1 + 5.4 == Complex.fromCartesian(7.4, 3.4))
        assert(complex1 + 5.3f == Complex.fromCartesian(7.3, 3.4))
        assert(complex1 + 5 == Complex.fromCartesian(7.0, 3.4))
        assert(complex1 + 6L == Complex.fromCartesian(8.0, 3.4))
        assert(complex1 + 7L == Complex.fromCartesian(9.0, 3.4))
        assert(complex1 + 8.toByte() == Complex.fromCartesian(10.0, 3.4))
        assert(complex1 + 9.toShort() == Complex.fromCartesian(11.0, 3.4))
    }

    @Test
    fun minusOperators() {
        val complex1 = Complex.fromCartesian(2.0, 3.4)
        val complex2 = Complex.fromCartesian(19.5, -7.8)

        assert(complex1 - complex2 == Complex.fromCartesian(-17.5, 11.2))
        assert(complex1 - 5.4 == Complex.fromCartesian(-3.4, 3.4))
        assert(complex1 - 5.3f == Complex.fromCartesian(-3.3, 3.4))
        assert(complex1 - 5 == Complex.fromCartesian(-3.0, 3.4))
        assert(complex1 - 6L == Complex.fromCartesian(-4.0, 3.4))
        assert(complex1 - 7L == Complex.fromCartesian(-5.0, 3.4))
        assert(complex1 - 8.toByte() == Complex.fromCartesian(-6.0, 3.4))
        assert(complex1 - 9.toShort() == Complex.fromCartesian(-7.0, 3.4))
    }

    @Test
    fun timesOperators() {
        val complex1 = Complex.fromCartesian(2.0, 3.4)
        val complex2 = Complex.fromCartesian(19.5, -7.8)

        assert(complex1 * complex2 == Complex.fromCartesian(65.52, 50.7))
        assert(complex1 * 5.4 == Complex.fromCartesian(10.8, 18.36))
        assert(complex1 * 5.3f == Complex.fromCartesian(10.6, 18.02))
        assert(complex1 * 5 == Complex.fromCartesian(10.0, 17.0))
        assert(complex1 * 6L == Complex.fromCartesian(12.0, 20.4))
        assert(complex1 * 7L == Complex.fromCartesian(14.0, 23.8))
        assert(complex1 * 8.toByte() == Complex.fromCartesian(16.0, 27.2))
        assert(complex1 * 9.toShort() == Complex.fromCartesian(18.0, 30.6))
    }

    @Test
    fun divOperators() {
        val complex1 = Complex.fromCartesian(2.0, 3.4)
        val complex2 = Complex.fromCartesian(19.5, -7.8)

        assert(complex1 / complex2 == Complex.fromCartesian(0.028293, 0.185676))
        assert(complex1 / 5.4 == Complex.fromCartesian(0.370370, 0.629629))
        assert(complex1 / 5.3f == Complex.fromCartesian(0.377358, 0.641509))
        assert(complex1 / 5 == Complex.fromCartesian(0.4, 0.68))
        assert(complex1 / 6L == Complex.fromCartesian(0.333333, 0.566666))
        assert(complex1 / 7L == Complex.fromCartesian(0.285714, 0.485714))
        assert(complex1 / 8.toByte() == Complex.fromCartesian(0.25, 0.425))
        assert(complex1 / 9.toShort() == Complex.fromCartesian(0.222222, 0.377777))
    }

    @Test
    fun pow() {
        val complex = Complex.fromCartesian(2.0, 3.4)
        assert(complex.power(2) == Complex.fromCartesian(-7.56, 13.6))
        assert(complex.power(3.toByte()) == Complex.fromCartesian(-61.36, 1.496))
        assert(complex.power(2.toShort()) == Complex.fromCartesian(-7.56, 13.6))
        assert(complex.power(3.toLong()) == Complex.fromCartesian(-61.36, 1.496))
    }

    @Test
    fun root() {
        val complex = Complex.fromCartesian(23.0, 79.0)
        val arrayOfRoots = complex.sqrt(2)

        assert(Complex.fromCartesian(7.255343, 5.444263) == arrayOfRoots[0])
        assert(Complex.fromCartesian(-7.255342, -5.444263) == arrayOfRoots[1])
    }

    @Test
    fun rotation() {
        val complex = Complex.fromCartesian(1.0, 1.0)

        assert(complex.rotateByDegree(90.0, Complex.Direction.Positive) == Complex.fromCartesian(-1.0, 1.0))
        assert(complex.rotateByRadian(PI, Complex.Direction.Negative) == Complex.fromCartesian(-1.0, -1.0))
    }

    @Test
    fun double() {
        val complex = 5.6.j

        assert((5.4 + complex) == Complex.fromCartesian(5.4, 5.6))
        assert((5.4 - complex) == Complex.fromCartesian(5.4, -5.6))
        assert((5.4 * complex) == Complex.fromCartesian(0.0, 30.24))
        assert((5.4 / complex) == Complex.fromCartesian(0.0, -0.964285))
    }

    @Test
    fun float() {
        val complex = 6.9f.j

        assert((5.5f + complex) == Complex.fromCartesian(5.5, 6.9))
        assert((5.5f - complex) == Complex.fromCartesian(5.5, -6.9))
        assert((5.5f * complex) == Complex.fromCartesian(0.0, 37.95))
        assert((5.5f / complex) == Complex.fromCartesian(0.0, -0.797101))
    }

    @Test
    fun int() {
        val complex = (-3).j

        assert((3 + complex) == Complex.fromCartesian(3.0, -3.0))
        assert((3 - complex) == Complex.fromCartesian(3.0, 3.0))
        assert((3 * complex) == Complex.fromCartesian(0.0, -9.0))
        assert((3 / complex) == Complex.fromCartesian(0.0, 1.0))
    }

    @Test
    fun long() {
        val complex = 1L.j

        assert((9L + complex) == Complex.fromCartesian(9.0, 1.0))
        assert((9L - complex) == Complex.fromCartesian(9.0, -1.0))
        assert((9L * complex) == Complex.fromCartesian(0.0, 9.0))
        assert((9L / complex) == Complex.fromCartesian(0.0, -9.0))
    }

    @Test
    fun byte() {
        val complex = 2.toByte().j

        assert((4.toByte() + complex) == Complex.fromCartesian(4.0, 2.0))
        assert((4.toByte() - complex) == Complex.fromCartesian(4.0, -2.0))
        assert((4.toByte() * complex) == Complex.fromCartesian(0.0, 8.0))
        assert((4.toByte() / complex) == Complex.fromCartesian(0.0, -2.0))
    }

    @Test
    fun short() {
        val complex = 8.toShort().j

        assert((3.toShort() + complex) == Complex.fromCartesian(3.0, 8.0))
        assert((3.toShort() - complex) == Complex.fromCartesian(3.0, -8.0))
        assert((3.toShort() * complex) == Complex.fromCartesian(0.0, 24.0))
        assert((3.toShort() / complex) == Complex.fromCartesian(0.0, -0.375))
    }
}
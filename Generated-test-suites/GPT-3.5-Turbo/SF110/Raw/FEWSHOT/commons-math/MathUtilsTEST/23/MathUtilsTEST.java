import org.junit.Test;

import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_Double() {
        assertEquals(-862509225, MathUtils.hash(-3.141592653589793));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(20703013, MathUtils.hash(3.141592653589793));
    }

    @Test
    public void testHash_DoubleArray() {
        double[] arr1 = {-1.0, 0.0, 1.0};
        double[] arr2 = {-1.0, 0.0, 1.0};
        double[] arr3 = {1.0, 0.0, -1.0};

        assertEquals(115276, MathUtils.hash(arr1));
        assertEquals(115276, MathUtils.hash(arr2));
        assertEquals(-4787096, MathUtils.hash(arr3));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(-0.15915494309189143, MathUtils.normalizeAngle(6.123233995736766E-17, 0.0), 0.0);
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0);
        assertEquals(0.0, MathUtils.normalizeAngle(12.566370614359172, 0.0), 0.0);
    }

    @Test
    public void testReduce() {
        assertEquals(-11.0, MathUtils.reduce(9.0, 10.0, 0.0), 0.0);
        assertEquals(-9.0, MathUtils.reduce(11.0, 10.0, 0.0), 0.0);
        assertEquals(1.0, MathUtils.reduce(-9.0, 10.0, 0.0), 0.0);
    }

    @Test
    public void testCopySign() {
        try {
            assertEquals((byte)-1, MathUtils.copySign((byte)1, (byte)-1));
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }

        assertEquals((byte)1, MathUtils.copySign((byte)1, (byte)1));
        assertEquals((byte)1, MathUtils.copySign((byte)-1, (byte)1));

        try {
            assertEquals((short)-1, MathUtils.copySign((short)1, (short)-1));
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }

        assertEquals((short)1, MathUtils.copySign((short)1, (short)1));
        assertEquals((short)1, MathUtils.copySign((short)-1, (short)1));

        try {
            assertEquals(-1, MathUtils.copySign(1, -1));
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }

        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(1, MathUtils.copySign(-1, 1));

        try {
            assertEquals(-1L, MathUtils.copySign(1L, -1L));
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }

        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(1L, MathUtils.copySign(-1L, 1L));
    }

    @Test
    public void testCheckFinite_Double() {
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        try {
            MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        try {
            MathUtils.checkFinite(Double.NaN);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(-1.0);
    }

    @Test
    public void testCheckFinite_DoubleArray() {
        try {
            MathUtils.checkFinite(new double[]{Double.POSITIVE_INFINITY});
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        try {
            MathUtils.checkFinite(new double[]{Double.NEGATIVE_INFINITY});
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        try {
            MathUtils.checkFinite(new double[]{Double.NaN});
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        try {
            MathUtils.checkFinite(new double[]{1.0, Double.POSITIVE_INFINITY});
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        MathUtils.checkFinite(new double[]{0.0, 1.0, -1.0});
        MathUtils.checkFinite(new double[]{-1.0, 0.0, 1.0});
    }

    @Test
    public void testCheckNotNull_Object_Localizable() {
        try {
            MathUtils.checkNotNull(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }

        try {
            MathUtils.checkNotNull(null, null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }

        MathUtils.checkNotNull(1.0, null);
        MathUtils.checkNotNull(1.0, null, null);
    }

    @Test
    public void testCheckNotNull_Object() {
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }

        MathUtils.checkNotNull(1.0);
    }
}
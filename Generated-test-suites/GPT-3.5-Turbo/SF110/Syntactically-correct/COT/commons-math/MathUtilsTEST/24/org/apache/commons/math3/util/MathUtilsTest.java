import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash() {
        // Test case 1: positive value
        double value1 = 3.5;
        int expected1 = Double.valueOf(value1).hashCode();
        assertEquals(expected1, MathUtils.hash(value1));

        // Test case 2: negative value
        double value2 = -2.5;
        int expected2 = Double.valueOf(value2).hashCode();
        assertEquals(expected2, MathUtils.hash(value2));
    }

    @Test
    public void testNormalizeAngle() {
        // Test case 1: a smaller than center
        double a1 = 2.0;
        double center1 = 5.0;
        double expected1 = a1 - 6.283185307179586 * Math.floor((a1 + 3.141592653589793 - center1) / 6.283185307179586);
        assertEquals(expected1, MathUtils.normalizeAngle(a1, center1), 0.00001);

        // Test case 2: a greater than center
        double a2 = 6.0;
        double center2 = 4.0;
        double expected2 = a2 - 6.283185307179586 * Math.floor((a2 + 3.141592653589793 - center2) / 6.283185307179586);
        assertEquals(expected2, MathUtils.normalizeAngle(a2, center2), 0.00001);
    }

    @Test
    public void testReduce() {
        // Test case 1: a smaller than offset
        double a1 = 2.5;
        double period1 = 5.0;
        double offset1 = 3.0;
        double p1 = Math.abs(period1);
        double expected1 = a1 - p1 * Math.floor((a1 - offset1) / p1) - offset1;
        assertEquals(expected1, MathUtils.reduce(a1, period1, offset1), 0.00001);

        // Test case 2: a greater than offset
        double a2 = 7.5;
        double period2 = 5.0;
        double offset2 = 3.0;
        double p2 = Math.abs(period2);
        double expected2 = a2 - p2 * Math.floor((a2 - offset2) / p2) - offset2;
        assertEquals(expected2, MathUtils.reduce(a2, period2, offset2), 0.00001);
    }

    @Test
    public void testCopySignByte() {
        // Test case 1: magnitude is positive and sign is negative
        byte magnitude1 = 3;
        byte sign1 = -1;
        assertEquals(magnitude1, MathUtils.copySign(magnitude1, sign1));

        // Test case 2: magnitude is negative and sign is positive
        byte magnitude2 = -3;
        byte sign2 = 1;
        assertEquals(-magnitude2, MathUtils.copySign(magnitude2, sign2));

        // Test case 3: magnitude is negative and sign is negative
        byte magnitude3 = -4;
        byte sign3 = -1;
        assertEquals(magnitude3, MathUtils.copySign(magnitude3, sign3));

        // Test case 4: magnitude is Byte.MIN_VALUE and sign is positive
        byte magnitude4 = Byte.MIN_VALUE;
        byte sign4 = 1;
        try {
            MathUtils.copySign(magnitude4, sign4);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testCopySignShort() {
        // Test case 1: magnitude is positive and sign is negative
        short magnitude1 = 300;
        short sign1 = -1;
        assertEquals(magnitude1, MathUtils.copySign(magnitude1, sign1));

        // Test case 2: magnitude is negative and sign is positive
        short magnitude2 = -300;
        short sign2 = 1;
        assertEquals(-magnitude2, MathUtils.copySign(magnitude2, sign2));

        // Test case 3: magnitude is negative and sign is negative
        short magnitude3 = -400;
        short sign3 = -1;
        assertEquals(magnitude3, MathUtils.copySign(magnitude3, sign3));

        // Test case 4: magnitude is Short.MIN_VALUE and sign is positive
        short magnitude4 = Short.MIN_VALUE;
        short sign4 = 1;
        try {
            MathUtils.copySign(magnitude4, sign4);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testCopySignInt() {
        // Test case 1: magnitude is positive and sign is negative
        int magnitude1 = 30000;
        int sign1 = -1;
        assertEquals(magnitude1, MathUtils.copySign(magnitude1, sign1));

        // Test case 2: magnitude is negative and sign is positive
        int magnitude2 = -30000;
        int sign2 = 1;
        assertEquals(-magnitude2, MathUtils.copySign(magnitude2, sign2));

        // Test case 3: magnitude is negative and sign is negative
        int magnitude3 = -40000;
        int sign3 = -1;
        assertEquals(magnitude3, MathUtils.copySign(magnitude3, sign3));

        // Test case 4: magnitude is Integer.MIN_VALUE and sign is positive
        int magnitude4 = Integer.MIN_VALUE;
        int sign4 = 1;
        try {
            MathUtils.copySign(magnitude4, sign4);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testCopySignLong() {
        // Test case 1: magnitude is positive and sign is negative
        long magnitude1 = 3000000000L;
        long sign1 = -1L;
        assertEquals(magnitude1, MathUtils.copySign(magnitude1, sign1));

        // Test case 2: magnitude is negative and sign is positive
        long magnitude2 = -3000000000L;
        long sign2 = 1L;
        assertEquals(-magnitude2, MathUtils.copySign(magnitude2, sign2));

        // Test case 3: magnitude is negative and sign is negative
        long magnitude3 = -4000000000L;
        long sign3 = -1L;
        assertEquals(magnitude3, MathUtils.copySign(magnitude3, sign3));

        // Test case 4: magnitude is Long.MIN_VALUE and sign is positive
        long magnitude4 = Long.MIN_VALUE;
        long sign4 = 1L;
        try {
            MathUtils.copySign(magnitude4, sign4);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        // Test case 1: finite number
        double x1 = 3.5;
        MathUtils.checkFinite(x1);

        // Test case 2: infinite number
        double x2 = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x2);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        // Test case 3: NaN
        double x3 = Double.NaN;
        try {
            MathUtils.checkFinite(x3);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        // Test case 1: all elements are finite numbers
        double[] val1 = {1.2, 3.4, 5.6};
        MathUtils.checkFinite(val1);

        // Test case 2: one element is infinite
        double[] val2 = {1.2, Double.POSITIVE_INFINITY, 5.6};
        try {
            MathUtils.checkFinite(val2);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        // Test case 3: one element is NaN
        double[] val3 = {1.2, Double.NaN, 5.6};
        try {
            MathUtils.checkFinite(val3);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckNotNullWithPattern() {
        // Test case 1: non-null object
        Object o1 = new Object();
        Localizable pattern1 = null;
        Object[] args1 = null;
        MathUtils.checkNotNull(o1, pattern1, args1);

        // Test case 2: null object
        Object o2 = null;
        Localizable pattern2 = null;
        Object[] args2 = null;
        try {
            MathUtils.checkNotNull(o2, pattern2, args2);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckNotNullWithoutPattern() {
        // Test case 1: non-null object
        Object o1 = new Object();
        MathUtils.checkNotNull(o1);

        // Test case 2: null object
        Object o2 = null;
        try {
            MathUtils.checkNotNull(o2);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

}
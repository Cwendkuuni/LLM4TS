import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class MathUtilsTest {

    private static final double DELTA = 1e-15;

    @Test
    public void testHash_Double() {
        double value = 3.14;
        int expected = new Double(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testHash_DoubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.0;
        double center = 0.0;
        double expected = 10.0;
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, DELTA);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 2.0;
        double offset = 0.0;
        double expected = 0.0;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, DELTA);
    }

    @Test
    public void testCopySign_Byte() {
        byte magnitude = 10;
        byte sign = -1;
        byte expected = -10;
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCopySign_Byte_Overflow() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // Pass, expected exception
        }
    }

    @Test
    public void testCopySign_Short() {
        short magnitude = 100;
        short sign = -1;
        short expected = -100;
        try {
            short result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCopySign_Short_Overflow() {
        short magnitude = -32768;
        short sign = 1;
        try {
            short result = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // Pass, expected exception
        }
    }

    @Test
    public void testCopySign_Integer() {
        int magnitude = 1000;
        int sign = -1;
        int expected = -1000;
        try {
            int result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCopySign_Integer_Overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            int result = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // Pass, expected exception
        }
    }

    @Test
    public void testCopySign_Long() {
        long magnitude = 1000000L;
        long sign = -1L;
        long expected = -1000000L;
        try {
            long result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCopySign_Long_Overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        try {
            long result = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // Pass, expected exception
        }
    }

    @Test
    public void testCheckFinite() {
        double x = 10.0;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCheckFinite_Infinite() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Pass, expected exception
        }
    }

    @Test
    public void testCheckFinite_NaN() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Pass, expected exception
        }
    }

    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.0, 2.0, 3.0};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCheckFiniteArray_Infinite() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Pass, expected exception
        }
    }

    @Test
    public void testCheckFiniteArray_NaN() {
        double[] val = {1.0, Double.NaN, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Pass, expected exception
        }
    }

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCheckNotNull_Null() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Pass, expected exception
        }
    }

    @Test
    public void testCheckNotNull_Localizable() {
        Object o = new Object();
        Localizable pattern = new LocalizedFormats() {
            public String getLocalizedString(Object... args) {
                return "Pattern";
            }
        };
        try {
            MathUtils.checkNotNull(o, pattern);
        } catch (NullArgumentException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCheckNotNull_Null_Localizable() {
        Object o = null;
        Localizable pattern = new LocalizedFormats() {
            public String getLocalizedString(Object... args) {
                return "Pattern";
            }
        };
        try {
            MathUtils.checkNotNull(o, pattern);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Pass, expected exception
        }
    }
}
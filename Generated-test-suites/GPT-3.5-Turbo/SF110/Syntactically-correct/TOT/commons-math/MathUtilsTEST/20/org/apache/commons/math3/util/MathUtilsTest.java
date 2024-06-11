import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 3.141592653589793;
        int expectedHash = -1468914501;
        int result = MathUtils.hash(value);
        assertEquals(expectedHash, result);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, -2.0, 3.0, -4.0};
        int expectedHash = -1260794353;
        int result = MathUtils.hash(value);
        assertEquals(expectedHash, result);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 5.8;
        double center = 1.0;
        double expectedNormalizedAngle = 4.814159265358979;
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedNormalizedAngle, result, 1e-15);
    }

    @Test
    public void testReduce() {
        double a = 5.8;
        double period = 2.0;
        double offset = 1.0;
        double expectedReducedValue = 2.8;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expectedReducedValue, result, 1e-15);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 100;
        byte sign = -1;
        byte expectedCopiedSign = -100;
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            assertEquals(expectedCopiedSign, result);
        } catch (MathArithmeticException ex) {
            fail("Unexpected MathArithmeticException thrown");
        }
    }

    @Test
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException not thrown");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 100;
        short sign = -1;
        short expectedCopiedSign = -100;
        try {
            short result = MathUtils.copySign(magnitude, sign);
            assertEquals(expectedCopiedSign, result);
        } catch (MathArithmeticException ex) {
            fail("Unexpected MathArithmeticException thrown");
        }
    }

    @Test
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException not thrown");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 100;
        int sign = -1;
        int expectedCopiedSign = -100;
        try {
            int result = MathUtils.copySign(magnitude, sign);
            assertEquals(expectedCopiedSign, result);
        } catch (MathArithmeticException ex) {
            fail("Unexpected MathArithmeticException thrown");
        }
    }

    @Test
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException not thrown");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 100;
        long sign = -1;
        long expectedCopiedSign = -100;
        try {
            long result = MathUtils.copySign(magnitude, sign);
            assertEquals(expectedCopiedSign, result);
        } catch (MathArithmeticException ex) {
            fail("Unexpected MathArithmeticException thrown");
        }
    }

    @Test
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException not thrown");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }

    @Test
    public void testCheckFinite_double() {
        double x = 5.0;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException ex) {
            fail("Unexpected NotFiniteNumberException thrown");
        }
    }

    @Test
    public void testCheckFinite_double_positiveInfinity() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException ex) {
            assertTrue(ex.getMessage().contains("Double.POSITIVE_INFINITY"));
        }
    }

    @Test
    public void testCheckFinite_double_negativeInfinity() {
        double x = Double.NEGATIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException ex) {
            assertTrue(ex.getMessage().contains("Double.NEGATIVE_INFINITY"));
        }
    }

    @Test
    public void testCheckFinite_double_NaN() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException ex) {
            assertTrue(ex.getMessage().contains("Double.NaN"));
        }
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.0, 2.0, 3.0};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException ex) {
            fail("Unexpected NotFiniteNumberException thrown");
        }
    }

    @Test
    public void testCheckFinite_doubleArray_positiveInfinity() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException ex) {
            assertTrue(ex.getMessage().contains("Double.POSITIVE_INFINITY"));
            assertTrue(ex.getMessage().contains("Element at index 1"));
        }
    }

    @Test
    public void testCheckFinite_doubleArray_negativeInfinity() {
        double[] val = {1.0, Double.NEGATIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException ex) {
            assertTrue(ex.getMessage().contains("Double.NEGATIVE_INFINITY"));
            assertTrue(ex.getMessage().contains("Element at index 1"));
        }
    }

    @Test
    public void testCheckFinite_doubleArray_NaN() {
        double[] val = {1.0, Double.NaN, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException ex) {
            assertTrue(ex.getMessage().contains("Double.NaN"));
            assertTrue(ex.getMessage().contains("Element at index 1"));
        }
    }

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException ex) {
            fail("Unexpected NullArgumentException thrown");
        }
    }

    @Test
    public void testCheckNotNull_nullPattern() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o, null);
            fail("Expected NullArgumentException not thrown");
        } catch (NullArgumentException ex) {
            assertNull(ex.getPattern());
        }
    }

    @Test
    public void testCheckNotNull_withPattern() {
        Object o = null;
        LocalizedFormats pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {2};
        try {
            MathUtils.checkNotNull(o, pattern, args);
            fail("Expected NullArgumentException not thrown");
        } catch (NullArgumentException ex) {
            assertEquals(pattern, ex.getPattern());
            assertArrayEquals(args, ex.getArguments());
        }
    }
}
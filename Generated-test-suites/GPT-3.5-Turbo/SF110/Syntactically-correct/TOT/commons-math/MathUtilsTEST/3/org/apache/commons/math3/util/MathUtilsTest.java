import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash() {
        // Typical use case
        assertEquals(12345, MathUtils.hash(12345.6789));

        // Edge case
        assertEquals(Integer.MAX_VALUE, MathUtils.hash(Double.MAX_VALUE));

        // Error scenario
        assertEquals(Integer.MIN_VALUE, MathUtils.hash(Double.NaN));
    }

    @Test
    public void testHashArray() {
        // Typical use case
        double[] array = {1.23, 4.56, 7.89};
        assertEquals(123456, MathUtils.hash(array));

        // Edge case
        array = new double[0];
        assertEquals(0, MathUtils.hash(array));

        // Error scenario
        array = null;
        assertEquals(0, MathUtils.hash(array));
    }

    @Test
    public void testNormalizeAngle() {
        // Typical use case
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0001);

        // Edge case
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(-3.141592653589793, 3.141592653589793), 0.0001);

        // Error scenario
        assertEquals(0.0, MathUtils.normalizeAngle(Double.NaN, 0.0), 0.0001);
    }

    @Test
    public void testReduce() {
        // Typical use case
        assertEquals(1.0, MathUtils.reduce(8.0, 7.0, 2.0), 0.0001);

        // Edge case
        assertEquals(0.0, MathUtils.reduce(0.0, 5.0, 0.0), 0.0001);

        // Error scenario
        assertEquals(Double.POSITIVE_INFINITY, MathUtils.reduce(Double.POSITIVE_INFINITY, 1.0, 0.0), 0.0001);
    }

    @Test
    public void testCopySignByte() {
        // Typical use case
        assertEquals((byte) 5, MathUtils.copySign((byte) 5, (byte) 3));

        // Edge case
        assertEquals((byte) -128, MathUtils.copySign((byte) -128, (byte) 3));

        // Error scenario
        try {
            MathUtils.copySign((byte) -128, (byte) -3);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignShort() {
        // Typical use case
        assertEquals((short) 5, MathUtils.copySign((short) 5, (short) 3));

        // Edge case
        assertEquals((short) -32768, MathUtils.copySign((short) -32768, (short) 3));

        // Error scenario
        try {
            MathUtils.copySign((short) -32767, (short) -3);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignInt() {
        // Typical use case
        assertEquals(5, MathUtils.copySign(5, 3));

        // Edge case
        assertEquals(0, MathUtils.copySign(0, 1));

        // Error scenario
        try {
            MathUtils.copySign(Integer.MIN_VALUE, -3);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignLong() {
        // Typical use case
        assertEquals(5L, MathUtils.copySign(5L, 3L));

        // Edge case
        assertEquals(0L, MathUtils.copySign(0L, 1L));

        // Error scenario
        try {
            MathUtils.copySign(Long.MIN_VALUE, -3L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        // Typical use case
        MathUtils.checkFinite(1.23);

        // Edge case
        MathUtils.checkFinite(0.0);

        // Error scenario
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals(0.0, e.getArgument(), 0.0001);
        }
    }

    @Test
    public void testCheckFiniteArray() {
        // Typical use case
        double[] array = {1.23, 4.56, 7.89};
        MathUtils.checkFinite(array);

        // Edge case
        array = new double[0];
        MathUtils.checkFinite(array);

        // Error scenario
        array = new double[] {1.23, Double.POSITIVE_INFINITY, 7.89};
        try {
            MathUtils.checkFinite(array);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            assertEquals(1.23, e.getArgument(), 0.0001);
            assertEquals(1, e.getArguments()[0]);
        }
    }

    @Test
    public void testCheckNotNullWithPattern() {
        // Typical use case
        Object obj = new Object();
        MathUtils.checkNotNull(obj, LocalizedFormats.UNSUPPORTED_OPERATION, "Not supported");

        // Edge case
        MathUtils.checkNotNull(new Object(), LocalizedFormats.UNSUPPORTED_OPERATION);

        // Error scenario
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.UNSUPPORTED_OPERATION);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertEquals(LocalizedFormats.UNSUPPORTED_OPERATION, e.getPattern());
        }
    }

    @Test
    public void testCheckNotNullWithoutPattern() {
        // Typical use case
        Object obj = new Object();
        MathUtils.checkNotNull(obj);

        // Edge case
        MathUtils.checkNotNull(new Object());

        // Error scenario
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Pass
        }
    }

}
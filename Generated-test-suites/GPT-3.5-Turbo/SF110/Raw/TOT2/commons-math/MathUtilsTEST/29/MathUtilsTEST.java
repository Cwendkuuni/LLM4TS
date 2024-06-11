import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        assertEquals(123456789, MathUtils.hash(123.456));
    }

    @Test
    public void testHash_doubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        assertEquals(123456789, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(7.5, 3.0), 0.001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.0, MathUtils.reduce(8.0, 3.0, 2.0), 0.001);
    }

    @Test
    public void testCopySign_byte() {
        try {
            assertEquals(5, MathUtils.copySign((byte) 5, (byte) -3));
            MathUtils.copySign((byte) -128, (byte) 2);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCopySign_short() {
        try {
            assertEquals(10, MathUtils.copySign((short) 10, (short) -6));
            MathUtils.copySign((short) -32768, (short) 4);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCopySign_int() {
        try {
            assertEquals(15, MathUtils.copySign(15, -10));
            MathUtils.copySign(Integer.MIN_VALUE, 100);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCopySign_long() {
        try {
            assertEquals(20, MathUtils.copySign(20L, -15L));
            MathUtils.copySign(Long.MIN_VALUE, 200L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckFinite_double() {
        MathUtils.checkFinite(100.0);
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] values = {1.0, 2.0, Double.NaN};
        try {
            MathUtils.checkFinite(values);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckNotNull_Localizable() {
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 0);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckNotNull_Object() {
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertTrue(true);
        }
    }
}
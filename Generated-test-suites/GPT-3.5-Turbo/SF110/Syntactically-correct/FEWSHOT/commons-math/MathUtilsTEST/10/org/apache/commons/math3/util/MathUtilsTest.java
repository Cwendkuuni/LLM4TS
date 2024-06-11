import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {
    @Test
    public void testHash_double() {
        assertEquals(12345678, MathUtils.hash(12345678.0));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(-12345678, MathUtils.hash(-12345678.0));
    }
    
    @Test
    public void testHash_doubleArray() {
        assertEquals(12345678, MathUtils.hash(new double[]{12345678.0}));
        assertEquals(0, MathUtils.hash(new double[]{0.0}));
        assertEquals(-12345678, MathUtils.hash(new double[]{-12345678.0}));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.000001);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(-3.141592653589793, 0.0), 0.000001);
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(4.71238898038469, 0.0), 0.000001);
    }
    
    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 6.283185307179586, 0.0), 0.000001);
        assertEquals(3.141592653589793, MathUtils.reduce(-3.141592653589793, 6.283185307179586, 0.0), 0.000001);
        assertEquals(1.5707963267948966, MathUtils.reduce(4.71238898038469, 6.283185307179586, 0.0), 0.000001);
    }
    
    @Test
    public void testCopySign_byte() {
        assertEquals((byte)5, MathUtils.copySign((byte)5, (byte)1));
        assertEquals((byte)-5, MathUtils.copySign((byte)5, (byte)-1));
        try {
            MathUtils.copySign((byte)-128, (byte)1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals("", e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_short() {
        assertEquals((short)5, MathUtils.copySign((short)5, (short)1));
        assertEquals((short)-5, MathUtils.copySign((short)5, (short)-1));
        try {
            MathUtils.copySign((short)-32768, (short)1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals("", e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_int() {
        assertEquals(5, MathUtils.copySign(5, 1));
        assertEquals(-5, MathUtils.copySign(5, -1));
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals("", e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_long() {
        assertEquals(5L, MathUtils.copySign(5L, 1L));
        assertEquals(-5L, MathUtils.copySign(5L, -1L));
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals("", e.getMessage());
        }
    }
    
    @Test
    public void testCheckFinite_double() {
        MathUtils.checkFinite(12345678.0);
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(-12345678.0);
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals("", e.getMessage());
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        MathUtils.checkFinite(new double[]{12345678.0});
        MathUtils.checkFinite(new double[]{0.0});
        MathUtils.checkFinite(new double[]{-12345678.0});
        try {
            MathUtils.checkFinite(new double[]{Double.POSITIVE_INFINITY});
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals("", e.getMessage());
        }
    }
    
    @Test
    public void testCheckNotNull_Localizable_ObjectArray() {
        MathUtils.checkNotNull("test", null);
        try {
            MathUtils.checkNotNull(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertEquals("", e.getMessage());
        }
    }
    
    @Test
    public void testCheckNotNull_Object() {
        MathUtils.checkNotNull("test");
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertEquals("", e.getMessage());
        }
    }
}
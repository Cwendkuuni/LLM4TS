import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {
    @Test
    public void testHash_double() {
        assertEquals(231, MathUtils.hash(4.56));
        assertEquals(-123, MathUtils.hash(-45.23));
        assertEquals(0, MathUtils.hash(0.0));
    }

    @Test
    public void testHash_doubleArray() {
        double[] array1 = {1.2, 2.3, 3.4};
        double[] array2 = {0.5, -1.5, 2.5};
        double[] array3 = {-9.8, -7.6, -5.4};
        
        assertEquals(1234, MathUtils.hash(array1));
        assertEquals(-4567, MathUtils.hash(array2));
        assertEquals(-7890, MathUtils.hash(array3));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.23, MathUtils.normalizeAngle(7.53, 1.34), 0.000001);
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.000001);
        assertEquals(-3.14, MathUtils.normalizeAngle(-9.42, 0.0), 0.000001);
    }

    @Test
    public void testReduce() {
        assertEquals(0.5, MathUtils.reduce(12.5, 4.0, 1.0), 0.000001);
        assertEquals(10.0, MathUtils.reduce(28.0, 9.0, 1.5), 0.000001);
        assertEquals(-1.0, MathUtils.reduce(-20.0, 7.0, -5.0), 0.000001);
    }

    @Test
    public void testCopySign_byte() {
        assertEquals((byte)5, MathUtils.copySign((byte)5, (byte)-3));
        assertEquals((byte)-7, MathUtils.copySign((byte)7, (byte)-3));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((byte)-128, (byte)-3));
    }
    
    @Test
    public void testCopySign_short() {
        assertEquals((short)10, MathUtils.copySign((short)10, (short)-20));
        assertEquals((short)-30, MathUtils.copySign((short)30, (short)-20));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((short)-32768, (short)-20));
    }

    @Test
    public void testCopySign_int() {
        assertEquals(50, MathUtils.copySign(50, -30));
        assertEquals(-70, MathUtils.copySign(70, -30));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(Integer.MIN_VALUE, -30));
    }

    @Test
    public void testCopySign_long() {
        assertEquals(12345678987654321L, MathUtils.copySign(12345678987654321L, -98765432123456789L));
        assertEquals(-98765432123456789L, MathUtils.copySign(98765432123456789L, -98765432123456789L));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(Long.MIN_VALUE, -98765432123456789L));
    }

    @Test
    public void testCheckFinite_double() {
        MathUtils.checkFinite(45.67);
        MathUtils.checkFinite(-12.34);
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.POSITIVE_INFINITY));
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.NEGATIVE_INFINITY));
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.NaN));
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] array1 = {1.2, 2.3, 3.4};
        double[] array2 = {0.5, -1.5, Double.POSITIVE_INFINITY};
        double[] array3 = {-9.8, -7.6, Double.NaN};
        
        MathUtils.checkFinite(array1);
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(array2));
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(array3));
    }

    @Test
    public void testCheckNotNull_Localizable_ObjectArray() {
        MathUtils.checkNotNull("abc", LocalizedFormats.INVALID_PARAMETER, "abc");
        MathUtils.checkNotNull(123, LocalizedFormats.INVALID_PARAMETER, "123");
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(null, LocalizedFormats.INVALID_PARAMETER, "null"));
    }

    @Test
    public void testCheckNotNull_Object() {
        MathUtils.checkNotNull("abc");
        MathUtils.checkNotNull(123);
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(null));
    }
}
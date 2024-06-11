import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {
    
    // Test hash(double) method
    @Test
    public void testHashDouble() {
        assertEquals(123456, MathUtils.hash(123456.0));
        assertEquals(-98765, MathUtils.hash(-98765.0));
        assertEquals(0, MathUtils.hash(0.0));
    }
    
    // Test hash(double[]) method
    @Test
    public void testHashDoubleArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        assertEquals(3089515, MathUtils.hash(array1));
        
        double[] array2 = {-4.0, -5.0, -6.0};
        assertEquals(-7222653, MathUtils.hash(array2));
        
        double[] array3 = {};
        assertEquals(1, MathUtils.hash(array3));
    }
    
    // Test normalizeAngle(double, double) method
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0001);
        assertEquals(0.0, MathUtils.normalizeAngle(2 * Math.PI, Math.PI), 0.0001);
        assertEquals(Math.PI / 4, MathUtils.normalizeAngle(9 * Math.PI / 4, Math.PI / 2), 0.0001);
    }
    
    // Test reduce(double, double, double) method
    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 5.0, 0.0), 0.0001);
        assertEquals(1.0, MathUtils.reduce(7.0, 3.0, 1.0), 0.0001);
        assertEquals(2.0, MathUtils.reduce(-8.0, 3.0, 2.0), 0.0001);
    }
    
    // Test copySign(byte, byte) method
    @Test
    public void testCopySignByte() throws MathArithmeticException {
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
        assertEquals((byte) -1, MathUtils.copySign((byte) -1, (byte) 1));
        assertEquals((byte) -128, MathUtils.copySign((byte) 128, (byte) -1));
    }
    
    // Test copySign(short, short) method
    @Test
    public void testCopySignShort() throws MathArithmeticException {
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
        assertEquals((short) -1, MathUtils.copySign((short) -1, (short) 1));
        assertEquals((short) -32768, MathUtils.copySign((short) 32768, (short) -1));
    }
    
    // Test copySign(int, int) method
    @Test
    public void testCopySignInt() throws MathArithmeticException {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(Integer.MIN_VALUE, MathUtils.copySign(Integer.MAX_VALUE, -1));
    }
    
    // Test copySign(long, long) method
    @Test
    public void testCopySignLong() throws MathArithmeticException {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, 1L));
        assertEquals(Long.MIN_VALUE, MathUtils.copySign(Long.MAX_VALUE, -1L));
    }
    
    // Test checkFinite(double) method
    @Test
    public void testCheckFiniteDouble() throws NotFiniteNumberException {
        MathUtils.checkFinite(123.456);
        MathUtils.checkFinite(-987.654);
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("NotFiniteNumberException should have been thrown");
        } catch (NotFiniteNumberException e) {
            // exception expected
        }
    }
    
    // Test checkFinite(double[]) method
    @Test
    public void testCheckFiniteDoubleArray() throws NotFiniteNumberException {
        double[] array1 = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(array1);
        
        double[] array2 = {Double.POSITIVE_INFINITY, -5.0, 6.0};
        try {
            MathUtils.checkFinite(array2);
            fail("NotFiniteNumberException should have been thrown");
        } catch (NotFiniteNumberException e) {
            // exception expected
        }
    }
    
    // Test checkNotNull(Object, Localizable, Object...) method
    @Test
    public void testCheckNotNullWithMessage() throws NullArgumentException {
        MathUtils.checkNotNull("test", LocalizedFormats.ARRAY_ELEMENT, 1);
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 1);
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException e) {
            // exception expected
        }
    }
    
    // Test checkNotNull(Object) method
    @Test
    public void testCheckNotNull() throws NullArgumentException {
        MathUtils.checkNotNull("test");
        try {
            MathUtils.checkNotNull(null);
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException e) {
            // exception expected
        }
    }
}
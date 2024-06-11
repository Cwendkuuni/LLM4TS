import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        assertEquals(-1390183815, MathUtils.hash(-3.5));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(1390183815, MathUtils.hash(3.5));
    }
    
    @Test
    public void testHashArray() {
        double[] arr1 = {1.0, 2.0, 3.0};
        double[] arr2 = {1.0, 2.0, 3.0};
        double[] arr3 = {3.0, 2.0, 1.0};
        
        assertEquals(490138654, MathUtils.hash(arr1));
        assertEquals(490138654, MathUtils.hash(arr2));
        assertEquals(490387225, MathUtils.hash(arr3));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(5.0, MathUtils.normalizeAngle(-1.0, 2.0), 0.000001);
        assertEquals(0.0, MathUtils.normalizeAngle(6.0, 1.0), 0.000001);
        assertEquals(3.0, MathUtils.normalizeAngle(-9.0, 4.0), 0.000001);
    }
    
    @Test
    public void testReduce() {
        assertEquals(3.0, MathUtils.reduce(-5.0, 8.0, 2.0), 0.000001);
        assertEquals(5.0, MathUtils.reduce(17.0, 6.0, 1.0), 0.000001);
        assertEquals(2.0, MathUtils.reduce(-13.0, -4.0, 0.0), 0.000001);
    }
    
    @Test
    public void testCopySignByte() throws MathArithmeticException {
        assertEquals(3, MathUtils.copySign((byte)3, (byte)2));
        assertEquals(-3, MathUtils.copySign((byte)-3, (byte)2));
        try {
            MathUtils.copySign((byte)128, (byte)1);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testCopySignShort() throws MathArithmeticException {
        assertEquals(7, MathUtils.copySign((short)7, (short)4));
        assertEquals(-7, MathUtils.copySign((short)-7, (short)4));
        try {
            MathUtils.copySign((short)32768, (short)1);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testCopySignInt() throws MathArithmeticException {
        assertEquals(12, MathUtils.copySign(12, 5));
        assertEquals(-12, MathUtils.copySign(-12, 5));
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testCopySignLong() throws MathArithmeticException {
        assertEquals(25, MathUtils.copySign(25L, 18L));
        assertEquals(-25, MathUtils.copySign(-25L, 18L));
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // expected
        }
    }
    
    @Test
    public void testCheckFinite() throws NotFiniteNumberException {
        MathUtils.checkFinite(3.5);
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(-2.8);
        
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // expected
        }
        
        try {
            MathUtils.checkFinite(Double.NaN);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // expected
        }
    }
    
    @Test
    public void testCheckFiniteArray() throws NotFiniteNumberException {
        double[] arr1 = {1.0, 2.0, 3.0};
        double[] arr2 = { Double.POSITIVE_INFINITY, 1.0, Double.NaN };
        
        MathUtils.checkFinite(arr1);
        MathUtils.checkFinite(arr2);
        
        double[] arr3 = { 3.0, Double.NaN, 5.0 };
        try {
            MathUtils.checkFinite(arr3);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals("Array element at index 1 is not a finite number", e.getMessage());
        }
    }
    
    @Test
    public void testCheckNotNull() throws NullArgumentException {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, null);
        
        try {
            MathUtils.checkNotNull(null, null);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testCheckNotNullPattern() throws NullArgumentException {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
        
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // expected
        }
    }
}
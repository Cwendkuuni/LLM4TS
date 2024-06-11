import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1070192796, MathUtils.hash(3.14));
        assertEquals(975401877, MathUtils.hash(6.283));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(1077533486, MathUtils.hash(-5.7));
    }
    
    @Test
    public void testHashDoubleArray() {
        assertEquals(-889275714, MathUtils.hash(new double[]{1.0, 2.0, 3.0}));
        assertEquals(961156862, MathUtils.hash(new double[]{0.5, 1.5, 2.5}));
        assertEquals(0, MathUtils.hash(new double[]{}));
        assertEquals(2057682484, MathUtils.hash(new double[]{-1.0, -2.0, -3.0}));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(-0.14159265358979312, MathUtils.normalizeAngle(6.142592653589793, 3.0), 0.0000000001);
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 3.0), 0.0000000001);
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0000000001);
        assertEquals(3.0, MathUtils.normalizeAngle(9.42477796076938, 3.0), 0.0000000001);
    }

    @Test
    public void testReduce() {
        assertEquals(4.0, MathUtils.reduce(70.3, 10.0, 6.0), 0.0000000001);
        assertEquals(1.0, MathUtils.reduce(91.0, 90.0, 0.0), 0.0000000001);
        assertEquals(0.0, MathUtils.reduce(24.0, 0.1, 0.0), 0.0000000001);
        assertEquals(0.0, MathUtils.reduce(340.0, 360.0, 0.0), 0.0000000001);
    }

    @Test
    public void testCopySignByte() {
        try {
            assertEquals(5, MathUtils.copySign((byte)5, (byte)3));
            assertEquals(-5, MathUtils.copySign((byte)5, (byte)-3));
            assertEquals(-5, MathUtils.copySign((byte)-5, (byte)3));
            assertEquals(0, MathUtils.copySign((byte)0, (byte)3));
        } catch (MathArithmeticException ex) {
            fail("No arithmetic exception should be thrown");
        }
        
        try {
            MathUtils.copySign((byte)-128, (byte)-1);
            fail("MathArithmeticException should be thrown");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern().getMessage());
        }
    }
    
    @Test
    public void testCopySignShort() {
        try {
            assertEquals(10, MathUtils.copySign((short)10, (short)7));
            assertEquals(-10, MathUtils.copySign((short)10, (short)-7));
            assertEquals(-10, MathUtils.copySign((short)-10, (short)7));
            assertEquals(0, MathUtils.copySign((short)0, (short)7));
        } catch (MathArithmeticException ex) {
            fail("No arithmetic exception should be thrown");
        }
        
        try {
            MathUtils.copySign((short)-32768, (short)-1);
            fail("MathArithmeticException should be thrown");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern().getMessage());
        }
    }

    @Test
    public void testCopySignInt() {
        try {
            assertEquals(100, MathUtils.copySign(100, 70));
            assertEquals(-100, MathUtils.copySign(100, -70));
            assertEquals(-100, MathUtils.copySign(-100, 70));
            assertEquals(0, MathUtils.copySign(0, 70));
        } catch (MathArithmeticException ex) {
            fail("No arithmetic exception should be thrown");
        }
        
        try {
            MathUtils.copySign(Integer.MIN_VALUE, -1);
            fail("MathArithmeticException should be thrown");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern().getMessage());
        }
    }
    
    @Test
    public void testCopySignLong() {
        try {
            assertEquals(1000L, MathUtils.copySign(1000L, 700L));
            assertEquals(-1000L, MathUtils.copySign(1000L, -700L));
            assertEquals(-1000L, MathUtils.copySign(-1000L, 700L));
            assertEquals(0L, MathUtils.copySign(0L, 700L));
        } catch (MathArithmeticException ex) {
            fail("No arithmetic exception should be thrown");
        }
        
        try {
            MathUtils.copySign(Long.MIN_VALUE, -1L);
            fail("MathArithmeticException should be thrown");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern().getMessage());
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        try {
            MathUtils.checkFinite(3.14);
        } catch (NotFiniteNumberException ex) {
            fail("No NotFiniteNumberException should be thrown");
        }
        
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException ex) {
            // Exception expected
        }
        
        try {
            MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException ex) {
            // Exception expected
        }
        
        try {
            MathUtils.checkFinite(Double.NaN);
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException ex) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckFiniteDoubleArray() {
        try {
            MathUtils.checkFinite(new double[]{1.0, 2.0, 3.0});
        } catch (NotFiniteNumberException ex) {
            fail("No NotFiniteNumberException should be thrown");
        }
        
        try {
            MathUtils.checkFinite(new double[]{Double.POSITIVE_INFINITY, 2.0, 3.0});
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException ex) {
            // Exception expected
        }
        
        try {
            MathUtils.checkFinite(new double[]{1.0, Double.NEGATIVE_INFINITY, 3.0});
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException ex) {
            // Exception expected
        }
        
        try {
            MathUtils.checkFinite(new double[]{1.0, 2.0, Double.NaN});
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException ex) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckNotNull() {
        try {
            MathUtils.checkNotNull(new Object());
        } catch (NullArgumentException ex) {
            fail("No NullArgumentException should be thrown");
        }
        
        try {
            MathUtils.checkNotNull(null);
            fail("NullArgumentException should be thrown");
        } catch (NullArgumentException ex) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckNotNullWithPattern() {
        try {
            MathUtils.checkNotNull(new Object(), LocalizedFormats.ARRAY_ELEMENT, 0);
        } catch (NullArgumentException ex) {
            fail("No NullArgumentException should be thrown");
        }
        
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 0);
            fail("NullArgumentException should be thrown");
        } catch (NullArgumentException ex) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, ex.getPattern().getMessage());
        }
    }
}
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHashDouble() {
        double value = 3.14;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testHashDoubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 4.5;
        double center = 2.0;
        double expected = a - MathUtils.TWO_PI * FastMath.floor((a + 3.141592653589793 - center) / MathUtils.TWO_PI);
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 5.0;
        double offset = 1.5;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -20;
        
        byte expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (byte) -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -200;
        
        short expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (short) -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -2000;
        
        int expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignLong() {
        long magnitude = 1000000000L;
        long sign = -2000000000L;
        
        long expected = (magnitude >= 0L && sign >= 0L) || (magnitude < 0L && sign < 0L) ? magnitude : -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite() {
        double x = 3.14;
        
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            Assert.fail("NotFiniteNumberException thrown");
        }
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinity() {
        double x = Double.POSITIVE_INFINITY;
        
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.0, 2.0, 3.0};
        
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            Assert.fail("NotFiniteNumberException thrown");
        }
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayInfinity() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNaN() {
        double[] val = {1.0, Double.NaN, 3.0};
        
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            Assert.fail("NullArgumentException thrown");
        }
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        Object o = null;
        
        MathUtils.checkNotNull(o, null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        
        MathUtils.checkNotNull(o, pattern, args);
    }
}
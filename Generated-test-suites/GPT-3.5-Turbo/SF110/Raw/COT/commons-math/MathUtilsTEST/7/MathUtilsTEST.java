import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class MathUtilsTest {

    @Test
    public void testHash() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testHashArray() {
        double[] value = {10.5, 20.3};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 2.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = -magnitude;
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
        short sign = -50;
        short expected = -magnitude;
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
        int magnitude = 100;
        int sign = -50;
        int expected = -magnitude;
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
        long magnitude = 100L;
        long sign = -50L;
        long expected = -magnitude;
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
        double x = 10.5;
        MathUtils.checkFinite(x);
        Assert.assertTrue(true);
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
        double[] val = {10.5, -20.3};
        MathUtils.checkFinite(val);
        Assert.assertTrue(true);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayInfinity() {
        double[] val = {10.5, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNaN() {
        double[] val = {10.5, Double.NaN};
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNullPattern() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(o, pattern, args);
        Assert.assertTrue(true);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullPatternNull() {
        Object o = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(o, pattern, args);
    }
    
    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
        Assert.assertTrue(true);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNull() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}


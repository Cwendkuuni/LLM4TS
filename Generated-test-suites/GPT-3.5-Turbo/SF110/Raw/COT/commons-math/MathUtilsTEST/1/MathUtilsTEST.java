
import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {

    // Test case for hash(double) method
    @Test
    public void testHashDouble() {
        double value = 10.5;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    // Test case for hash(double[]) method
    @Test
    public void testHashDoubleArray() {
        double[] value = {10.5, 20.3, 30.1};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    // Test case for normalizeAngle(double, double) method
    @Test
    public void testNormalizeAngle() {
        double a = 3.5;
        double center = 1.2;
        double expected = a - MathUtils.TWO_PI * FastMath.floor((a + FastMath.PI - center) / MathUtils.TWO_PI);
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    // Test case for reduce(double, double, double) method
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 5.0;
        double offset = 2.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    // Test case for copySign(byte, byte) method
    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (byte) (-magnitude);
        byte actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    // Test case for copySign(short, short) method
    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -50;
        short expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (short) (-magnitude);
        short actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    // Test case for copySign(int, int) method
    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -500;
        int expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    // Test case for copySign(long, long) method
    @Test
    public void testCopySignLong() {
        long magnitude = 1000000L;
        long sign = -500000L;
        long expected = (magnitude >= 0L && sign >= 0L) || (magnitude < 0L && sign < 0L) ? magnitude : -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    // Test case for checkFinite(double) method with finite number
    @Test
    public void testCheckFiniteWithFiniteNumber() {
        double x = 10.5;
        MathUtils.checkFinite(x);
    }

    // Test case for checkFinite(double) method with infinite number
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithInfiniteNumber() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    // Test case for checkFinite(double) method with NaN
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    // Test case for checkFinite(double[]) method with finite numbers
    @Test
    public void testCheckFiniteArrayWithFiniteNumbers() {
        double[] val = {10.5, 20.3, 30.1};
        MathUtils.checkFinite(val);
    }

    // Test case for checkFinite(double[]) method with infinite number
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayWithInfiniteNumber() {
        double[] val = {10.5, Double.POSITIVE_INFINITY, 30.1};
        MathUtils.checkFinite(val);
    }

    // Test case for checkFinite(double[]) method with NaN
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayWithNaN() {
        double[] val = {10.5, Double.NaN, 30.1};
        MathUtils.checkFinite(val);
    }

    // Test case for checkNotNull(Object, Localizable, Object...) method with non-null object
    @Test
    public void testCheckNotNullWithNonNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o, null);
    }

    // Test case for checkNotNull(Object, Localizable, Object...) method with null object
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o, null);
    }

    // Test case for checkNotNull(Object) method with non-null object
    @Test
    public void testCheckNotNullObjectWithNonNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    // Test case for checkNotNull(Object) method with null object
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectWithNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}


import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;

public class MathUtilsTest {

    @Test
    public void testHash_PositiveValue() {
        double value = 5.0;
        int expectedHashValue = Double.valueOf(value).hashCode();
        int hashValue = MathUtils.hash(value);
        Assert.assertEquals(expectedHashValue, hashValue);
    }
    
    @Test
    public void testHash_NegativeValue() {
        double value = -3.0;
        int expectedHashValue = Double.valueOf(value).hashCode();
        int hashValue = MathUtils.hash(value);
        Assert.assertEquals(expectedHashValue, hashValue);
    }
    
    @Test
    public void testHash_ZeroValue() {
        double value = 0.0;
        int expectedHashValue = Double.valueOf(value).hashCode();
        int hashValue = MathUtils.hash(value);
        Assert.assertEquals(expectedHashValue, hashValue);
    }
    
    @Test
    public void testHashArray() {
        double[] value = {2.0, 4.0, 6.0};
        int expectedHashValue = Double.valueOf(Arrays.hashCode(value)).hashCode();
        int hashValue = MathUtils.hash(value);
        Assert.assertEquals(expectedHashValue, hashValue);
    }
    
    @Test
    public void testNormalizeAngle_WithinBounds() {
        double a = 4.5;
        double center = 2.0;
        double expectedNormalizedAngle = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double normalizedAngle = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expectedNormalizedAngle, normalizedAngle, 0.001);
    }
    
    @Test
    public void testNormalizeAngle_OutsideBounds() {
        double a = 10.5;
        double center = 2.0;
        double expectedNormalizedAngle = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double normalizedAngle = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expectedNormalizedAngle, normalizedAngle, 0.001);
    }
    
    @Test
    public void testReduce_WithinPeriod() {
        double a = 20.0;
        double period = 10.0;
        double offset = 5.0;
        double expectedReducedValue = a - period * FastMath.floor((a - offset) / period) - offset;
        double reducedValue = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expectedReducedValue, reducedValue, 0.001);
    }
    
    @Test
    public void testReduce_OutsidePeriod() {
        double a = 10.0;
        double period = 20.0;
        double offset = 5.0;
        double expectedReducedValue = a - period * FastMath.floor((a - offset) / period) - offset;
        double reducedValue = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expectedReducedValue, reducedValue, 0.001);
    }
    
    @Test
    public void testCopySign_PositiveMagnitudePositiveSign() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = 5;
        byte expectedValue = magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expectedValue, result);
    }
    
    @Test
    public void testCopySign_NegativeMagnitudeNegativeSign() throws MathArithmeticException {
        byte magnitude = -10;
        byte sign = -5;
        byte expectedValue = magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expectedValue, result);
    }
    
    @Test
    public void testCopySign_PositiveMagnitudeNegativeSign() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = -5;
        byte expectedValue = (byte)(-magnitude);
        byte result = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expectedValue, result);
    }
    
    @Test
    public void testCopySign_NegativeMagnitudePositiveSign() throws MathArithmeticException {
        byte magnitude = -10;
        byte sign = 5;
        byte expectedValue = (byte)(-magnitude);
        byte result = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expectedValue, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_OVERFLOW_PositiveMagnitudeMaxValueSign() throws MathArithmeticException {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_OVERFLOW_PositiveMagnitudeMaxValueSignExpectedExceptionMessage() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException exception) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, exception.getSpecifier());
        }
    }
    
    // Similar test cases can be created for the remaining copySign methods
    
    @Test
    public void testCheckFinite_ValidFiniteValue() throws NotFiniteNumberException {
        double x = 10.5;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_InfiniteValue() throws NotFiniteNumberException {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_NaNValue() throws NotFiniteNumberException {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteArray_ValidFiniteValues() throws NotFiniteNumberException {
        double[] values = {4.5, 6.7, 8.9};
        MathUtils.checkFinite(values);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_InfiniteValue() throws NotFiniteNumberException {
        double[] values = {4.5, Double.POSITIVE_INFINITY, 8.9};
        MathUtils.checkFinite(values);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_NaNValue() throws NotFiniteNumberException {
        double[] values = {4.5, Double.NaN, 8.9};
        MathUtils.checkFinite(values);
    }
    
    @Test
    public void testCheckNotNull_ValidObjectPattern() throws NullArgumentException {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = {};
        MathUtils.checkNotNull(o, pattern, args);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_NullObjectPattern() throws NullArgumentException {
        Object o = null;
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = {};
        MathUtils.checkNotNull(o, pattern, args);
    }
    
    @Test
    public void testCheckNotNull_ValidObject() throws NullArgumentException {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_NullObject() throws NullArgumentException {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

}
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.MathArithmeticException;
import org.apache.commons.math3.exception.util.NotFiniteNumberException;
import org.apache.commons.math3.exception.*;
import org.junit.Before;

public class MathUtilsTest {

    private MathUtils mathUtils;

    @Before
    public void setUp() {
        mathUtils = new MathUtils();
    }

    @Test
    public void testHash_whenValidDouble_thenReturnHashCode() {
        double value = 10.5;
        int expectedHashCode = Double.valueOf(value).hashCode();
        
        int actualHashCode = mathUtils.hash(value);
        
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testHash_whenValidDoubleArray_thenReturnHashCode() {
        double[] value = {3.14, 2.7, 1.618};
        int expectedHashCode = value.hashCode();
        
        int actualHashCode = mathUtils.hash(value);
        
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testNormalizeAngle_whenAIsWithin2PiRange_thenReturnNormalizedAngle() {
        double a = 3.456;
        double center = 0.0;
        double expectedNormalizedAngle = a;
        
        double actualNormalizedAngle = mathUtils.normalizeAngle(a, center);
        
        assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.0001);
    }
    
    @Test
    public void testNormalizeAngle_whenAIsOutside2PiRange_thenReturnNormalizedAngle() {
        double a = -7.8;
        double center = 0.0;
        double expectedNormalizedAngle = a + MathUtils.TWO_PI;
        
        double actualNormalizedAngle = mathUtils.normalizeAngle(a, center);
        
        assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.0001);
    }
    
    @Test
    public void testReduce_whenAIsWithinPeriodRange_thenReturnReducedValue() {
        double a = 5.0;
        double period = 10.0;
        double offset = 0.0;
        double expectedReducedValue = 5.0;
        
        double actualReducedValue = mathUtils.reduce(a, period, offset);
        
        assertEquals(expectedReducedValue, actualReducedValue, 0.0001);
    }
    
    @Test
    public void testReduce_whenAIsOutsidePeriodRange_thenReturnReducedValue() {
        double a = 15.0;
        double period = 10.0;
        double offset = 0.0;
        double expectedReducedValue = 5.0;
        
        double actualReducedValue = mathUtils.reduce(a, period, offset);
        
        assertEquals(expectedReducedValue, actualReducedValue, 0.0001);
    }
    
    @Test
    public void testCopySign_whenMagnitudeAndSignHaveSameSign_thenReturnMagnitude() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = 20;
        byte expectedCopiedSign = magnitude;
        
        byte actualCopiedSign = mathUtils.copySign(magnitude, sign);
        
        assertEquals(expectedCopiedSign, actualCopiedSign);
    }
    
    @Test
    public void testCopySign_whenMagnitudeAndSignHaveDifferentSigns_thenReturnNegatedMagnitude() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = -20;
        byte expectedCopiedSign = (byte) -magnitude;
        
        byte actualCopiedSign = mathUtils.copySign(magnitude, sign);
        
        assertEquals(expectedCopiedSign, actualCopiedSign);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_whenMagnitudeIsMinValue_thenReturnOverflowException() throws MathArithmeticException {
        byte magnitude = Byte.MIN_VALUE;
        byte sign = 10;
        
        mathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite_whenXIsFinite_thenReturnNoException() throws NotFiniteNumberException {
        double x = 1.5;
        
        mathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_whenXIsInfinite_thenReturnNotFiniteNumberException() throws NotFiniteNumberException {
        double x = Double.POSITIVE_INFINITY;
        
        mathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_whenXIsNaN_thenReturnNotFiniteNumberException() throws NotFiniteNumberException {
        double x = Double.NaN;
        
        mathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteArray_whenAllValuesInArrayAreFinite_thenReturnNoException() throws NotFiniteNumberException {
        double[] val = {1.5, -2.0, 3.14};
        
        mathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_whenSomeValuesInArrayAreInfinite_thenReturnNotFiniteNumberException() throws NotFiniteNumberException {
        double[] val = {1.5, Double.POSITIVE_INFINITY, 3.14};
        
        mathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_whenSomeValuesInArrayAreNaN_thenReturnNotFiniteNumberException() throws NotFiniteNumberException {
        double[] val = {1.5, Double.NaN, 3.14};
        
        mathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNull_whenObjectNotNull_thenReturnNoException() throws NullArgumentException {
        Object obj = new Object();
        
        mathUtils.checkNotNull(obj);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_whenObjectNull_thenReturnNullArgumentException() throws NullArgumentException {
        Object obj = null;
        
        mathUtils.checkNotNull(obj);
    }
    
    @Test
    public void testCheckNotNull_withPatternAndArgs_whenObjectNotNull_thenReturnNoException() throws NullArgumentException {
        Object obj = new Object();
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = {"arg1"};
        
        mathUtils.checkNotNull(obj, pattern, args);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withPatternAndArgs_whenObjectNull_thenReturnNullArgumentException() throws NullArgumentException {
        Object obj = null;
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = {"arg1"};
        
        mathUtils.checkNotNull(obj, pattern, args);
    }

}
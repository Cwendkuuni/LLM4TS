import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNumeratorDegreesOfFreedomNegative() {
        new FDistribution(-1, 4);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNumeratorDegreesOfFreedomZero() {
        new FDistribution(0, 4);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorDenominatorDegreesOfFreedomNegative() {
        new FDistribution(2, -1);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorDenominatorDegreesOfFreedomZero() {
        new FDistribution(2, 0);
    }
    
    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(2, 3);
        double x = 1;
        double result = fDist.density(x);
        double expected = 0.44684565157328404;
        assertEquals(expected, result, 1e-9);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(2, 3);
        double x = 1;
        double result = fDist.cumulativeProbability(x);
        double expected = 0.6085471749234639;
        assertEquals(expected, result, 1e-9);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(2, 3);
        double result = fDist.getNumeratorDegreesOfFreedom();
        double expected = 2;
        assertEquals(expected, result, 1e-9);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(2, 3);
        double result = fDist.getDenominatorDegreesOfFreedom();
        double expected = 3;
        assertEquals(expected, result, 1e-9);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(2, 3);
        double result = fDist.getNumericalMean();
        double expected = 3;
        assertEquals(expected, result, 1e-9);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(2, 3);
        double result = fDist.getNumericalVariance();
        double expected = 3;
        assertEquals(expected, result, 1e-9);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(2, 3);
        double result = fDist.getSupportLowerBound();
        double expected = 0;
        assertEquals(expected, result, 1e-9);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(2, 3);
        double result = fDist.getSupportUpperBound();
        double expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, result, 1e-9);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(2, 3);
        boolean result = fDist.isSupportLowerBoundInclusive();
        assertTrue(result);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(2, 3);
        boolean result = fDist.isSupportUpperBoundInclusive();
        assertFalse(result);
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fDist = new FDistribution(2, 3);
        boolean result = fDist.isSupportConnected();
        assertTrue(result);
    }
}
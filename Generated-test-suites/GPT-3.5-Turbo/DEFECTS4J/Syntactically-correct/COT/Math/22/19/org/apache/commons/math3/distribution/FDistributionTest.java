import org.junit.Test;
import org.apache.commons.math3.distribution.*;

public class FDistributionTest {
    
    // Test constructor with invalid numerator degrees of freedom
    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testConstructorInvalidNumeratorDF() {
        FDistribution f = new FDistribution(-1, 5);
    }
    
    // Test constructor with invalid denominator degrees of freedom
    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testConstructorInvalidDenominatorDF() {
        FDistribution f = new FDistribution(4, 0);
    }
    
    // Test density method
    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(4, 6);
        double density = f.density(3);
        assertEquals(0.1148, density, 0.0001);
    }
    
    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(4, 6);
        double cumulativeProbability = f.cumulativeProbability(2);
        assertEquals(0.5483, cumulativeProbability, 0.0001);
    }
    
    // Test getNumeratorDegreesOfFreedom method
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(4, 6);
        double numeratorDF = f.getNumeratorDegreesOfFreedom();
        assertEquals(4, numeratorDF, 0);
    }
    
    // Test getDenominatorDegreesOfFreedom method
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(4, 6);
        double denominatorDF = f.getDenominatorDegreesOfFreedom();
        assertEquals(6, denominatorDF, 0);
    }
    
    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(4, 6);
        double mean = f.getNumericalMean();
        assertEquals(1.5, mean, 0);
    }
    
    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(4, 6);
        double variance = f.getNumericalVariance();
        assertEquals(2.5714, variance, 0.0001);
    }
    
    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(4, 6);
        double lowerBound = f.getSupportLowerBound();
        assertEquals(0, lowerBound, 0);
    }
    
    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(4, 6);
        double upperBound = f.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0);
    }
    
    // Test isSupportLowerBoundInclusive method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(4, 6);
        boolean lowerBoundInclusive = f.isSupportLowerBoundInclusive();
        assertTrue(lowerBoundInclusive);
    }
    
    // Test isSupportUpperBoundInclusive method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(4, 6);
        boolean upperBoundInclusive = f.isSupportUpperBoundInclusive();
        assertFalse(upperBoundInclusive);
    }
    
    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(4, 6);
        boolean supportConnected = f.isSupportConnected();
        assertTrue(supportConnected);
    }
}
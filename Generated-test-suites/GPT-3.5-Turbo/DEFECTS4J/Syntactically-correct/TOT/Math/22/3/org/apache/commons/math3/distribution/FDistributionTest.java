import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {
    
    private FDistribution fDistribution;
    
    @Before
    public void setup() {
        fDistribution = new FDistribution(3, 5);
    }
    
    @Test
    public void testDensity() {
        double density = fDistribution.density(0.5);
        assertEquals(0.458882, density, 0.000001);
    }
    
    @Test
    public void testDensityWithNegativeX() {
        double density = fDistribution.density(-2);
        assertEquals(0, density, 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() {
        double probability = fDistribution.cumulativeProbability(1);
        assertEquals(0.7381, probability, 0.0001);
    }
    
    @Test
    public void testCumulativeProbabilityWithNegativeX() {
        double probability = fDistribution.cumulativeProbability(-2);
        assertEquals(0, probability, 0.0001);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double degreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(3, degreesOfFreedom, 0.0001);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double degreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(5, degreesOfFreedom, 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        double lowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, lowerBound, 0.0001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        double upperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.0001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean isLowerInclusive = fDistribution.isSupportLowerBoundInclusive();
        assertTrue(isLowerInclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean isUpperInclusive = fDistribution.isSupportUpperBoundInclusive();
        assertFalse(isUpperInclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        boolean isSupportConnected = fDistribution.isSupportConnected();
        assertTrue(isSupportConnected);
    }
    
    @Test
    public void testGetNumericalMean() {
        double mean = fDistribution.getNumericalMean();
        assertEquals(1.25, mean, 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        double variance = fDistribution.getNumericalVariance();
        assertEquals(3.125, variance, 0.0001);
    }
}
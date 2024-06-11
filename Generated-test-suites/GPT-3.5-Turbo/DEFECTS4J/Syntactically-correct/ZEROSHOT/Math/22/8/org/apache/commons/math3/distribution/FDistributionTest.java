import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {
    
    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(5, 10);
        // Test known density values
        assertEquals(0.1591560403, dist.density(0.5), 1e-9);
        assertEquals(0.6886871474, dist.density(1.5), 1e-9);
        assertEquals(0.09646360125, dist.density(2.5), 1e-9);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(5, 10);
        // Test known cumulative probability values
        assertEquals(0.04333770646, dist.cumulativeProbability(0.5), 1e-9);
        assertEquals(0.7447633084, dist.cumulativeProbability(1.5), 1e-9);
        assertEquals(0.1843619846, dist.cumulativeProbability(2.5), 1e-9);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(5, 10);
        assertEquals(5, dist.getNumeratorDegreesOfFreedom(), 0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(5, 10);
        assertEquals(10, dist.getDenominatorDegreesOfFreedom(), 0);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(5, 10);
        assertEquals(1.25, dist.getNumericalMean(), 0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(5, 10);
        assertEquals(1.875, dist.getNumericalVariance(), 0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(5, 10);
        assertEquals(0, dist.getSupportLowerBound(), 0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(5, 10);
        assertTrue(Double.isInfinite(dist.getSupportUpperBound()));
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(5, 10);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(5, 10);
        assertTrue(!dist.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(5, 10);
        assertTrue(dist.isSupportConnected());
    }
}
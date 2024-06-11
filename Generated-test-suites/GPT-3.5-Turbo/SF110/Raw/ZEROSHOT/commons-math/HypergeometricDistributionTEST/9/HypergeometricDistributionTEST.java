import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.175, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.675, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        int[] domain = distribution.getDomain(10, 4, 3);
        assertEquals(0, domain[0]);
        assertEquals(4, domain[1]);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        int lowerDomain = distribution.getLowerDomain(10, 4, 3);
        assertEquals(1, lowerDomain);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(4, distribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(10, distribution.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(3, distribution.getSampleSize());
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.0911, distribution.probability(1), 0.0001);
        assertEquals(0.5455, distribution.probability(2), 0.0001);
        assertEquals(0.3636, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
        assertEquals(0.0, distribution.probability(5), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.825, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.325, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(1.0, distribution.innerCumulativeProbability(4, 4, 1), 0.0001);
        assertEquals(0.3636, distribution.innerCumulativeProbability(3, 3, -1), 0.0001);
        assertEquals(0.9091, distribution.innerCumulativeProbability(2, 2, -1), 0.0001);
        assertEquals(0.8182, distribution.innerCumulativeProbability(1, 1, -1), 0.0001);
        assertEquals(0.1818, distribution.innerCumulativeProbability(0, 0, -1), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(1.2, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(0.6857, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(1, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertTrue(distribution.isSupportConnected());
    }
}
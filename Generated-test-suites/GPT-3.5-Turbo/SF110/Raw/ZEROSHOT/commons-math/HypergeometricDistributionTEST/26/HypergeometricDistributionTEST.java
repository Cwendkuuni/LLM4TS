import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test for values outside the domain
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.0001);
        
        // Test for values inside the domain
        assertEquals(0.5714, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.7857, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        int[] domain = distribution.getDomain(10, 2, 3);
        assertEquals(0, domain[0]);
        assertEquals(2, domain[1]);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(0, distribution.getLowerDomain(10, 2, 3));
        assertEquals(0, distribution.getLowerDomain(5, 2, 3));
        assertEquals(0, distribution.getLowerDomain(6, 2, 3));
        assertEquals(1, distribution.getLowerDomain(7, 2, 3));
        assertEquals(1, distribution.getLowerDomain(8, 2, 3));
        assertEquals(2, distribution.getLowerDomain(9, 2, 3));
        assertEquals(2, distribution.getLowerDomain(10, 2, 3));
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(2, distribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(10, distribution.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(3, distribution.getSampleSize());
    }
    
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(2, distribution.getUpperDomain(10, 2, 3));
        assertEquals(2, distribution.getUpperDomain(5, 2, 3));
        assertEquals(2, distribution.getUpperDomain(6, 2, 3));
        assertEquals(2, distribution.getUpperDomain(7, 2, 3));
        assertEquals(2, distribution.getUpperDomain(8, 2, 3));
        assertEquals(2, distribution.getUpperDomain(9, 2, 3));
        assertEquals(2, distribution.getUpperDomain(10, 2, 3));
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test for values outside the domain
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.0, distribution.probability(6), 0.0001);
        
        // Test for values inside the domain
        assertEquals(0.4286, distribution.probability(1), 0.0001);
        assertEquals(0.2857, distribution.probability(2), 0.0001);
        assertEquals(0.2857, distribution.probability(3), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test for values outside the domain
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0001);
        
        // Test for values inside the domain
        assertEquals(0.4286, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.7143, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(0.5714, distribution.innerCumulativeProbability(1, 2, 1), 0.0001);
        assertEquals(0.8571, distribution.innerCumulativeProbability(2, 2, 1), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(0.6, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(0.1714, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(2, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertTrue(distribution.isSupportConnected());
    }
}
import org.junit.*;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        // Test for x < lower domain
        assertEquals(0.0, hd.cumulativeProbability(0), 0.0001);
        
        // Test for x >= upper domain
        assertEquals(1.0, hd.cumulativeProbability(6), 0.0001);
        
        // Test for x within domain
        assertEquals(0.4286, hd.cumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        int[] domain = hd.getDomain(10, 5, 3);
        
        // Test lower bound
        assertEquals(2, domain[0]);
        
        // Test upper bound
        assertEquals(3, domain[1]);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        int lowerDomain = hd.getLowerDomain(10, 5, 3);
        
        assertEquals(2, lowerDomain);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        int numberOfSuccesses = hd.getNumberOfSuccesses();
        
        assertEquals(5, numberOfSuccesses);
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        int populationSize = hd.getPopulationSize();
        
        assertEquals(10, populationSize);
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        int sampleSize = hd.getSampleSize();
        
        assertEquals(3, sampleSize);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        // Test for x < lower domain
        assertEquals(0.0, hd.probability(0), 0.0001);
        
        // Test for x > upper domain
        assertEquals(0.0, hd.probability(6), 0.0001);
        
        // Test for x within domain
        assertEquals(0.2381, hd.probability(3), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        // Test for x <= lower domain
        assertEquals(1.0, hd.upperCumulativeProbability(0), 0.0001);
        
        // Test for x > upper domain
        assertEquals(0.0, hd.upperCumulativeProbability(6), 0.0001);
        
        // Test for x within domain
        assertEquals(0.5714, hd.upperCumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        double numericalMean = hd.getNumericalMean();
        
        assertEquals(1.5, numericalMean, 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        double numericalVariance = hd.getNumericalVariance();
        
        assertEquals(0.6429, numericalVariance, 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        int supportLowerBound = hd.getSupportLowerBound();
        
        assertEquals(2, supportLowerBound);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        int supportUpperBound = hd.getSupportUpperBound();
        
        assertEquals(3, supportUpperBound);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        
        boolean supportConnected = hd.isSupportConnected();
        
        assertEquals(true, supportConnected);
    }
    
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        double result = dist.cumulativeProbability(0);
        assertEquals(0.0, result, 0.0001);
        
        result = dist.cumulativeProbability(3);
        assertEquals(0.61667, result, 0.0001);
        
        result = dist.cumulativeProbability(5);
        assertEquals(1.0, result, 0.0001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        int[] domain = dist.getDomain(10, 3, 5);
        assertEquals(0, domain[0]);
        assertEquals(3, domain[1]);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        int lowerDomain = dist.getLowerDomain(10, 3, 5);
        assertEquals(0, lowerDomain);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        int numberOfSuccesses = dist.getNumberOfSuccesses();
        assertEquals(3, numberOfSuccesses);
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        int populationSize = dist.getPopulationSize();
        assertEquals(10, populationSize);
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        int sampleSize = dist.getSampleSize();
        assertEquals(5, sampleSize);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        double result = dist.probability(0);
        assertEquals(0.08333, result, 0.0001);
        
        result = dist.probability(3);
        assertEquals(0.33333, result, 0.0001);
        
        result = dist.probability(5);
        assertEquals(0.08333, result, 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        double result = dist.upperCumulativeProbability(0);
        assertEquals(1.0, result, 0.0001);
        
        result = dist.upperCumulativeProbability(3);
        assertEquals(0.38333, result, 0.0001);
        
        result = dist.upperCumulativeProbability(5);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        double result = dist.innerCumulativeProbability(0, 3, 1);
        assertEquals(0.83, result, 0.0001);
        
        result = dist.innerCumulativeProbability(3, 0, -1);
        assertEquals(0.83, result, 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        double result = dist.getNumericalMean();
        assertEquals(1.5, result, 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        double result = dist.getNumericalVariance();
        assertEquals(0.42857, result, 0.0001);
    }
    
    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        double result = dist.calculateNumericalVariance();
        assertEquals(0.42857, result, 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        int result = dist.getSupportLowerBound();
        assertEquals(0, result);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        int result = dist.getSupportUpperBound();
        assertEquals(3, result);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        
        boolean result = dist.isSupportConnected();
        assertTrue(result);
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        // Cumulative probability for x = 0
        double result1 = dist.cumulativeProbability(0);
        assertEquals(0.0, result1, 0.0001);
        
        // Cumulative probability for x = 1
        double result2 = dist.cumulativeProbability(1);
        assertEquals(0.1667, result2, 0.0001);
        
        // Cumulative probability for x = 2
        double result3 = dist.cumulativeProbability(2);
        assertEquals(0.6667, result3, 0.0001);
        
        // Cumulative probability for x = 3
        double result4 = dist.cumulativeProbability(3);
        assertEquals(1.0, result4, 0.0001);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int result = dist.getNumberOfSuccesses();
        assertEquals(5, result);
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int result = dist.getPopulationSize();
        assertEquals(10, result);
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int result = dist.getSampleSize();
        assertEquals(3, result);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        // Probability for x = 0
        double result1 = dist.probability(0);
        assertEquals(0.1429, result1, 0.0001);
        
        // Probability for x = 1
        double result2 = dist.probability(1);
        assertEquals(0.4286, result2, 0.0001);
        
        // Probability for x = 2
        double result3 = dist.probability(2);
        assertEquals(0.4286, result3, 0.0001);
        
        // Probability for x = 3
        double result4 = dist.probability(3);
        assertEquals(0.0, result4, 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        // Upper cumulative probability for x = 0
        double result1 = dist.upperCumulativeProbability(0);
        assertEquals(1.0, result1, 0.0001);
        
        // Upper cumulative probability for x = 1
        double result2 = dist.upperCumulativeProbability(1);
        assertEquals(0.8333, result2, 0.0001);
        
        // Upper cumulative probability for x = 2
        double result3 = dist.upperCumulativeProbability(2);
        assertEquals(0.1667, result3, 0.0001);
        
        // Upper cumulative probability for x = 3
        double result4 = dist.upperCumulativeProbability(3);
        assertEquals(0.0, result4, 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.getNumericalMean();
        assertEquals(1.5, result, 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.getNumericalVariance();
        assertEquals(0.4286, result, 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int result = dist.getSupportLowerBound();
        assertEquals(0, result);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int result = dist.getSupportUpperBound();
        assertEquals(3, result);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        boolean result = dist.isSupportConnected();
        assertTrue(result);
    }
}
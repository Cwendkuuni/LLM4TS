import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructorWithValidParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(distribution);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativePopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-1, 5, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -1, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 30);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.375, distribution.getNumericalVariance(), 0.001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.357, distribution.probability(0), 0.001);
        assertEquals(0.429, distribution.probability(1), 0.001);
        assertEquals(0.214, distribution.probability(2), 0.001);
        assertEquals(0.0, distribution.probability(4), 0.001);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.357, distribution.cumulativeProbability(0), 0.001);
        assertEquals(0.786, distribution.cumulativeProbability(1), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.001);
        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.001);
        assertEquals(0.643, distribution.upperCumulativeProbability(2), 0.001);
        assertEquals(0.214, distribution.upperCumulativeProbability(4), 0.001);
    }

}
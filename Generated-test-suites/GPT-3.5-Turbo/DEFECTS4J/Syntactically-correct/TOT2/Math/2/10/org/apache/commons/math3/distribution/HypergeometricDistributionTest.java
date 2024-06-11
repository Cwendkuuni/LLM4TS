import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        // Constructing a valid HypergeometricDistribution object
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(hypergeometric);
    }
    
    @Test
    public void testProbability() {
        // Testing probability for valid input
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.5714, hypergeometric.probability(2), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        // Testing cumulativeProbability for valid input
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.8571, hypergeometric.cumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        // Testing upperCumulativeProbability for valid input
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.1428, hypergeometric.upperCumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        // Testing getNumericalMean
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hypergeometric.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        // Testing getNumericalVariance
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6428, hypergeometric.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        // Testing getSupportLowerBound
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, hypergeometric.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        // Testing getSupportUpperBound
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, hypergeometric.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        // Testing isSupportConnected
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertTrue(hypergeometric.isSupportConnected());
    }

}
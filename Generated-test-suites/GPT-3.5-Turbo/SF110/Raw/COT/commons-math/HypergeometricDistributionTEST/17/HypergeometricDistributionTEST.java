import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(0.611, distribution.cumulativeProbability(1), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.0, distribution.cumulativeProbability(3), 0.001);
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
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(0.222, distribution.probability(1), 0.001);
        assertEquals(0.222, distribution.probability(2), 0.001);
        assertEquals(0.0, distribution.probability(3), 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.001);
        assertEquals(0.389, distribution.upperCumulativeProbability(2), 0.001);
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(0.6, distribution.getNumericalMean(), 0.001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(0.48, distribution.getNumericalVariance(), 0.001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        assertEquals(1, distribution.getSupportLowerBound());
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

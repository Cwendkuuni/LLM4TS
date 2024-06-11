
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;
    
    @Before
    public void setup() {
        distribution = new HypergeometricDistribution(100, 10, 5);
    }
    
    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0151515, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.487112, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.947846, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(10, distribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(100, distribution.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        assertEquals(5, distribution.getSampleSize());
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.145455, distribution.probability(0), 0.0001);
        assertEquals(0.496165, distribution.probability(2), 0.0001);
        assertEquals(0.0, distribution.probability(6), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.515152, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0848484, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(5.0, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.6666666, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(5, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(5, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

}

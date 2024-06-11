import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
    private HypergeometricDistribution hypergeometricDistribution;
    
    @Before
    public void setUp() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(5, hypergeometricDistribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(10, hypergeometricDistribution.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        assertEquals(2, hypergeometricDistribution.getSampleSize());
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(2, hypergeometricDistribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(1.0, hypergeometricDistribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.8888888889, hypergeometricDistribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        assertEquals(0.2, hypergeometricDistribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.4, hypergeometricDistribution.cumulativeProbability(1), 0.0001);
        assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.8, hypergeometricDistribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.2, hypergeometricDistribution.probability(0), 0.0001);
        assertEquals(0.4, hypergeometricDistribution.probability(1), 0.0001);
        assertEquals(0.4, hypergeometricDistribution.probability(2), 0.0001);
    }
}
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(3, distribution.getSampleSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativePopulationSize() {
        new HypergeometricDistribution(0, 5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testNegativeSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 15);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0);
        assertEquals(0.156, distribution.cumulativeProbability(1), 0.001);
        assertEquals(0.51, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.84, distribution.cumulativeProbability(3), 0.001);
        assertEquals(0.984, distribution.cumulativeProbability(4), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.0);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.probability(-1), 0.0);
        assertEquals(0.0, distribution.probability(0), 0.0);
        assertEquals(0.156, distribution.probability(1), 0.001);
        assertEquals(0.351, distribution.probability(2), 0.001);
        assertEquals(0.336, distribution.probability(3), 0.001);
        assertEquals(0.140, distribution.probability(4), 0.001);
        assertEquals(0.016, distribution.probability(5), 0.001);
        assertEquals(0.0, distribution.probability(6), 0.0);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0);
        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.0);
        assertEquals(0.844, distribution.upperCumulativeProbability(2), 0.001);
        assertEquals(0.490, distribution.upperCumulativeProbability(3), 0.001);
        assertEquals(0.159, distribution.upperCumulativeProbability(4), 0.001);
        assertEquals(0.016, distribution.upperCumulativeProbability(5), 0.001);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0);
    }
    
    @Test
    public void testNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0);
    }
    
    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.450, distribution.getNumericalVariance(), 0.001);
    }
    
    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(true, distribution.isSupportConnected());
    }
    
}
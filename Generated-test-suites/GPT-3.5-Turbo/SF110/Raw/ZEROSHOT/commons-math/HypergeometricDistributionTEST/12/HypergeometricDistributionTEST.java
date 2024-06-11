import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativePopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 12, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 12);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.0001);
        assertEquals(0.4444, dist.cumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.probability(-1), 0.0001);
        assertEquals(0.0, dist.probability(6), 0.0001);
        assertEquals(0.0476, dist.probability(3), 0.0001);
        assertEquals(0.0, dist.probability(7), 0.0001);
        assertEquals(0.0, dist.probability(12), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.5556, dist.upperCumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.28125, dist.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, dist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
}
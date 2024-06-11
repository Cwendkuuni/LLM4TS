import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(dist);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 30);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, dist.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSampleSize());
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.642857, dist.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, dist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.3571428571, dist.probability(3), 0.0001);
        assertEquals(0.4285714286, dist.probability(4), 0.0001);
        assertEquals(0.2142857143, dist.probability(5), 0.0001);
        assertEquals(0, dist.probability(2), 0.0001);
        assertEquals(0, dist.probability(6), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.3571428571, dist.cumulativeProbability(3), 0.0001);
        assertEquals(0.7857142857, dist.cumulativeProbability(4), 0.0001);
        assertEquals(1, dist.cumulativeProbability(5), 0.0001);
        assertEquals(0, dist.cumulativeProbability(2), 0.0001);
        assertEquals(0, dist.cumulativeProbability(6), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.6428571429, dist.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.2142857143, dist.upperCumulativeProbability(5), 0.0001);
        assertEquals(1, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(1, dist.upperCumulativeProbability(6), 0.0001);
    }
}
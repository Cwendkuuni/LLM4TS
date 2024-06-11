import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(dist);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NumOfSuccessesNotPositive() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_SampleSizeNotPositive() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_PopulationSizeNotPositive() {
        HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 15, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 13);
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
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.4167, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.8333, dist.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.0, dist.probability(-1), 0.0001);
        assertEquals(0.1, dist.probability(0), 0.0001);
        assertEquals(0.3, dist.probability(1), 0.0001);
        assertEquals(0.4, dist.probability(2), 0.0001);
        assertEquals(0.2, dist.probability(3), 0.0001);
        assertEquals(0.0, dist.probability(4), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.5833, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.1667, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.0001);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    // Test for the constructor
    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(dist);
    }

    // Test for getNumberOfSuccesses()
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    // Test for getPopulationSize()
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
    }

    // Test for getSampleSize()
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSampleSize());
    }

    // Test for cumulativeProbability(int x)
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0714, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.0714, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.5714, dist.cumulativeProbability(2), 0.0001);
        assertEquals(0.5714, dist.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(10), 0.0001);
    }

    // Test for probability(int x)
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.probability(-1), 0.0001);
        assertEquals(0.0714, dist.probability(0), 0.0001);
        assertEquals(0.2381, dist.probability(1), 0.0001);
        assertEquals(0.4762, dist.probability(2), 0.0001);
        assertEquals(0.4762, dist.probability(3), 0.0001);
        assertEquals(0.2381, dist.probability(4), 0.0001);
        assertEquals(0.0714, dist.probability(5), 0.0001);
        assertEquals(0.0, dist.probability(10), 0.0001);
    }

    // Test for upperCumulativeProbability(int x)
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.9286, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.2857, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.2143, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(10), 0.0001);
    }

    // Test for getNumericalMean()
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }

    // Test for getNumericalVariance()
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.8929, dist.getNumericalVariance(), 0.0001);
    }

    // Test for getSupportLowerBound()
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, dist.getSupportLowerBound());
    }

    // Test for getSupportUpperBound()
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportUpperBound());
    }

    // Test for isSupportConnected()
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
}
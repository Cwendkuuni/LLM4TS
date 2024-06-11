import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(2, distribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.36363636363636365, distribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.4545454545454545, distribution.probability(1), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.6363636363636364, distribution.upperCumulativeProbability(1), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.18181818181818182, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(2, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertTrue(distribution.isSupportConnected());
    }
}
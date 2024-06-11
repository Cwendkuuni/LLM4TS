import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(0.03333333333333333, distribution.cumulativeProbability(0), 0.000001);
        assertEquals(0.9333333333333333, distribution.cumulativeProbability(1), 0.000001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.000001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.000001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.000001);
        assertEquals(0.9666666666666667, distribution.cumulativeProbability(5), 0.000001);
        assertEquals(0.1, distribution.cumulativeProbability(6), 0.000001);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(4, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(3, distribution.getSampleSize());
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(0.4, distribution.probability(0), 0.000001);
        assertEquals(0.4, distribution.probability(1), 0.000001);
        assertEquals(0.2, distribution.probability(2), 0.000001);
        assertEquals(0.0, distribution.probability(3), 0.000001);
        assertEquals(0.0, distribution.probability(4), 0.000001);
        assertEquals(0.0, distribution.probability(5), 0.000001);
        assertEquals(0.0, distribution.probability(6), 0.000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.000001);
        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.000001);
        assertEquals(0.9666666666666667, distribution.upperCumulativeProbability(2), 0.000001);
        assertEquals(0.03333333333333333, distribution.upperCumulativeProbability(3), 0.000001);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.000001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.000001);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(1.2, distribution.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(0.48, distribution.getNumericalVariance(), 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 3);
        assertTrue(distribution.isSupportConnected());
    }
}
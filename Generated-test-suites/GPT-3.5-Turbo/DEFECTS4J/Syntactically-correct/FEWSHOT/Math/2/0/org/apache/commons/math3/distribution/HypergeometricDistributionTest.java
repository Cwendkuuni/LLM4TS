import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.0142857, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.1714286, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.5571429, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.9428571, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.0285714, distribution.probability(0), 0.0001);
        assertEquals(0.2285714, distribution.probability(1), 0.0001);
        assertEquals(0.4714286, distribution.probability(2), 0.0001);
        assertEquals(0.2285714, distribution.probability(3), 0.0001);
        assertEquals(0.0428571, distribution.probability(4), 0.0001);
        assertEquals(0.0, distribution.probability(5), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.9857143, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.8142857, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.2428571, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0571429, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.87778, distribution.getNumericalVariance(), 0.0001);
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
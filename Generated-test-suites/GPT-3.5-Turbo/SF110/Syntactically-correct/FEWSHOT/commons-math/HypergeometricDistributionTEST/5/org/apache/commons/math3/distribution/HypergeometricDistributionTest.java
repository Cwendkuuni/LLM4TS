import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.1333, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.6666, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.8666, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.9333, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.2, distribution.probability(0), 0.0001);
        assertEquals(0.4, distribution.probability(1), 0.0001);
        assertEquals(0.4, distribution.probability(2), 0.0001);
        assertEquals(0.0, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
        assertEquals(0.0, distribution.probability(5), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.8666, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.3333, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.1333, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0666, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0333, distribution.upperCumulativeProbability(5), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.8571, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(true, distribution.isSupportConnected());
    }
}
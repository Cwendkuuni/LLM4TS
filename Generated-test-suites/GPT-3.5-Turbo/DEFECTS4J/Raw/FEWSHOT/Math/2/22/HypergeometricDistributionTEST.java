import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.4444, dist.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.probability(0), 0.0001);
        assertEquals(0.3333, dist.probability(1), 0.0001);
        assertEquals(0.5556, dist.probability(2), 0.0001);
        assertEquals(0.1111, dist.probability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.5556, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.4286, dist.getNumericalVariance(), 0.0001);
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
        assertEquals(true, dist.isSupportConnected());
    }
}
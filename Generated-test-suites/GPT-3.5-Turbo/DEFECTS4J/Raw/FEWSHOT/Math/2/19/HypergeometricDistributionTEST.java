import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        assertEquals(0.4395238095238095, dist.cumulativeProbability(3), 0.00001);
        assertEquals(0.5714285714285714, dist.cumulativeProbability(5), 0.00001);
        assertEquals(1.0, dist.cumulativeProbability(10), 0.00001);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.00001);
        assertEquals(0.0, dist.cumulativeProbability(11), 0.00001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        assertEquals(0.07088607595, dist.probability(3), 0.00001);
        assertEquals(0.2222222222, dist.probability(5), 0.00001);
        assertEquals(0.0, dist.probability(-1), 0.00001);
        assertEquals(0.0, dist.probability(11), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        assertEquals(1.0, dist.upperCumulativeProbability(3), 0.00001);
        assertEquals(0.5928571429, dist.upperCumulativeProbability(5), 0.00001);
        assertEquals(0.0, dist.upperCumulativeProbability(10), 0.00001);
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.00001);
        assertEquals(1.0, dist.upperCumulativeProbability(11), 0.00001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        assertEquals(2.5, dist.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        assertEquals(1.2105263158, dist.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        assertEquals(5, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        assertEquals(true, dist.isSupportConnected());
    }
}
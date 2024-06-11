import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 20, 10);
        assertEquals(100, dist.getPopulationSize());
        assertEquals(20, dist.getNumberOfSuccesses());
        assertEquals(10, dist.getSampleSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorZeroPopulationSize() {
        new HypergeometricDistribution(0, 20, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(100, -1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeSampleSize() {
        new HypergeometricDistribution(100, 20, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(100, 200, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(100, 20, 200);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 20, 10);
        assertEquals(2.0, dist.getNumericalMean(), 1e-12);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 20, 10);
        assertEquals(1.8979591836734695, dist.getNumericalVariance(), 1e-12);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 20, 10);
        assertEquals(0.09368358922099618, dist.cumulativeProbability(1), 1e-12);
        assertEquals(0.6494075938916617, dist.cumulativeProbability(5), 1e-12);
        assertEquals(0.9987825876443621, dist.cumulativeProbability(10), 1e-12);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 20, 10);
        assertEquals(0.0937217864835595, dist.probability(1), 1e-12);
        assertEquals(0.22656641604010024, dist.probability(5), 1e-12);
        assertEquals(0.07767644727064914, dist.probability(10), 1e-12);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 20, 10);
        assertEquals(0.9999999999998871, dist.upperCumulativeProbability(1), 1e-12);
        assertEquals(0.9752492080003851, dist.upperCumulativeProbability(5), 1e-12);
        assertEquals(0.4062523627168939, dist.upperCumulativeProbability(10), 1e-12);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 20, 10);
        assertTrue(dist.isSupportConnected());
    }
}
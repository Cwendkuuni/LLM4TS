import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.000, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.091, dist.cumulativeProbability(1), 0.001);
        assertEquals(0.364, dist.cumulativeProbability(2), 0.001);
        assertEquals(0.727, dist.cumulativeProbability(3), 0.001);
        assertEquals(1.000, dist.cumulativeProbability(4), 0.001);
        assertEquals(1.000, dist.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.083, dist.probability(0), 0.001);
        assertEquals(0.417, dist.probability(1), 0.001);
        assertEquals(0.500, dist.probability(2), 0.001);
        assertEquals(0.083, dist.probability(3), 0.001);
        assertEquals(0.000, dist.probability(4), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.000, dist.upperCumulativeProbability(0), 0.001);
        assertEquals(0.909, dist.upperCumulativeProbability(1), 0.001);
        assertEquals(0.818, dist.upperCumulativeProbability(2), 0.001);
        assertEquals(0.455, dist.upperCumulativeProbability(3), 0.001);
        assertEquals(0.000, dist.upperCumulativeProbability(4), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.500, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.750, dist.getNumericalVariance(), 0.001);
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
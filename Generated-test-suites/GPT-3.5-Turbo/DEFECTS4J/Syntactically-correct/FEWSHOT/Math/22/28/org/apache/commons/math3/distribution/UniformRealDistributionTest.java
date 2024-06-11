import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertEquals(0.1, dist.density(5), 0);
        assertEquals(0.0, dist.density(15), 0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertEquals(0.5, dist.cumulativeProbability(5), 0);
        assertEquals(1.0, dist.cumulativeProbability(15), 0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertEquals(5, dist.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertEquals(8.333333333333334, dist.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertEquals(0, dist.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertEquals(10, dist.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertTrue(!dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        double sample = dist.sample();
        assertTrue(sample >= 0 && sample < 10);
    }
}
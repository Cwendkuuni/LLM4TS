import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0.1, distribution.density(5), 0.0001);
        assertEquals(0.0, distribution.density(15), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0.5, distribution.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(15), 0.0001);
    }

    @Test
    public void testSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(5.0, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(8.3333, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(10.0, distribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 10);
    }
}
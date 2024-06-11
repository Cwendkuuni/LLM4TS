import org.junit.Test;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Test
    public void testDensity() {
        distribution = new UniformRealDistribution(0, 1);
        double density = distribution.density(0.5);
        assertEquals(1, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        distribution = new UniformRealDistribution(0, 1);
        double cumulativeProbability = distribution.cumulativeProbability(0.5);
        assertEquals(0.5, cumulativeProbability, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        distribution = new UniformRealDistribution(0, 1);
        double numericalMean = distribution.getNumericalMean();
        assertEquals(0.5, numericalMean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        distribution = new UniformRealDistribution(0, 1);
        double numericalVariance = distribution.getNumericalVariance();
        assertEquals(0.0833, numericalVariance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        distribution = new UniformRealDistribution(0, 1);
        double supportLowerBound = distribution.getSupportLowerBound();
        assertEquals(0, supportLowerBound, 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        distribution = new UniformRealDistribution(0, 1);
        double supportUpperBound = distribution.getSupportUpperBound();
        assertEquals(1, supportUpperBound, 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        distribution = new UniformRealDistribution(0, 1);
        boolean supportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        assertTrue(supportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        distribution = new UniformRealDistribution(0, 1);
        boolean supportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        assertFalse(supportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        distribution = new UniformRealDistribution(0, 1);
        boolean supportConnected = distribution.isSupportConnected();
        assertTrue(supportConnected);
    }

    @Test
    public void testSample() {
        distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 1);
    }
}
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class UniformRealDistributionTest {
    // create instance of UniformRealDistribution
    private UniformRealDistribution uniformRealDistribution = new UniformRealDistribution();

    @Test
    public void testDensity() {
        // test for value lower than lower bound
        assertEquals(0.0, uniformRealDistribution.density(-1.0), 0.0);

        // test for value higher than upper bound
        assertEquals(0.0, uniformRealDistribution.density(2.0), 0.0);

        // test for value within range
        assertEquals(0.5, uniformRealDistribution.density(0.5), 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        // test for value lower than lower bound
        assertEquals(0.0, uniformRealDistribution.cumulativeProbability(-1.0), 0.0);

        // test for value higher than upper bound
        assertEquals(1.0, uniformRealDistribution.cumulativeProbability(2.0), 0.0);

        // test for value within range
        assertEquals(0.5, uniformRealDistribution.cumulativeProbability(0.5), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        // test for default bounds (0 and 1)
        assertEquals(0.5, uniformRealDistribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        // test for default bounds (0 and 1)
        assertEquals(0.08333333333333334, uniformRealDistribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        // test for default lower bound (0)
        assertEquals(0.0, uniformRealDistribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        // test for default upper bound (1)
        assertEquals(1.0, uniformRealDistribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        // the lower bound is inclusive
        assertEquals(true, uniformRealDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        // the upper bound is not inclusive
        assertEquals(false, uniformRealDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        // the support is connected
        assertEquals(true, uniformRealDistribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        // sample the distribution 1000 times and check if all values are within bounds
        for (int i = 0; i < 1000; i++) {
            double value = uniformRealDistribution.sample();
            assertEquals(true, value >= 0.0 && value < 1.0);
        }
    }
}
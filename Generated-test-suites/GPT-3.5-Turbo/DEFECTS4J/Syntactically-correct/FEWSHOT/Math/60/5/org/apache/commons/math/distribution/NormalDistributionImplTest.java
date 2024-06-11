package org.apache.commons.math.distribution;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {
    private final double DELTA = 1e-9;
    private NormalDistributionImpl normalDistribution = new NormalDistributionImpl();

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), DELTA);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), DELTA);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), DELTA);
        assertEquals(0.24197072451914337, normalDistribution.density(-1), DELTA);
        assertEquals(0.24197072451914337, normalDistribution.density(1), DELTA);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0, normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY), DELTA);
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), DELTA);
        assertEquals(1, normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), DELTA);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), DELTA);
    }
    
    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        assertEquals(0, normalDistribution.cumulativeProbability(sample), DELTA);
    }
}
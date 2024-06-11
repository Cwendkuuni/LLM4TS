package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NormalDistributionImplTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(3, 2);
        assertEquals(3, distribution.getMean(), EPSILON);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(3, 2);
        assertEquals(2, distribution.getStandardDeviation(), EPSILON);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), EPSILON);
        assertEquals(0.24197072451914337, distribution.density(1), EPSILON);
        assertEquals(0.05399096651318806, distribution.density(2), EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.8413447460685429, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(0.9772498680518208, distribution.cumulativeProbability(2), EPSILON);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1.2815515655446004, distribution.inverseCumulativeProbability(0.1), EPSILON);
        assertEquals(1.2815515655446004, distribution.inverseCumulativeProbability(0.9), EPSILON);
        assertEquals(-1.959963984540054, distribution.inverseCumulativeProbability(0.025), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
}
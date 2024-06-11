package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NormalDistributionImplTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), EPSILON);

        distribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution.getMean(), EPSILON);

        distribution = new NormalDistributionImpl(-10, 0.5);
        assertEquals(-10, distribution.getMean(), EPSILON);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1, distribution.getStandardDeviation(), EPSILON);

        distribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, distribution.getStandardDeviation(), EPSILON);

        distribution = new NormalDistributionImpl(-10, 0.5);
        assertEquals(0.5, distribution.getStandardDeviation(), EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testCreateInvalidDistribution() {
        new NormalDistributionImpl(0, 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), EPSILON);
        assertEquals(0.24197072451914337, distribution.density(-1), EPSILON);
        assertEquals(0.24197072451914337, distribution.density(1), EPSILON);
        assertEquals(0.05399096651318806, distribution.density(-2), EPSILON);
        assertEquals(0.05399096651318806, distribution.density(2), EPSILON);

        distribution = new NormalDistributionImpl(3, 2);
        assertEquals(0.12951759566589338, distribution.density(3), EPSILON);
        assertEquals(0.05399096651318806, distribution.density(4), EPSILON);
        assertEquals(0.05399096651318806, distribution.density(2), EPSILON);
        assertEquals(0.0029150244650281948, distribution.density(7), EPSILON);
        assertEquals(0.0029150244650281948, distribution.density(-1), EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.022750131948179095, distribution.cumulativeProbability(-2), EPSILON);
        assertEquals(0.9772498680518209, distribution.cumulativeProbability(2), EPSILON);
        assertEquals(0.0013498980316300933, distribution.cumulativeProbability(-3), EPSILON);
        assertEquals(0.9986501019683699, distribution.cumulativeProbability(3), EPSILON);

        distribution = new NormalDistributionImpl(3, 2);
        assertEquals(0.5, distribution.cumulativeProbability(3), EPSILON);
        assertEquals(0.15865525393145707, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(0.9772498680518209, distribution.cumulativeProbability(7), EPSILON);
        assertEquals(0.0013498980316300933, distribution.cumulativeProbability(-1), EPSILON);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-12.7062047361748, distribution.inverseCumulativeProbability(0.00000001), EPSILON);
        assertEquals(-1.9599639845400545, distribution.inverseCumulativeProbability(0.025), EPSILON);
        assertEquals(0, distribution.inverseCumulativeProbability(0.5), EPSILON);
        assertEquals(1.9599639845400545, distribution.inverseCumulativeProbability(0.975), EPSILON);
        assertEquals(12.7062047361748, distribution.inverseCumulativeProbability(0.99999999), EPSILON);

        distribution = new NormalDistributionImpl(3, 2);
        assertEquals(-11.41254267827839, distribution.inverseCumulativeProbability(0.00000001), EPSILON);
        assertEquals(-0.08161744166662183, distribution.inverseCumulativeProbability(0.025), EPSILON);
        assertEquals(3, distribution.inverseCumulativeProbability(0.5), EPSILON);
        assertEquals(6.081617441666621, distribution.inverseCumulativeProbability(0.975), EPSILON);
        assertEquals(15.412542678278388, distribution.inverseCumulativeProbability(0.99999999), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 10000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        }
    }
}
package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_mean0stdDev1() {
        NormalDistribution normalDist = new NormalDistributionImpl();

        assertEquals(0.0, normalDist.getMean(), 0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0);

        assertNotNull(normalDist.sample());
    }

    @Test
    public void testNormalDistributionImpl_mean1stdDev1() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.0, 1.0);

        assertEquals(1.0, normalDist.getMean(), 0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0);

        assertNotNull(normalDist.sample());
    }

    @Test
    public void testNormalDistributionImpl_mean2stdDev1() {
        NormalDistribution normalDist = new NormalDistributionImpl(2.0, 1.0);

        assertEquals(2.0, normalDist.getMean(), 0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0);

        assertNotNull(normalDist.sample());
    }

    @Test
    public void testDensity() throws Exception {
        NormalDistribution normalDist = new NormalDistributionImpl(1.0, 1.0);

        for (double x = -10.0; x <= 10.0; x += 0.1) {
            double density = normalDist.density(x);
            double expectedValue = FastMath.exp(-0.5 * Math.pow((x - 1.0) / 1.0, 2)) / (1.0 * Math.sqrt(2 * Math.PI));
            assertEquals(expectedValue, density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistribution normalDist = new NormalDistributionImpl(1.0, 1.0);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double cumulativeProbability = normalDist.cumulativeProbability(x);
            double expectedValue = 0.5 + 0.5 * Erf.erf((x - 1.0) / (Math.sqrt(2.0) * 1.0));
            assertEquals(expectedValue, cumulativeProbability, 0.001);
        }

        try {
            normalDist.cumulativeProbability(Double.NEGATIVE_INFINITY);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            normalDist.cumulativeProbability(Double.POSITIVE_INFINITY);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        NormalDistribution normalDist = new NormalDistributionImpl(1.0, 1.0);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double inverseCumulativeProbability = normalDist.inverseCumulativeProbability(p);
            double expectedValue = normalDist.getMean() + p * normalDist.getStandardDeviation();
            assertEquals(expectedValue, inverseCumulativeProbability, 0.001);
        }

        try {
            normalDist.inverseCumulativeProbability(0.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            normalDist.inverseCumulativeProbability(1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testSample() throws Exception {
        NormalDistribution normalDist = new NormalDistributionImpl(1.0, 1.0);

        for (int i = 0; i < 10000; i++) {
            double sample = normalDist.sample();
            assertTrue(sample >= -10.0 && sample <= 10.0);
        }
    }
}
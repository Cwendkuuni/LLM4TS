package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("Mean not set correctly", 0.0, dist.getMean(), 1e-10);
        assertEquals("Standard Deviation not set correctly", 1.0, dist.getStandardDeviation(), 1e-10);
    }

    /**
     * Test constructor with invalid argument.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidStandardDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    /**
     * Test method {@link NormalDistributionImpl#density(double)}.
     */
    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double density = dist.density(1.0);
        assertEquals("Density not calculated correctly", 0.398942280401, density, 1e-10);
        density = dist.density(-1.0);
        assertEquals("Density not calculated correctly for negative x", 0.398942280401, density, 1e-10);
    }

    /**
     * Test method {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = dist.cumulativeProbability(0.5);
        assertEquals("CDF not calculated correctly for p = 0.5", 0.682689462137, p, 1e-10);
        p = dist.cumulativeProbability(-3.0);
        assertEquals("CDF not calculated correctly for x < mean", 0.0, p, 1e-10);
        p = dist.cumulativeProbability(3.0);
        assertEquals("CDF not calculated correctly for x > mean", 1.0, p, 1e-10);
    }

    /**
     * Test method {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.1;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals("Inverse CDF not calculated correctly for p = 0.1", -0.842685449844, x, 1e-10);
        x = dist.inverseCumulativeProbability(0.99999);
        assertEquals("Inverse CDF not calculated correctly for p close to 1", 3.04435495256, x, 1e-10);
    }

    /**
     * Test method {@link NormalDistributionImpl#sample()}
     */
    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = dist.sample();
        assertTrue("Sample not within expected range", x >= -3.5 && x <= 3.5);
        x = dist.sample();
        assertTrue("Sample not within expected range", x >= -3.5 && x <= 3.5);
    }
}
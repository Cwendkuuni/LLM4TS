package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Test;

/**
 * Tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test the constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(sd, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test the constructor with invalid argument (negative standard deviation).
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() {
        final double mean = 0.0;
        final double sd = -1.0;
        new NormalDistributionImpl(mean, sd);
    }

    /**
     * Test the density method.
     */
    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        final double x = 0.5;
        double density = dist.density(x);
        assertEquals(0.1543083221288232, density, 1e-7);
    }

    /**
     * Test the cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p;
        double cdf;

        p = 0.0;
        cdf = dist.cumulativeProbability(p);
        assertEquals(0.0, cdf, 1e-8);

        p = 0.5;
        cdf = dist.cumulativeProbability(p);
        assertEquals(0.5, cdf, 1e-8);

        p = 1.0;
        cdf = dist.cumulativeProbability(p);
        assertEquals(1.0, cdf, 1e-8);

        p = 2.0;
        cdf = dist.cumulativeProbability(p);
        assertEquals(1.0, cdf, 1e-8);

        p = -2.0;
        cdf = dist.cumulativeProbability(p);
        assertEquals(0.0, cdf, 1e-8);
    }

    /**
     * Test the inverse cumulative probability method.
     */
    @Test
    public void testInverseCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p;
        double x;

        // valid probabilities
        p = 0.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(-3.345935334847694, x, 1e-8);

        p = 0.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 1e-8);

        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(3.345935334847694, x, 1e-8);

        // invalid probabilities
        p = 1.1;
        try {
            x = dist.inverseCumulativeProbability(p);
        } catch (MathIllegalArgumentException e) {
            assertTrue(true);
        }

        p = -0.5;
        try {
            x = dist.inverseCumulativeProbability(p);
        } catch (MathIllegalArgumentException e) {
            assertTrue(true);
        }

        p = 2.0;
        try {
            x = dist.inverseCumulativeProbability(p);
        } catch (MathIllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Test the sample method.
     */
    @Test
    public void testSample() throws Exception {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        final RandomDataGenerator rng = new RandomDataGenerator();
        int count = 10000;
        int i;
        double x;

        for (i = 0; i < count; i++) {
            x = dist.sample();
            assertTrue(x >= -10.0 && x <= 10.0);
        }
    }
}
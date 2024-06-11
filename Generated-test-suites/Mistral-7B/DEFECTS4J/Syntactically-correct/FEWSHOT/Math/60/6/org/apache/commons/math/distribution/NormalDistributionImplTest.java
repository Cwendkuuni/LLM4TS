package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RoundingMode;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testDefaultConstructor() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0, dist.getMean(), 0);
        assertEquals(1, dist.getStandardDeviation(), 0);
    }

    @Test
    public void testMeanAndStandardDeviationConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(1, 2);
        assertEquals(1, dist.getMean(), 0);
        assertEquals(2, dist.getStandardDeviation(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviationConstructor() {
        new NormalDistributionImpl(1, -1);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 0.5;
        double expected = 0.12533141364234;
        assertEquals(expected, dist.density(x), 1e-8);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 0;
        double expected = 0.5;
        assertEquals(expected, dist.cumulativeProbability(x), 1e-8);

        x = 1;
        expected = 0.841344746;
        assertEquals(expected, dist.cumulativeProbability(x), 1e-8);

        x = 2;
        expected = 0.977249868;
        assertEquals(expected, dist.cumulativeProbability(x), 1e-8);

        x = 3;
        expected = 0.997310582;
        assertEquals(expected, dist.cumulativeProbability(x), 1e-8);

        x = 4;
        expected = 0.999999999;
        assertEquals(expected, dist.cumulativeProbability(x), 1e-8);

        x = 5;
        assertNull(dist.cumulativeProbability(x));
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityFailedConvergence() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 1000;
        dist.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.1587;
        double expected = -0.679465978;
        assertEquals(expected, dist.inverseCumulativeProbability(p), 1e-8);

        p = 0.8413;
        expected = 0.842700915;
        assertEquals(expected, dist.inverseCumulativeProbability(p), 1e-8);

        p = 0.999;
        expected = 3.09355949;
        assertEquals(expected, dist.inverseCumulativeProbability(p), 1e-8);

        p = 1;
        assertNull(dist.inverseCumulativeProbability(p));
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Random random = new Random();
        double x = dist.sample();
        double expected = Math.sqrt(-2.0 * FastMath.log(random.nextDouble()))
                * FastMath.cos(2.0 * FastMath.PI * random.nextDouble())
                + dist.getMean();
        assertEquals(expected, x, 1e-8);
    }
}
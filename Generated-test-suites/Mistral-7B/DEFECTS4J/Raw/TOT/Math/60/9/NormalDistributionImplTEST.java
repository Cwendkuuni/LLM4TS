package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomDataGenerator;

import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @org.junit.Test
    public void testValidConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(5, 2);
        assertEquals(5, dist.getMean(), 0.0);
        assertEquals(2, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with invalid argument (negative standard deviation).
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructorNegativeStdDev() {
        new NormalDistributionImpl(-1, 2);
    }

    /**
     * Test method {@link NormalDistributionImpl#density(double)}.
     */
    @org.junit.Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double density = dist.density(0);
        assertEquals(0.398942280401433, density, 0.000001);

        density = dist.density(3);
        assertEquals(0.134934157363354, density, 0.000001);

        density = dist.density(-3);
        assertEquals(0.134934157363354, density, 0.000001);
    }

    /**
     * Test method {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @org.junit.Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double p = 0.25;
        double result = dist.cumulativeProbability(0);
        assertEquals(0, result, 0.0);

        result = dist.cumulativeProbability(1);
        assertEquals(0.841344746043649, result, 0.0001);

        result = dist.cumulativeProbability(p);
        assertEquals(p, result, 0.0001);

        result = dist.cumulativeProbability(-1);
        assertEquals(0, result, 0.0);

        p = 2.5;
        result = dist.cumulativeProbability(p);
        assertEquals(1, result, 0.0);
    }

    /**
     * Test method {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @org.junit.Test
    public void testInverseCumulativeProbability() throws MathException, OutOfRangeException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double p = 0.5;
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(0, result, 0.0);

        p = 1.0;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);

        p = 0.0;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);

        p = 0.25;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(-0.6794674527885578, result, 0.0001);

        p = 1.25;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(0.6794674527885578, result, 0.0001);
    }

    /**
     * Test method {@link NormalDistributionImpl#sample()}
     */
    @org.junit.Test
    public void testSample() throws MathException {
        RandomDataGenerator random = new RandomDataGenerator();
        NormalDistribution dist = new NormalDistributionImpl();

        for (int i = 0; i < 1000; i++) {
            double result = dist.sample();
            assertTrue(Double.isFinite(result));
            assertEquals(0, dist.cumulativeProbability(result), 0.01);
        }
    }
}
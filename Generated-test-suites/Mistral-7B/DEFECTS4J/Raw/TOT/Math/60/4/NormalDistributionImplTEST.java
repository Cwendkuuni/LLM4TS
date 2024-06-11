package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;

import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("Mean", 0.0, dist.getMean(), 1e-9);
        assertEquals("Standard deviation", 1.0, dist.getStandardDeviation(), 1e-9);
        assertNotNull("Instance created", dist);
    }

    /**
     * Test constructor with negative standard deviation.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(-1.0, 0.0);
    }

    /**
     * Test density.
     */
    @org.junit.Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double density = dist.density(0.5);
        assertEquals("Density at 0.5", 0.10825413, density, 1e-9);

        density = dist.density(-3.2);
        assertEquals("Density at -3.2", 0.00238813, density, 1e-9);
    }

    /**
     * Test cumulative probability.
     */
    @org.junit.Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.158;
        double result = dist.cumulativeProbability(1.2);
        assertEquals("Cumulative probability for 1.2", 0.84134474, result, 1e-9);
        result = dist.cumulativeProbability(-2.5);
        assertEquals("Cumulative probability for -2.5", 0.0, result, 1e-9);
        result = dist.cumulativeProbability(0.0);
        assertEquals("Cumulative probability for 0.0", 0.5, result, 1e-9);

        p = 0.999;
        result = dist.cumulativeProbability(5.0);
        assertEquals("Cumulative probability for 5.0", 0.9999306, result, 1e-9);
        result = dist.cumulativeProbability(-5.0);
        assertEquals("Cumulative probability for -5.0", 0.0, result, 1e-9);

        p = 0.5;
        result = dist.cumulativeProbability(0.0);
        assertEquals("Cumulative probability for 0.0", p, result, 1e-9);
    }

    /**
     * Test inverse cumulative probability.
     */
    @org.junit.Test
    public void testInverseCumulativeProbability() throws MathException,
            OutOfRangeException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.1;
        double result = dist.inverseCumulativeProbability(0.1);
        assertEquals("Inverse cumulative probability for 0.1", -0.8426767, result, 1e-9);

        p = 0.999;
        result = dist.inverseCumulativeProbability(0.999);
        assertEquals("Inverse cumulative probability for 0.999", 5.5811811, result, 1e-9);

        p = 0.5;
        result = dist.inverseCumulativeProbability(0.5);
        assertEquals("Inverse cumulative probability for 0.5", 0.0, result, 1e-9);

        p = 0.0;
        try {
            dist.inverseCumulativeProbability(0.0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            //Expected
        }

        p = 1.0;
        try {
            dist.inverseCumulativeProbability(1.0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            //Expected
        }
    }

    /**
     * Test sample.
     */
    @org.junit.Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double sample = dist.sample();
        assertEquals("Sample", 0.0, sample, 1e-9);

        sample = dist.sample();
        assertTrue("Sample is within bounds", Precision.isClose(0.0, sample, 3.0));
    }
}
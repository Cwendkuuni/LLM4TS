package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

/**
 * Test class for NormalDistributionImpl.
 */
public class NormalDistributionImplTest {

    /**
     * Test the creation of a NormalDistributionImpl with valid parameters.
     */
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals("Mean should be 0", 0, distribution.getMean(), 1e-9);
        assertEquals("Standard Deviation should be 1", 1, distribution.getStandardDeviation(), 1e-9);
    }

    /**
     * Test the density method with valid parameters.
     */
    @Test
    public void testDensityMethod() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double x = 0.5;
        double density = distribution.density(x);
        double expected = 0.1274111; // according to https://en.wikipedia.org/wiki/Normal_distribution#Density_function
        assertEquals("Density should match expected value", expected, density, 1e-6);
    }

    /**
     * Test the cumulativeProbability method with valid parameters.
     */
    @Test
    public void testCumulativeProbabilityMethod() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double x = 0.5;
        double cumulativeProbability = distribution.cumulativeProbability(x);
        double expected = 0.5;
        assertEquals("Cumulative probability should match expected value", expected, cumulativeProbability, 1e-6);
    }

    /**
     * Test the inverseCumulativeProbability method with valid parameters.
     */
    @Test
    public void testInverseCumulativeProbabilityMethod() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double p = 0.5;
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
        double expected = 0.5;
        assertEquals("Inverse cumulative probability should match expected value", expected, inverseCumulativeProbability, 1e-6);
    }

    /**
     * Test the sample method with valid parameters.
     */
    @Test
    public void testSampleMethod() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double sample = distribution.sample();
        double expectedMean = 0;
        double expectedStandardDeviation = 1;
        double expected = Precision.round(FastMath.exp(-0.5) / FastMath.sqrt(2 * FastMath.PI)) * expectedStandardDeviation + expectedMean;
        assertEquals("Sample should match expected value", expected, sample, 1e-3);
    }

    /**
     * Test the density method with negative parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new NormalDistributionImpl(-1, 1);
    }

    /**
     * Test the density method with zero standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroStandardDeviation() {
        new NormalDistributionImpl(0, 0);
    }

    /**
     * Test the cumulativeProbability method with negative parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeX() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double x = -1.0;
        distribution.cumulativeProbability(x);
    }

    /**
     * Test the inverseCumulativeProbability method with negative parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeP() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double p = -1.0;
        distribution.inverseCumulativeProbability(p);
    }

    /**
     * Test the inverseCumulativeProbability method with parameters outside the range.
     */
    @Test(expected = org.apache.commons.math3.exception.OutOfRangeException.class)
    public void testOutOfRangeP() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double p = 1.01;
        distribution.inverseCumulativeProbability(p);
    }

    /**
     * Test the inverseCumulativeProbability method with accuracy set to 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroSolverAccuracy() {
        new NormalDistributionImpl(0, 1, 0);
    }

    /**
     * Test the inverseCumulativeProbability method with accuracy set to a negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSolverAccuracy() {
        new NormalDistributionImpl(0, 1, -0.1);
    }

    /**
     * Test the inverseCumulativeProbability method with accuracy set to a too high value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTooHighSolverAccuracy() {
        new NormalDistributionImpl(0, 1, 1e10);
    }
}
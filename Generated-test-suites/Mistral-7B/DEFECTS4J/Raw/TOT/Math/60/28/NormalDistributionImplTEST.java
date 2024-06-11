package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Tests construction with valid parameters.
     */
    @Test
    public void testConstruction() {
        final double mean = 0;
        final double standardDeviation = 1;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);
        assertEquals(mean, distribution.getMean(), 0.0);
        assertEquals(standardDeviation, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Tests construction with invalid standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() {
        final double mean = 0;
        final double standardDeviation = -1;
        new NormalDistributionImpl(mean, standardDeviation);
    }

    /**
     * Test density method with valid arguments.
     */
    @Test
    public void testDensity() {
        final double mean = 0;
        final double standardDeviation = 1;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);

        final double x = 1.0;
        double density = distribution.density(x);
        final double expected = FastMath.exp(-0.5 * Math.pow(x - mean, 2) / (Math.pow(standardDeviation, 2))) / (standardDeviation * SQRT2PI);
        assertEquals(expected, density, 0.0001);
    }

    /**
     * Test density method with negative argument.
     */
    @Test
    public void testDensityNegativeArgument() {
        final double mean = 0;
        final double standardDeviation = 1;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);

        final double x = -1.0;
        double density = distribution.density(x);
        assertEquals(0.0, density, 0.0);
    }

    /**
     * Test cumulativeProbability method with valid arguments.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        final double mean = 0;
        final double standardDeviation = 1;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);

        final double x = 0.5;
        double cumulativeProbability = distribution.cumulativeProbability(x);
        final double expected = 0.5;
        assertEquals(expected, cumulativeProbability, 0.0001);
    }

    /**
     * Test cumulativeProbability method with extreme arguments.
     */
    @Test
    public void testCumulativeProbabilityExtremeArguments() throws MathException {
        final double mean = 0;
        final double standardDeviation = 1;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);

        final double xMin = -30 * standardDeviation;
        double cumulativeProbabilityMin = distribution.cumulativeProbability(xMin);
        assertEquals(0.0, cumulativeProbabilityMin, 0.0);

        final double xMax = 30 * standardDeviation;
        double cumulativeProbabilityMax = distribution.cumulativeProbability(xMax);
        assertEquals(1.0, cumulativeProbabilityMax, 0.0001);
    }

    /**
     * Test inverseCumulativeProbability method with valid arguments.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        final double mean = 0;
        final double standardDeviation = 1;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);

        final double p = 0.15;
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);

        final double expected = mean + standardDeviation * Erf.erfInverse(p / 2.0);
        assertEquals(expected, inverseCumulativeProbability, 0.0001);
    }

    /**
     * Test inverseCumulativeProbability method with invalid arguments.
     */
    @Test(expected = MathException.class)
    public void testInvalidInverseCumulativeProbability() throws MathException {
        final double mean = 0;
        final double standardDeviation = 1;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);

        final double p = -1.0;
        distribution.inverseCumulativeProbability(p);
    }

    /**
     * Test inverseCumulativeProbability method with p == 0 or p == 1.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        final double mean = 0;
        final double standardDeviation = 1;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);

        final double pMin = 0.0;
        double inverseCumulativeProbabilityMin = distribution.inverseCumulativeProbability(pMin);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbabilityMin, 0.0);

        final double pMax = 1.0;
        double inverseCumulativeProbabilityMax = distribution.inverseCumulativeProbability(pMax);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbabilityMax, 0.0);

    }

    /**
     * Test sample method with valid distribution.
     */
    @Test
    public void testSampleValidDistribution() throws MathException {
        final double mean = 10;
        final double standardDeviation = 3;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);

        final int numberOfSamples = 10;
        for (int i = 0; i < numberOfSamples; i++) {
            final double sample = distribution.sample();
            assertTrue(sample >= mean - 3 * standardDeviation);
            assertTrue(sample <= mean + 3 * standardDeviation);
        }
    }

    /**
     * Test sample method with distribution mean == 0 and standard deviation == 0.
     */
    @Test
    public void testSampleZeroMeanZeroStandardDeviation() throws MathException {
        final NormalDistribution distribution = new NormalDistributionImpl();
        final int numberOfSamples = 10;
        for (int i = 0; i < numberOfSamples; i++) {
            final double sample = distribution.sample();
            assertEquals(0.0, sample, 0.0);
        }
    }

    /**
     * Test sample method with distribution mean == 0 and standard deviation != 0.
     */
    @Test
    public void testSampleZeroMeanNonZeroStandardDeviation() throws MathException {
        final double mean = 0;
        final double standardDeviation = 1;
        final NormalDistribution distribution = new NormalDistributionImpl(mean, standardDeviation);

        final int numberOfSamples = 10;
        for (int i = 0; i < numberOfSamples; i++) {
            final double sample = distribution.sample();
            assertEquals(0.0, sample, Precision.SAFE_EPSILON);
        }
    }
}
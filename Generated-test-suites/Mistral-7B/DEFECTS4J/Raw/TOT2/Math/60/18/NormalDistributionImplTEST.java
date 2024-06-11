package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test the constructor with valid arguments.
     */
    @Test
    public void testNormalDistributionImplConstruction() {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test the constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructionNegativeStdDev() {
        new NormalDistributionImpl(0, -1);
    }

    /**
     * Test the density method with a valid argument.
     */
    @Test
    public void testDensity() {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.398942280401431, dist.density(0), 1e-6);
    }

    /**
     * Test the cumulativeProbability method with a valid argument.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, dist.cumulativeProbability(0), 1e-15);
        assertEquals(0.841345318823081, dist.cumulativeProbability(1), 1e-15);
        assertEquals(0, dist.cumulativeProbability(-2), 1e-15);
        assertEquals(1, dist.cumulativeProbability(2), 1e-15);
    }

    /**
     * Test the inverseCumulativeProbability method with valid arguments.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.inverseCumulativeProbability(0), 1e-15);
        assertEquals(1.282052072098523, dist.inverseCumulativeProbability(0.841345318823081), 1e-15);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.5), 1e-15);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1), 1e-15);
    }

    /**
     * Test the inverseCumulativeProbability method with invalid arguments.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability()
            throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(-1);
    }

    /**
     * Test the sample method.
     */
    @Test
    public void testSample() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        final double sample = dist.sample();
        assertTrue(dist.cumulativeProbability(sample) > 0.5);
    }

    /**
     * Test the sample method with extremely low and high probabilities.
     */
    @Test
    public void testSampleExtremeProbabilities() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        final double sample0 = dist.sample();
        final double sample1 = dist.sample();
        final double sample2 = dist.sample();
        assertFalse(sample0 < -10 || sample0 > 10);
        assertFalse(sample1 < -10 || sample1 > 10);
        assertFalse(sample2 < -10 || sample2 > 10);
    }

    /**
     * Test the getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 1e-10);
    }

    /**
     * Test the getSolverAbsoluteAccuracy method with extreme values.
     */
    @Test
    public void testGetSolverAbsoluteAccuracyExtreme() {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, dist.getSolverAbsoluteAccuracy(), 1e-10);
        assertEquals(0.0, dist.getSolverAbsoluteAccuracy(), 1e-10);
    }

    /**
     * Test the density method with extreme values.
     */
    @Test
    public void testDensityExtreme() {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertFalse(Double.isNaN(dist.density(-10000)));
        assertFalse(Double.isNaN(dist.density(10000)));
    }

    /**
     * Test the cumulativeProbability method with extreme values.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testCumulativeProbabilityExtreme() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(100000);
    }

    /**
     * Test the inverseCumulativeProbability method with extreme values.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityExtreme() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(1.0001);
    }
}
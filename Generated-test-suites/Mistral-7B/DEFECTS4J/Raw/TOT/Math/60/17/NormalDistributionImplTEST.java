package org.apache.commons.math.distribution;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test normal distribution construction with positive mean and standard deviation.
     */
    @Test
    public void testNormalDistributionConstructionPositiveMeanAndStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        assertTrue(normalDist instanceof NormalDistributionImpl);
        assertEquals(0.5, normalDist.getMean(), 0.0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test normal distribution construction with negative mean and positive standard deviation.
     */
    @Test
    public void testNormalDistributionConstructionNegativeMeanAndPositiveStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, -1.0);
        assertTrue(normalDist instanceof NormalDistributionImpl);
        assertEquals(0.5, normalDist.getMean(), 0.0);
        assertEquals(-1.0, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test normal distribution construction with positive mean and zero standard deviation.
     */
    @Test
    public void testNormalDistributionConstructionPositiveMeanAndZeroStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 0.0);
        assertTrue(normalDist instanceof NormalDistributionImpl);
        assertEquals(0.5, normalDist.getMean(), 0.0);
        assertEquals(0.0, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test normal distribution construction with negative mean and zero standard deviation.
     */
    @Test
    public void testNormalDistributionConstructionNegativeMeanAndZeroStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 0.0);
        assertTrue(normalDist instanceof NormalDistributionImpl);
        assertEquals(0.5, normalDist.getMean(), 0.0);
        assertEquals(0.0, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test normal distribution construction with non-positive standard deviation.
     */
    @Test
    public void testNormalDistributionConstructionNonPositiveStandardDeviation() {
        assertThrows(NotStrictlyPositiveException.class, () -> new NormalDistributionImpl(0.5, -1.0));
    }

    /**
     * Test normal distribution density calculation.
     */
    @Test
    public void testNormalDistributionDensity() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double x = 0.5;
        double density = normalDist.density(x);
        assertEquals(0.12566370614359172, density, 1e-6);
    }

    /**
     * Test normal distribution cumulative probability calculation.
     */
    @Test
    public void testNormalDistributionCumulativeProbability() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double x = 0.5;
        double cdf = normalDist.cumulativeProbability(x);
        assertEquals(0.5, cdf, 1e-6);
    }

    /**
     * Test normal distribution inverse cumulative probability calculation.
     */
    @Test
    public void testNormalDistributionInverseCumulativeProbability() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double p = 0.75;
        double x = normalDist.inverseCumulativeProbability(p);
        assertEquals(1.0, x, 1e-6);
    }

    /**
     * Test normal distribution inverse cumulative probability calculation for p = 0 and p = 1.
     */
    @Test
    public void testNormalDistributionInverseCumulativeProbabilityPZeroAndPEqualOne() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double p = 0.0;
        double x = normalDist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, x, 0.0);

        p = 1.0;
        x = normalDist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x, 0.0);
    }

    /**
     * Test normal distribution sample calculation.
     */
    @Test
    public void testNormalDistributionSample() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0.5, 1.0);
        double sample = normalDist.sample();
        double density = normalDist.density(sample);
        assertTrue(Precision.abs(density, 1e-6) > 0);
    }
}
package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

public class NormalDistributionImplTest {

    /**
     * Test normal distribution creation.
     */
    @Test
    public void testCreation() {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        assertEquals(NormalDistribution.class, dist.getClass());
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(sd, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test negative mean creation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeMeanCreation() {
        new NormalDistributionImpl(-1.0, 1.0);
    }

    /**
     * Test negative standard deviation creation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviationCreation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double x = 1.0;
        double expected = 0.39894228040143271;
        assertEquals(expected, dist.density(x), Precision.SAFE_EPSILON);
        x = -1.0;
        expected = 0.39894228040143271;
        assertEquals(expected, dist.density(x), Precision.SAFE_EPSILON);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValid() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double x = 0.5;
        double expected = 0.5;
        assertEquals(expected, dist.cumulativeProbability(x), Precision.SAFE_EPSILON);
        x = 1.5;
        expected = 0.841344746;
        assertEquals(expected, dist.cumulativeProbability(x), Precision.SAFE_EPSILON);
        x = -0.5;
        expected = 0.5;
        assertEquals(expected, dist.cumulativeProbability(x), Precision.SAFE_EPSILON);
        x = -1.5;
        expected = 0.158655254;
        assertEquals(expected, dist.cumulativeProbability(x), Precision.SAFE_EPSILON);
    }

    /**
     * Test cumulativeProbability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalid() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double x = -Double.MAX_VALUE;
        dist.cumulativeProbability(x);
    }

    /**
     * Test inverseCumulativeProbability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValid() throws MathException,
            org.apache.commons.math3.exception.OutOfRangeException {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double p = 0.25;
        double x = dist.inverseCumulativeProbability(p);
        double expected = -0.6745;
        assertEquals(expected, x, Precision.SAFE_EPSILON);
        p = 0.75;
        x = dist.inverseCumulativeProbability(p);
        expected = 0.6745;
        assertEquals(expected, x, Precision.SAFE_EPSILON);
        p = 0.5;
        x = dist.inverseCumulativeProbability(p);
        expected = 0.0;
        assertEquals(expected, x, Precision.SAFE_EPSILON);
    }

    /**
     * Test inverseCumulativeProbability method with edge cases.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException,
            org.apache.commons.math3.exception.OutOfRangeException {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double p = 0.0;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, x, Precision.SAFE_EPSILON);
        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x, Precision.SAFE_EPSILON);
    }

    /**
     * Test inverseCumulativeProbability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalid() throws MathException,
            org.apache.commons.math3.exception.OutOfRangeException {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double p = 1.1;
        dist.inverseCumulativeProbability(p);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() throws MathException {
        final double mean = 0.0;
        final double sd = 1.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double sample = dist.sample();
        assertEquals(sd, Precision.abs(sample - mean), Precision.SAFE_EPSILON);
    }
}
package org.apache.commons.math.distribution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

import org.apache.commons.math.distribution.AbstractContinuousDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test cases for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testNormalDistributionImplWithValidArguments() {
        double mean = 1.0;
        double sd = 2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(sd, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with invalid argument (negative standard deviation).
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplWithInvalidArgument() {
        double mean = 1.0;
        double sd = -2.0;
        new NormalDistributionImpl(mean, sd);
    }

    /**
     * Test density method with valid argument.
     */
    @Test
    public void testDensityWithValidArgument() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = 0.5;
        double expected = 0.1273205890410953;
        assertEquals(expected, dist.density(x), Precision.SAFE_EARPSILON);
    }

    /**
     * Test density method with argument outside the domain.
     */
    @Test
    public void testDensityWithArgumentOutsideDomain() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = Double.NEGATIVE_INFINITY;
        double result = dist.density(x);
        assertEquals(0.0, result, Precision.SAFE_EARPSILON);

        x = Double.POSITIVE_INFINITY;
        result = dist.density(x);
        assertEquals(0.0, result, Precision.SAFE_EARPSILON);
    }

    /**
     * Test cumulativeProbability method with valid arguments.
     */
    @Test
    public void testCumulativeProbabilityWithValidArguments() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = 0.0;
        double expected = 0.5;
        assertEquals(expected, dist.cumulativeProbability(x), Precision.SAFE_EARPSILON);

        x = 1.0;
        expected = 0.8413447460113203;
        assertEquals(expected, dist.cumulativeProbability(x), Precision.SAFE_EARPSILON);

        x = 2.0;
        expected = 0.9772498680574828;
        assertEquals(expected, dist.cumulativeProbability(x), Precision.SAFE_EARPSILON);
    }

    /**
     * Test cumulativeProbability method with arguments outside the domain.
     */
    @Test
    public void testCumulativeProbabilityWithArgumentsOutsideDomain() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x = Double.NEGATIVE_INFINITY;
        double result = dist.cumulativeProbability(x);
        assertEquals(0.0, result, Precision.SAFE_EARPSILON);

        x = Double.POSITIVE_INFINITY;
        result = dist.cumulativeProbability(x);
        assertEquals(1.0, result, Precision.SAFE_EARPSILON);
    }

    /**
     * Test inverseCumulativeProbability method with valid probabilities.
     */
    @Test
    public void testInverseCumulativeProbabilityWithValidProbabilities() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.1587; // approximately the 68th percentile
        double expected = 0.6745;
        assertEquals(expected, dist.inverseCumulativeProbability(p), Precision.SAFE_EARPSILON);

        p = 0.975; // approximately the 99.9th percentile
        expected = 3.0776;
        assertEquals(expected, dist.inverseCumulativeProbability(p), Precision.SAFE_EARPSILON);
    }

    /**
     * Test inverseCumulativeProbability method with invalid probabilities.
     */
    @Test(expected = OutOfRangeException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbabilities() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = -1.0;
        dist.inverseCumulativeProbability(p);
    }

    /**
     * Test inverseCumulativeProbability method with probabilities outside the range [0,1].
     */
    @Test(expected = OutOfRangeException.class)
    public void testInverseCumulativeProbabilityWithProbabilitiesOutsideRange() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 2.0;
        dist.inverseCumulativeProbability(p);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double sample = dist.sample();
        assertEquals(0.0, sample, Precision.SAFE_EARPSILON); // The mean value can be generated as well
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double accuracy = dist.getSolverAbsoluteAccuracy();
        assertEquals(NormalDistributionImpl.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, accuracy, Precision.SAFE_EARPSILON);
    }
}
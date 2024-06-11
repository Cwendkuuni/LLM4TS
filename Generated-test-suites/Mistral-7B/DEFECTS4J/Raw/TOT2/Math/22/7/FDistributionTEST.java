package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Tests for {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Test constructor with valid parameters.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test
    public void testFDistributionValidParameters() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with invalid numerator degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = -1.0;
        double denominatorDegreesOfFreedom = 4.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Test constructor with invalid denominator degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 4.0;
        double denominatorDegreesOfFreedom = -1.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Test density method with valid parameters.
     */
    @Test
    public void testFDistributionDensityValidParameters() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double expected = 0.4414352274634783;
        assertEquals(expected, fDistribution.density(x), 0.00000001);
    }

    /**
     * Test density method with non-positive x value.
     */
    @Test
    public void testFDistributionDensityNonPositiveX() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 0.0;
        double expected = 0.0;
        assertEquals(expected, fDistribution.density(x), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid parameters.
     */
    @Test
    public void testFDistributionCumulativeProbabilityValidParameters() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double expected = 0.9142857142857143;
        assertEquals(expected, fDistribution.cumulativeProbability(x), 0.0000001);
    }

    /**
     * Test cumulativeProbability method with x > supportUpperBound.
     */
    @Test
    public void testFDistributionCumulativeProbabilityUpperBound() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = Double.POSITIVE_INFINITY;
        double expected = 1.0;
        assertEquals(expected, fDistribution.cumulativeProbability(x), 0.0);
    }

    /**
     * Test getNumericalMean method with valid parameters.
     */
    @Test
    public void testFDistributionNumericalMeanValidParameters() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expected = 1.5;
        assertEquals(expected, fDistribution.getNumericalMean(), 0.0);
    }

    /**
     * Test getNumericalMean method with invalid denominator degrees of freedom.
     */
    @Test
    public void testFDistributionNumericalMeanInvalidparameters() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 1.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expected = Double.NaN;
        assertEquals(expected, fDistribution.getNumericalMean(), 0.0);
    }

    /**
     * Test getNumericalVariance method with valid parameters.
     */
    @Test
    public void testFDistributionNumericalVarianceValidParameters() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expected = 2.119813513;
        assertEquals(expected, fDistribution.getNumericalVariance(), 0.0);
    }

    /**
     * Test getNumericalVariance method with invalid denominator degrees of freedom.
     */
    @Test
    public void testFDistributionNumericalVarianceInvalidParameters() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 2.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expected = Double.NaN;
        assertEquals(expected, fDistribution.getNumericalVariance(), 0.0);
    }

    /**
     * Test instantiation with random number generator.
     */
    @Test
    public void testFDistributionInstanceWithRandomNumberGenerator() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        RandomGenerator rng = new Well19937c();
        FDistribution fDistribution = new FDistribution(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}
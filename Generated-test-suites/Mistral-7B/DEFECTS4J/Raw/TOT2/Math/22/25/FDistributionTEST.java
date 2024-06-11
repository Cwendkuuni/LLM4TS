package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNaN;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Tests for the {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Test the constructor.
     *
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertNotNull(fd);
        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the density method.
     */
    @Test
    public void testDensity() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 1.0;
        double density = fd.density(x);
        assertEquals(density, fd.density(x), 0.0);

        x = 2.0;
        density = fd.density(x);
        assertTrue(density > 0.0);

        x = 3.0;
        density = fd.density(x);
        assertTrue(density > 0.0);

        x = 10.0;
        density = fd.density(x);
        assertTrue(density > 0.0);
    }

    /**
     * Test the cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 2.0;
        double cp = fd.cumulativeProbability(x);
        assertTrue(cp > 0.0);
        assertTrue(cp < 1.0);

        x = 1.0;
        cp = fd.cumulativeProbability(x);
        assertTrue(cp < 0.0);

        x = 3.0;
        cp = fd.cumulativeProbability(x);
        assertTrue(cp > 0.0);
        assertTrue(cp < 1.0);

        x = Double.POSITIVE_INFINITY;
        cp = fd.cumulativeProbability(x);
        assertTrue(cp == 1.0);

        x = -1.0;
        cp = fd.cumulativeProbability(x);
        assertTrue(cp == 0.0);
    }

    /**
     * Test the getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        final double numeratorDegreesOfFreedom = 4.0;
        final double denominatorDegreesOfFreedom = 6.0;
        final FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double mean = fd.getNumericalMean();
        assertNotNaN(mean);
        assertEquals(mean, 6.0 / 4.0, 0.0);
    }

    /**
     * Test the getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        final double numeratorDegreesOfFreedom = 4.0;
        final double denominatorDegreesOfFreedom = 6.0;
        final FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double variance = fd.getNumericalVariance();
        assertNotNaN(variance);
        assertEquals(variance, (2 * 36.0 * (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                       (numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) * (denominatorDegreesOfFreedom - 4)), 0.0);
    }

    /**
     * Test the getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        final FDistribution fd = new FDistribution(1.0, 1.0);
        assertEquals(fd.getSupportLowerBound(), 0.0, 0.0);
    }

    /**
     * Test the getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        final FDistribution fd = new FDistribution(1.0, 1.0);
        assertEquals(fd.getSupportUpperBound(), Double.POSITIVE_INFINITY, 0.0);
    }

    /**
     * Test the isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        final FDistribution fd = new FDistribution(1.0, 1.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    /**
     * Test the isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        final FDistribution fd = new FDistribution(1.0, 1.0);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    /**
     * Test the isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        final FDistribution fd = new FDistribution(1.0, 1.0);
        assertTrue(fd.isSupportConnected());
    }

    /**
     * Test the exception handling.
     *
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testExceptionHandling() throws NotStrictlyPositiveException {
        final double negativeNumeratorDegreesOfFreedom = -3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        new FDistribution(negativeNumeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Test the exception handling.
     *
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testExceptionHandlingNegativeDenominator() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 3.0;
        final double negativeDenominatorDegreesOfFreedom = -5.0;
        new FDistribution(numeratorDegreesOfFreedom, negativeDenominatorDegreesOfFreedom);
    }

    /**
     * Test the exception handling.
     *
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testExceptionHandlingNullRNG() throws NotStrictlyPositiveException {
        new FDistribution(null, 3.0, 5.0);
    }

}
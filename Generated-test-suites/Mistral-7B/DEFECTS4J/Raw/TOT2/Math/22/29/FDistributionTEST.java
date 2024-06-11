package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test cases for the {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Test constructor with positive numerator and denominator degrees of freedom.
     */
    @Test
    public void testConstructor() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertNotNull(fd);
    }

    /**
     * Test constructor with invalid degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = 0.0;
        final double denominatorDegreesOfFreedom = 5.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double result;
        result = fd.density(0.5);
        assertEquals(0.0732452343296694, result, 1e-8);
        result = fd.density(2.5);
        assertEquals(0.0613461447673527, result, 1e-8);
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double result;
        result = fd.cumulativeProbability(0.0);
        assertEquals(0.0, result, 1e-8);
        result = fd.cumulativeProbability(0.5);
        assertEquals(0.3732050807568877, result, 1e-8);
        result = fd.cumulativeProbability(1.0);
        assertEquals(0.8095551200344126, result, 1e-8);
        result = fd.cumulativeProbability(2.0);
        assertEquals(0.9772364249694237, result, 1e-8);
    }

    /**
     * Test getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = 3.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, 5.0);
        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Test getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(3.0, denominatorDegreesOfFreedom);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        final double solverAbsoluteAccuracy = 1e-6;
        FDistribution fd = new FDistribution(3.0, 5.0, solverAbsoluteAccuracy);
        assertEquals(solverAbsoluteAccuracy, fd.getSolverAbsoluteAccuracy(), 1e-14);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(3.0, denominatorDegreesOfFreedom);
        double result = fd.getNumericalMean();
        assertTrue(Double.isFinite(result));
        assertEquals(denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2), result, 1e-8);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double result = fd.getNumericalVariance();
        assertTrue(Double.isFinite(result));
        final double expected = (2 * denominatorDegreesOfFreedom * (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                               (numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) * (denominatorDegreesOfFreedom - 4));
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        final FDistribution fd = new FDistribution(3.0, 5.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 1e-14);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        final FDistribution fd = new FDistribution(3.0, 5.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        final FDistribution fd = new FDistribution(3.0, 5.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 1e-14);
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        final FDistribution fd = new FDistribution(3.0, 5.0);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    /**
     * Test distribution with small degrees of freedom.
     */
    @Test
    public void testSmallDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 1.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(Double.NaN, fd.getNumericalMean(), 0);
        assertEquals(Double.NaN, fd.getNumericalVariance(), 0);
    }

    /**
     * Test exception when creating distribution with negative degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = -1.0;
        final double denominatorDegreesOfFreedom = 5.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Test exception when creating distribution with null random number generator.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullRandomNumberGenerator() {
        new FDistribution(null, 5.0, 1e-6);
    }
}
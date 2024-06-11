package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Test cases for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Test constructor with valid arguments.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test
    public void testFDistribution() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with inverse cumulative probability accuracy.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test
    public void testFDistributionAccuracy() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(2.0, 3.0, 0.001);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(0.001, fd.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test constructor with RandomGenerator.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test
    public void testFDistributionRandomGenerator() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(new Well19937c(), 2.0, 3.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test getNumeratorDegreesOfFreedom() method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test getDenominatorDegreesOfFreedom() method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test getSolverAbsoluteAccuracy() method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fd.getSolverAbsoluteAccuracy(), 0.0);

        FDistribution fdAccuracy = new FDistribution(2.0, 3.0, 0.001);
        assertEquals(0.001, fdAccuracy.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test density() method.
     */
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double density = fd.density(1.5);
        // The precise density value is difficult to compare directly, so
        // we compare the absolute difference between the calculated and
        // expected values instead.
        double eps = 1e-9;
        assertTrue(Math.abs(density - 0.1444075541353427) < eps);
    }

    /**
     * Test cumulativeProbability() method.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double probability = fd.cumulativeProbability(1.5);
        double eps = 1e-9;
        assertTrue(Math.abs(probability - 0.9182878627213173) < eps);
    }

    /**
     * Test getNumericalMean() method.
     */
    @Test
    public void testNumericalMean() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double mean = fd.getNumericalMean();
        double eps = 1e-9;
        assertTrue(Math.abs(mean - 1.5) < eps);
    }

    /**
     * Test getNumericalVariance() method.
     */
    @Test
    public void testNumericalVariance() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double variance = fd.getNumericalVariance();
        double eps = 1e-9;
        assertTrue(Math.abs(variance - 0.2721267112184738) < eps);
    }

    /**
     * Test getSupportLowerBound() method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
    }

    /**
     * Test getSupportUpperBound() method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive() method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive() method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected() method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertTrue(fd.isSupportConnected());
    }

    /**
     * Test exception handling for NotStrictlyPositiveException.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() throws NotStrictlyPositiveException {
        new FDistribution(-2.0, 3.0);
    }

    /**
     * Test exception handling for NotStrictlyPositiveException with negative numerator degrees of freedom.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveExceptionNumerator() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 3.0);
    }

    /**
     * Test exception handling for NotStrictlyPositiveException with negative denominator degrees of freedom.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveExceptionDenominator() throws NotStrictlyPositiveException {
        new FDistribution(2.0, 0.0);
    }
}
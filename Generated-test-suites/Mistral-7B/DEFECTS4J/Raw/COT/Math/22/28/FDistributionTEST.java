package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Test case for {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Test constructor with valid parameters.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testFDistribution() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(5.0, 10.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with invalid negative parameters.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() {
        new FDistribution(-1.0, 1.0);
    }

    /**
     * Test density function.
     */
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        double result = fd.density(1.0);
        assertEquals(0.017355376332234093, result, 1e-8);

        fd = new FDistribution(3.0, 6.0);
        result = fd.density(1.5);
        assertEquals(0.03976167435518967, result, 1e-8);

        fd = new FDistribution(2.0, 4.0);
        result = fd.density(2.0);
        assertEquals(Double.NaN, result);
    }

    /**
     * Test cumulative probability function.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        double result = fd.cumulativeProbability(0.0);
        assertEquals(0.0, result, 1e-8);

        result = fd.cumulativeProbability(2.0);
        assertEquals(0.1566489382085195, result, 1e-8);

        result = fd.cumulativeProbability(5.0);
        assertEquals(0.9974896308746744, result, 1e-8);

        fd = new FDistribution(2.0, 4.0);
        result = fd.cumulativeProbability(1.0);
        assertEquals(0.1101797611017976, result, 1e-8);

        fd = new FDistribution(3.0, 6.0);
        result = fd.cumulativeProbability(1.5);
        assertEquals(0.4126059738436415, result, 1e-8);
    }

    /**
     * Test inverse cumulative probability function.
     */
    @Test
    public void testInverseCumulativeProbability() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        assertEquals(0.1306348957752133, fd.inverseCumulativeProbability(0.1), 1e-8);

        fd = new FDistribution(3.0, 6.0);
        assertEquals(1.5129946242505254, fd.inverseCumulativeProbability(0.4), 1e-8);

        fd = new FDistribution(2.0, 4.0);
        assertEquals(Double.NaN, fd.inverseCumulativeProbability(0.5), 1e-8);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        double result = fd.getNumericalMean();
        assertEquals(3.0, result, 1e-8);

        fd = new FDistribution(3.0, 6.0);
        result = fd.getNumericalMean();
        assertEquals(2.5, result, 1e-8);

        fd = new FDistribution(2.0, 4.0);
        result = fd.getNumericalMean();
        assertEquals(Double.NaN, result);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        double result = fd.getNumericalVariance();
        assertEquals(1.586981252379443, result, 1e-8);

        fd = new FDistribution(3.0, 6.0);
        result = fd.getNumericalVariance();
        assertEquals(1.2886825396825397, result, 1e-8);

        fd = new FDistribution(2.0, 4.0);
        result = fd.getNumericalVariance();
        assertEquals(Double.NaN, result);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 1e-8);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 1e-8);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(5.0, 10.0);
        assertTrue(fd.isSupportConnected());
    }

    /**
     * Test constructor with random number generator.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testFDistributionWithRandomNumberGenerator() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(new Well19937c(), 5.0, 10.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }
}
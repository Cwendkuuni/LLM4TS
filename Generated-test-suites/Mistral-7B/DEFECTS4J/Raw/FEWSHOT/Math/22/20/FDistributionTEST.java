package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import junit.framework.JUnit4TestAdapter;

/**
 * Test cases for FDistribution class.
 */
public class FDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstruction() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertNotNull(dist);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() {
        new FDistribution(-1.0, 1.0);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        for (double x = 0.0; x <= 10.0; x += 0.1) {
            double density = dist.density(x);
            double expected = Beta.regularizedBeta((2.0 * x) / (3.0 + 2.0 * x), 1.0, 1.5)
                    * Math.pow(Math.PI * Math.pow(2.0, 1.5), 0.5);
            assertEquals(expected, density, 0.0001);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        for (double x = 0.0; x <= 10.0; x += 0.1) {
            double prob = dist.cumulativeProbability(x);
            double expected = Beta.cumulativeProbability(x / 3.0, 2.0, 3.0);
            assertEquals(expected, prob, 0.0001);
        }
    }

    /**
     * Test getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY,
                dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(3.0 / 2.0, dist.getNumericalMean(), 0.01);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(12.0 / 15.0, dist.getNumericalVariance(), 0.01);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test distribution properties.
     */
    @Test
    public void testProperties() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        RealDistribution inverse = dist.createInverseCumulativeProbability();
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(1.0 / (2.0 * 3.0), dist.getNumericalMean(), 0.01);
        assertEquals(12.0 / 15.0, dist.getNumericalVariance(), 0.01);
        assertArrayEquals(new double[] { 0.0, 1.0 / 3.0, 1.0 / 2.0, 2.0 / 3.0, 1.0, 2.0, 3.0 / 2.0, 2.0, 3.0 / 2.0,
                3.0, 4.0 / 3.0, 2.0, 5.0 / 6.0, 3.0, 2.0, 7.0 / 6.0, 3.0, 4.0, 5.0 / 4.0, 6.0 / 5.0, 2.0,
                7.0, 8.0 / 7.0, 3.0, 9.0 / 8.0, 4.0, 10.0 / 9.0, 3.0, 11.0 / 10.0, 5.0, 12.0 / 11.0, 4.0,
                13.0 / 12.0, 6.0, 14.0 / 13.0, 5.0, 15.0 / 14.0, 7.0, 16.0 / 15.0, 6.0, 17.0 / 16.0, 8.0,
                18.0 / 17.0, 9.0, 19.0 / 18.0, 10.0, 20.0 / 19.0, 11.0, 21.0 / 20.0, 12.0, 22.0 / 21.0, 13.0,
                23.0 / 22.0, 14.0, 24.0 / 23.0, 15.0, 25.0 / 24.0, 16.0, 26.0 / 25.0, 17.0, 27.0 / 26.0, 18.0,
                28.0 / 27.0, 19.0, 29.0 / 28.0, 20.0 }, inverse.getCumulativeProbability(new double[] { 0.0, 0.1, 0.2, 0.3, 0.4,
                0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, 2.1, 2.2, 2.3,
                2.4, 2.5, 2.6, 2.7, 2.8, 2.9, 3.0, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9, 4.0, 4.1, 4.2,
                4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 5.0, 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 5.9, 6.0, 6.1,
                6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8, 6.9, 7.0, 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7, 7.8, 7.9, 8.0,
                8.1, 8.2, 8.3, 8.4, 8.5, 8.6, 8.7, 8.8, 8.9, 9.0, 9.1, 9.2, 9.3, 9.4, 9.5, 9.6, 9.7, 9.8, 9.9,
                10.0 }));
    }

    /**
     * Java test runner.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FDistributionTest.class);
    }
}
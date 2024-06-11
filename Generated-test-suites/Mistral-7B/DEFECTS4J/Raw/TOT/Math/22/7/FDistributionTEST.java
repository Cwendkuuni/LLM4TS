package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Test constructor with valid arguments.
     * @throws NotStrictlyPositiveException if arguments are not positive.
     */
    @Test
    public void testFDistributionConstruction() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructionInvalidArguments() {
        new FDistribution(-2.0, 4.0);
        new FDistribution(2.0, -4.0);
    }

    /**
     * Test getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test density method with valid arguments.
     */
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double x = 1.5;
        double expected = 0.4359859843140345;
        assertEquals(expected, fd.density(x), 1e-6);
    }

    /**
     * Test density method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDensityInvalidArguments() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        fd.density(-1.0);
    }

    /**
     * Test cumulativeProbability method with valid arguments.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double x = 1.5;
        double expected = 0.9110711121193803;
        assertEquals(expected, fd.cumulativeProbability(x), 1e-6);
    }

    /**
     * Test cumulativeProbability method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityInvalidArguments() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        fd.cumulativeProbability(-1.0);
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, fd.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(2.5, 4.5);
        double expected = 2.5 / (2.5 - 2);
        assertEquals(expected, fd.getNumericalMean(), 1e-6);
    }

    /**
     * Test getNumericalVariance method with valid arguments.
     */
    @Test
    public void testGetNumericalVarianceValidArguments() {
        FDistribution fd = new FDistribution(2.5, 4.5);
        double expected = (2 * (4.5 * 4.5) * (2.5 + 4.5 - 2)) /
                ((2.5 * (4.5 - 2) * (4.5 - 4)));
        assertEquals(expected, fd.getNumericalVariance(), 1e-6);
    }

    /**
     * Test getNumericalVariance method with invalid arguments.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetNumericalVarianceInvalidArguments() {
        FDistribution fd = new FDistribution(2.0, 2.0);
        fd.getNumericalVariance();
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        assertTrue(fd.isSupportConnected());
    }

    /**
     * Test generateRandomValue method with valid arguments.
     */
    @Test
    public void testGenerateRandomValue() {
        FDistribution fd = new FDistribution(new Well19937c(), 2.0, 4.0);
        double expectedMean = fd.getNumericalMean();
        double expectedVariance = fd.getNumericalVariance();
        for (int i = 0; i < 10000; i++) {
            double value = fd.generateRandomValue();
            double meanDeviation = Math.abs(value - expectedMean);
            double varianceDeviation = Math.pow(Math.abs(value - expectedMean * expectedMean), 0.5);
            assertTrue(meanDeviation <= expectedVariance);
            assertTrue(varianceDeviation <= expectedVariance);
        }
    }

    /**
     * Test generateRandomValues method with valid arguments.
     */
    @Test
    public void testGenerateRandomValues() {
        FDistribution fd = new FDistribution(new Well19937c(), 2.0, 4.0);
        int sampleSize = 1000;
        double[] values = new double[sampleSize];
        fd.generateRandomValues(values);
        for (int i = 0; i < sampleSize; i++) {
            double value = values[i];
            double expectedMean = fd.getNumericalMean();
            double expectedVariance = fd.getNumericalVariance();
            double meanDeviation = Math.abs(value - expectedMean);
            double varianceDeviation = Math.pow(Math.abs(value - expectedMean * expectedMean), 0.5);
            assertTrue(meanDeviation <= expectedVariance);
            assertTrue(varianceDeviation <= expectedVariance);
        }
    }
}
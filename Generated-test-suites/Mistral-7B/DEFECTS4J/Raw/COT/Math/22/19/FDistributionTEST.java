package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.MathException;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomGenerator;
import org.apache.commons.math3.util.Statistics;
import org.junit.Test;

/**
 * JUnit test cases for FDistribution class.
 */
public class FDistributionTest {

    

    /**
     * Test constructor with valid arguments.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testFDistributionConstructorValidArgs()
            throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(3.0, 4.0);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
        assertNotNull(fd);
    }

    /**
     * Test density function.
     */
    @Test
    public void testDensityFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double density = fd.density(1.5);
        assertEquals(0.0411200993529788, density, 1e-9);
    }

    /**
     * Test cumulativeProbability function.
     */
    @Test
    public void testCumulativeProbabilityFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double cdf = fd.cumulativeProbability(1.5);
        assertEquals(0.918443823987746, cdf, 1e-9);
    }

    /**
     * Test getNumericalMean function.
     */
    @Test
    public void testGetNumericalMeanFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double mean = fd.getNumericalMean();
        assertEquals(2.5, mean, 1e-9);
    }

    /**
     * Test getNumericalVariance function.
     */
    @Test
    public void testGetNumericalVarianceFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double variance = fd.getNumericalVariance();
        assertEquals(1.92306441147288, variance, 1e-9);
    }

    /**
     * Test getSupportLowerBound function.
     */
    @Test
    public void testGetSupportLowerBoundFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double lowerBound = fd.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 1e-9);
    }

    /**
     * Test getSupportUpperBound function.
     */
    @Test
    public void testGetSupportUpperBoundFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double upperBound = fd.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-9);
    }

    /**
     * Test isSupportLowerBoundInclusive function.
     */
    @Test
    public void testIsSupportLowerBoundInclusiveFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive function.
     */
    @Test
    public void testIsSupportUpperBoundInclusiveFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected function.
     */
    @Test
    public void testIsSupportConnectedFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        assertTrue(fd.isSupportConnected());
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorInvalidArgs()
            throws NotStrictlyPositiveException {
        new FDistribution(-1.0, 4.0);
    }

    /**
     * Test constructor with negative numerator degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorNegativeNumeratorDegreesOfFreedom()
            throws NotStrictlyPositiveException {
        new FDistribution(0.0, 4.0);
    }

    /**
     * Test constructor with negative denominator degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorNegativeDenominatorDegreesOfFreedom()
            throws NotStrictlyPositiveException {
        new FDistribution(3.0, 0.0);
    }

    /**
     * Test inverseCumulativeProbability function.
     */
    @Test
    public void testInverseCumulativeProbabilityFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double q = fd.inverseCumulativeProbability(0.95);
        assertEquals(3.04414322312864, q, 1e-9);
    }

    /**
     * Test inverseCumulativeProbability function when p > 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityFunctionGreaterThanOne() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        fd.inverseCumulativeProbability(1.1);
    }

    /**
     * Test inverseCumulativeProbability function when p < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityFunctionLessThanZero() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        fd.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test generateRandomNumber function.
     */
    @Test
    public void testGenerateRandomNumberFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        RandomGenerator rng = fd.getRandomGenerator();
        double randomNumber = fd.generateRandomNumber(rng);
        double mean = fd.getNumericalMean();
        double variance = fd.getNumericalVariance();
        double stdDev = Math.sqrt(variance);

        assertEquals(mean, Statistics.mean(new double[]{randomNumber}), 1e-9);
        assertEquals(stdDev, Statistics.stdev(new double[]{randomNumber}), 1e-9);
    }

    /**
     * Test generateRandomNumbersInRange function.
     */
    @Test
    public void testGenerateRandomNumbersInRangeFunction() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        int size = 10000;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] randomNumbers = new double[size];

        for (int i = 0; i < size; i++) {
            randomNumbers[i] = fd.generateRandomNumberInRange(lowerBound, upperBound);
        }

        double mean = Statistics.mean(randomNumbers);
        double variance = Statistics.variance(randomNumbers);
        double stdDev = Math.sqrt(variance);

        assertEquals(fd.getNumericalMean(), mean, 1e-9);
        assertEquals(fd.getNumericalVariance(), variance, 1e-9);
        assertTrue(Precision.isClose(fd.getNumericalMean(), mean, 1e-9));
        assertTrue(Precision.isClose(fd.getNumericalVariance(), variance, 1e-9));
    }

    
}
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidPopulationSize_ThrowsNotStrictlyPositiveException() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(0, 1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses_ThrowsNotPositiveException() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, -1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize_ThrowsNotPositiveException() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 1, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidNumberOfSuccessesAndPopulationSize_ThrowsNumberIsTooLargeException() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 11, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidSampleSizeAndPopulationSize_ThrowsNumberIsTooLargeException() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 1, 11);
    }

    @Test
    public void testGetNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, hypergeometricDistribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, hypergeometricDistribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, hypergeometricDistribution.getSampleSize());
    }

    @Test
    public void testGetNumericalMean() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.225, hypergeometricDistribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }

    @Test
    public void testCumulativeProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.3333, hypergeometricDistribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.3333, hypergeometricDistribution.probability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6666, hypergeometricDistribution.upperCumulativeProbability(2), 0.0001);
    }

    @Test
    public void testNullNumericalVariance() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertFalse(hypergeometricDistribution.isNumericalVarianceCalculated());
        assertNull(hypergeometricDistribution.getNumericalVariance());
    }
}
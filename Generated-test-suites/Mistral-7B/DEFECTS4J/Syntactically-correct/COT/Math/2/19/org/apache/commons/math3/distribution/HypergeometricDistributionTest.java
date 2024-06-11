package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test cases for {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() {
        new HypergeometricDistribution(0, 1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroNumberOfSuccesses() {
        new HypergeometricDistribution(10, 0, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 1, 11);
    }

    @Test
    public void testCumulativeProbabilityZero() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.0, dist.cumulativeProbability(0), 1e-15);
    }

    @Test
    public void testCumulativeProbabilityMiddle() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.3515625, dist.cumulativeProbability(3), 1e-15);
    }

    @Test
    public void testCumulativeProbabilityMax() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.640625, dist.cumulativeProbability(2), 1e-15);
        assertEquals(1.0, dist.cumulativeProbability(2), 1e-15);
    }

    @Test
    public void testProbabilityZero() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.01041666666666667, dist.probability(0), 1e-15);
    }

    @Test
    public void testProbabilityMiddle() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.27613888888888884, dist.probability(1), 1e-15);
    }

    @Test
    public void testProbabilityMax() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.4583333333333333, dist.probability(2), 1e-15);
    }

    @Test
    public void testUpperCumulativeProbabilityZero() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 1e-15);
    }

    @Test
    public void testUpperCumulativeProbabilityMax() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 1e-15);
    }

    @Test
    public void testUpperCumulativeProbabilityMiddle() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.3515625, dist.upperCumulativeProbability(2), 1e-15);
    }

    @Test
    public void testGetNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(2, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(3, dist.getSampleSize());
    }

    @Test
    public void testGetNumericalMean() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(1.6, dist.getNumericalMean(), 1e-15);
    }

    @Test
    public void testGetNumericalVariance() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(0.38888888888888886, dist.getNumericalVariance(), 1e-15);
    }

    @Test
    public void testGetSupportLowerBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(1, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertEquals(2, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 3);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testGenerateRandomNumber() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 2, 3);

        for (int i = 0; i < 10000; i++) {
            int num = dist.sample();
            assertTrue(num >= 0 && num <= 2);
        }
    }
}
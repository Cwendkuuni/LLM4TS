package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test suite for the HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {

    /**
     * Tests constructor.
     *
     * @throws Exception if an exception is thrown by the constructor
     */
    @Test
    public void testInstance() throws Exception {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertNotNull(dist);
    }

    /**
     * Tests constructor with negative arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    /**
     * Tests constructor with non-positive population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonPositivePopulationSize() {
        new HypergeometricDistribution(0, 5, 5);
    }

    /**
     * Tests constructor with non-positive sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testNonPositiveSampleSize() {
        new HypergeometricDistribution(10, 5, 0);
    }

    /**
     * Tests constructor with sample size larger than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    /**
     * Tests constructor with number of successes larger than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    /**
     * Tests getters.
     */
    @Test
    public void testGetters() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    /**
     * Tests probability method with argument outside the support.
     */
    @Test
    public void testProbabilityOutsideSupport() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertFalse(dist.probability(-1));
        assertFalse(dist.probability(11));
    }

    /**
     * Tests probability method with arguments inside the support.
     */
    @Test
    public void testProbabilityInsideSupport() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = dist.probability(x);
            assertEquals(dist.cumulativeProbability(x), p, 1e-12);
        }
    }

    /**
     * Tests cumulativeProbability method with argument outside the support.
     */
    @Test
    public void testCumulativeProbabilityOutsideSupport() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertFalse(dist.cumulativeProbability(-1) > 0);
        assertFalse(dist.cumulativeProbability(11) <= 1);
    }

    /**
     * Tests cumulativeProbability method with arguments inside the support.
     */
    @Test
    public void testCumulativeProbabilityInsideSupport() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            assertEquals(dist.probability(x), dist.cumulativeProbability(x + 1) - dist.cumulativeProbability(x),
                        1e-12);
        }
    }

    /**
     * Tests upperCumulativeProbability method with argument outside the support.
     */
    @Test
    public void testUpperCumulativeProbabilityOutsideSupport() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertFalse(dist.upperCumulativeProbability(-1) > 0);
        assertFalse(dist.upperCumulativeProbability(11) <= 1);
    }

    /**
     * Tests upperCumulativeProbability method with arguments inside the support.
     */
    @Test
    public void testUpperCumulativeProbabilityInsideSupport() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            assertEquals(1 - dist.cumulativeProbability(x), dist.upperCumulativeProbability(x), 1e-12);
        }
    }

    /**
     * Tests getDomain method.
     */
    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();
        int[] expected = { 5, 5 };
        assertArraysEqual(expected, domain);
    }

    /**
     * Tests getLowerDomain method.
     */
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getLowerDomain());
    }

    /**
     * Tests getUpperDomain method.
     */
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getUpperDomain());
    }

    /**
     * Tests getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5.0, dist.getNumericalMean(), 1e-12);
    }

    /**
     * Tests getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(1.97087389326793, numericalVariance, 1e-12);
    }

    /**
     * Tests getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0, dist.getSupportLowerBound());
    }

    /**
     * Tests getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSupportUpperBound());
    }

    /**
     * Tests isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Helper method for asserting arrays are equal.
     *
     * @param expected the expected array
     * @param actual the actual array
     */
    private void assertArraysEqual(int[] expected, int[] actual) {
        Assert.assertArrayEquals(expected, actual);
    }
}
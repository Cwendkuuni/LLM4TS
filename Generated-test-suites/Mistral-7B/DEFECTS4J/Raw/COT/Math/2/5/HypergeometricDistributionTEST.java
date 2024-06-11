package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

/**
 * Test cases for HypergeometricDistribution class
 */
public class HypergeometricDistributionTest {

    @ org.junit.Test
    public void testHypergeometricDistributionConstructor() {
        try {
            new HypergeometricDistribution(-1, 5, 5);
            assertTrue(false); // should not reach this point
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(5, -1, 5);
            assertTrue(false); // should not reach this point
        } catch (NotPositiveException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(5, 5, -1);
            assertTrue(false); // should not reach this point
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(5, 6, 5);
            assertTrue(false); // should not reach this point
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(5, 5, 6);
            assertTrue(false); // should not reach this point
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(0, 5, 5);
            assertTrue(false); // should not reach this point
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(5, 0, 5);
            assertTrue(false); // should not reach this point
        } catch (NotPositiveException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(5, 5, 0);
            assertTrue(false); // should not reach this point
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        new HypergeometricDistribution(5, 5, 5);
    }

    @ org.junit.Test
    public void testGetters() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 5);
        assertEquals(5, dist.getPopulationSize());
        assertEquals(3, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    @ org.junit.Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 5);

        assertEquals(0.0, dist.cumulativeProbability(0), 1e-10);
        assertEquals(0.1875, dist.cumulativeProbability(1), 1e-10);
        assertEquals(0.375, dist.cumulativeProbability(2), 1e-10);
        assertEquals(0.5625, dist.cumulativeProbability(3), 1e-10);
        assertEquals(0.75, dist.cumulativeProbability(4), 1e-10);
        assertEquals(1.0, dist.cumulativeProbability(5), 1e-10);
        assertEquals(1.0, dist.cumulativeProbability(6), 1e-10); // should be 1.0, not NaN

        assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), 1e-10);

        int[] domain = dist.getDomain();
        assertEquals(domain[0], 2);
        assertEquals(domain[1], 5);
    }

    @ org.junit.Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 5);

        assertEquals(0.03125, dist.probability(0), 1e-10);
        assertEquals(0.1176470588235294, dist.probability(1), 1e-10);
        assertEquals(0.1875, dist.probability(2), 1e-10);
        assertEquals(0.2232146536214623, dist.probability(3), 1e-10);
        assertEquals(0.1875, dist.probability(4), 1e-10);
        assertEquals(0.1176470588235294, dist.probability(5), 1e-10);
        assertEquals(0.03125, dist.probability(6), 1e-10);
        assertEquals(0.0, dist.probability(7), 1e-10);

        assertEquals(0.0, dist.probability(Integer.MIN_VALUE), 1e-10);
        assertEquals(0.0, dist.probability(Integer.MAX_VALUE), 1e-10);
    }

    @ org.junit.Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 5);

        assertEquals(0.8125, dist.upperCumulativeProbability(0), 1e-10);
        assertEquals(0.6875, dist.upperCumulativeProbability(1), 1e-10);
        assertEquals(0.5625, dist.upperCumulativeProbability(2), 1e-10);
        assertEquals(0.45, dist.upperCumulativeProbability(3), 1e-10);
        assertEquals(0.3125, dist.upperCumulativeProbability(4), 1e-10);
        assertEquals(0.1875, dist.upperCumulativeProbability(5), 1e-10);
        assertEquals(0.0, dist.upperCumulativeProbability(6), 1e-10);

        int[] domain = dist.getDomain();
        assertEquals(domain[0], 2);
        assertEquals(domain[1], 5);
    }

    @ org.junit.Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 5);
        assertEquals(3.0, dist.getNumericalMean(), 1e-10);
    }

    @ org.junit.Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 5);
        assertEquals(0.15625, dist.getNumericalVariance(), 1e-10);

        dist.calculateNumericalVariance();
    }
}
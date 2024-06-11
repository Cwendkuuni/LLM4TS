package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid arguments.
     */
    @Test
    public void testConstruction() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(4, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    /**
     * Test construction with negative arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionNegativePopulationSize() {
        new HypergeometricDistribution(-1, 4, 5);
    }

    /**
     * Test construction with negative arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    /**
     * Test construction with negative arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionNegativeSampleSize() {
        new HypergeometricDistribution(10, 4, -1);
    }

    /**
     * Test construction with large arguments.
     */
    @Test
    public void testConstructionLargeArguments() {
        HypergeometricDistribution dist = new HypergeometricDistribution(
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, dist.getPopulationSize());
        assertEquals(Integer.MAX_VALUE, dist.getNumberOfSuccesses());
        assertEquals(Integer.MAX_VALUE, dist.getSampleSize());
    }

    /**
     * Test construction with invalid arguments.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionInvalidArguments() {
        new HypergeometricDistribution(10, 11, 5);
    }

    /**
     * Test {@link #cumulativeProbability(int)}.
     */
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        // Test with valid arguments
        assertEquals(0.0, dist.cumulativeProbability(0), 1e-12);
        assertEquals(0.005641025, dist.cumulativeProbability(1), 1e-12);
        assertEquals(0.033142241, dist.cumulativeProbability(2), 1e-12);
        assertEquals(0.109861167, dist.cumulativeProbability(3), 1e-12);
        assertEquals(0.286887228, dist.cumulativeProbability(4), 1e-12);
        assertEquals(0.503603139, dist.cumulativeProbability(5), 1e-12);
        assertEquals(1.0, dist.cumulativeProbability(5), 1e-12);

        // Test with invalid arguments
        assertEquals(0.0, dist.cumulativeProbability(-1), 1e-12);
        assertEquals(1.0, dist.cumulativeProbability(6), 1e-12);
    }

    /**
     * Test {@link #probability(int)}.
     */
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        // Test with valid arguments
        assertEquals(0.000975505, dist.probability(0), 1e-12);
        assertEquals(0.012816993, dist.probability(1), 1e-12);
        assertEquals(0.023633986, dist.probability(2), 1e-12);
        assertEquals(0.042835582, dist.probability(3), 1e-12);
        assertEquals(0.073288223, dist.probability(4), 1e-12);
        assertEquals(0.115570563, dist.probability(5), 1e-12);
        assertEquals(0.127578319, dist.probability(6), 1e-12);

        // Test with invalid arguments
        assertEquals(0.0, dist.probability(-1), 1e-12);
        assertEquals(0.0, dist.probability(7), 1e-12);
    }

    /**
     * Test {@link #upperCumulativeProbability(int)}.
     */
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 4, 5);
        // Test with valid arguments
        assertEquals(0.999024495, dist.upperCumulativeProbability(0), 1e-12);
        assertEquals(0.987183007, dist.upperCumulativeProbability(1), 1e-12);
        assertEquals(0.973733523, dist.upperCumulativeProbability(2), 1e-12);
        assertEquals(0.948412861, dist.upperCumulativeProbability(3), 1e-12);
        assertEquals(0.883692433, dist.upperCumulativeProbability(4), 1e-12);
        assertEquals(0.805016238, dist.upperCumulativeProbability(5), 1e-12);
        assertEquals(0.547334471, dist.upperCumulativeProbability(6), 1e-12);

        // Test with invalid arguments
        assertEquals(0.0, dist.upperCumulativeProbability(-1), 1e-12);
        assertEquals(1.0, dist.upperCumulativeProbability(7), 1e-12);
    }
}
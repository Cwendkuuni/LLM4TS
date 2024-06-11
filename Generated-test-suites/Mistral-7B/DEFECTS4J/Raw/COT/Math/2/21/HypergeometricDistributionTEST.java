package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test class for HypergeometricDistribution.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid arguments.
     * @throws Exception if an exception is thrown.
     */
    @Test
    public void testValidConstructor() throws Exception {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 3);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        new HypergeometricDistribution(10, 5, 0);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    /**
     * Test constructor with random generator.
     * @throws Exception if an exception is thrown.
     */
    @Test
    public void testRandomConstructor() throws Exception {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }

    /**
     * Test method getDomain.
     */
    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int[] domain = dist.getDomain();
        assertEquals(3, domain[0]);
        assertEquals(5, domain[1]);
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.cumulativeProbability(0), 1e-12);
        assertEquals(0.204587818604708, dist.cumulativeProbability(2), 1e-12);
        assertEquals(0.523809523757274, dist.cumulativeProbability(4), 1e-12);
        assertEquals(0.997019833519663, dist.cumulativeProbability(9), 1e-12);
        assertEquals(1.0, dist.cumulativeProbability(10), 1e-12);
    }

    /**
     * Test method probability.
     */
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0384615384615385, dist.probability(0), 1e-12);
        assertEquals(0.0826086956521736, dist.probability(1), 1e-12);
        assertEquals(0.0769230769230769, dist.probability(2), 1e-12);
        assertEquals(0.115764705882353, dist.probability(3), 1e-12);
        assertEquals(0.115764705882353, dist.probability(4), 1e-12);
        assertEquals(0.115764705882353, dist.probability(5), 1e-12);
        assertEquals(0.0826086956521736, dist.probability(6), 1e-12);
        assertEquals(0.0384615384615385, dist.probability(9), 1e-12);
        assertEquals(0.0, dist.probability(11), 1e-12);
    }

    /**
     * Test method upperCumulativeProbability.
     */
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.997019833519663, dist.upperCumulativeProbability(0), 1e-12);
        assertEquals(0.959138611855062, dist.upperCumulativeProbability(1), 1e-12);
        assertEquals(0.882036880555649, dist.upperCumulativeProbability(2), 1e-12);
        assertEquals(0.811412640428058, dist.upperCumulativeProbability(3), 1e-12);
        assertEquals(0.720985925632022, dist.upperCumulativeProbability(4), 1e-12);
        assertEquals(0.619722149251024, dist.upperCumulativeProbability(5), 1e-12);
        assertEquals(0.494433156419199, dist.upperCumulativeProbability(6), 1e-12);
        assertEquals(0.344116519422594, dist.upperCumulativeProbability(7), 1e-12);
        assertEquals(0.16896549583984, dist.upperCumulativeProbability(8), 1e-12);
        assertEquals(0.0, dist.upperCumulativeProbability(10), 1e-12);
    }

    /**
     * Test method getNumericalMean.
     */
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3.5, dist.getNumericalMean(), 1e-12);
    }

    /**
     * Test method getNumericalVariance.
     */
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.12867812867813, dist.getNumericalVariance(), 1e-12);
    }

    /**
     * Test method getSupportLowerBound.
     */
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportLowerBound());
    }

    /**
     * Test method getSupportUpperBound.
     */
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, dist.getSupportUpperBound());
    }
}
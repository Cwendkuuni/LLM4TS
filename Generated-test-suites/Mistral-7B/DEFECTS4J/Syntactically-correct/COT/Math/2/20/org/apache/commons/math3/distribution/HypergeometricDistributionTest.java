package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test for {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSize() {
        new HypergeometricDistribution(0, 1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroNumberOfSuccesses() {
        new HypergeometricDistribution(10, 0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSampleSize() {
        new HypergeometricDistribution(10, 1, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 1, 11);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();
        assertEquals(0, domain[0]);
        assertEquals(5, domain[1]);

        // Test with values out of domain
        assertEquals(0.0, dist.cumulativeProbability(-1), 1e-10);
        assertEquals(1.0, dist.cumulativeProbability(6), 1e-10);

        // Test with values in domain
        assertEquals(0.0821641, dist.cumulativeProbability(0), 1e-10); // p(0)
        assertEquals(0.15625, dist.cumulativeProbability(1), 1e-10); // p(1)
        assertEquals(0.2682985, dist.cumulativeProbability(2), 1e-10); // p(2)
        assertEquals(0.3973684, dist.cumulativeProbability(3), 1e-10); // p(3)
        assertEquals(0.5343262, dist.cumulativeProbability(4), 1e-10); // p(4)
        assertEquals(1.0, dist.cumulativeProbability(5), 1e-10); // p(5)
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();
        assertEquals(0, domain[0]);
        assertEquals(5, domain[1]);

        // Test with values out of domain
        assertEquals(0.0, dist.probability(-1), 1e-10);
        assertEquals(0.0, dist.probability(6), 1e-10);

        // Test with values in domain
        assertEquals(0.0821641, dist.probability(0), 1e-10); // p(0)
        assertEquals(0.1389534, dist.probability(1), 1e-10); // p(1)
        assertEquals(0.1913622, dist.probability(2), 1e-10); // p(2)
        assertEquals(0.2576111, dist.probability(3), 1e-10); // p(3)
        assertEquals(0.3391661, dist.probability(4), 1e-10); // p(4)
        assertEquals(0.3846154, dist.probability(5), 1e-10); // p(5)
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();
        assertEquals(0, domain[0]);
        assertEquals(5, domain[1]);

        // Test with values out of domain
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 1e-10);
        assertEquals(0.0, dist.upperCumulativeProbability(6), 1e-10);

        // Test with values in domain
        assertEquals(0.917836, dist.upperCumulativeProbability(0), 1e-10); // P(X >= 0)
        assertEquals(0.861047, dist.upperCumulativeProbability(1), 1e-10); // P(X >= 1)
        assertEquals(0.8041956, dist.upperCumulativeProbability(2), 1e-10); // P(X >= 2)
        assertEquals(0.7473442, dist.upperCumulativeProbability(3), 1e-10); // P(X >= 3)
        assertEquals(0.6904928, dist.upperCumulativeProbability(4), 1e-10); // P(X >= 4)
        assertEquals(0.6336316, dist.upperCumulativeProbability(5), 1e-10); // P(X >= 5)
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(3.5, dist.getNumericalMean(), 1e-10);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(1.587401, dist.getNumericalVariance(), 1e-10);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testGenerateValues() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int numberOfTrials = 100000;
        int[] values = new int[numberOfTrials];

        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < numberOfTrials; i++) {
            values[i] = dist.sample(rng);
        }

        int[] domain = dist.getDomain();
        int sum = 0;
        int sumSquare = 0;

        for (int i = 0; i < numberOfTrials; i++) {
            assertEquals(domain[0], values[i], 0); // Check if all values are in the domain
            sum += values[i];
            sumSquare += values[i] * values[i];
        }

        assertEquals(5 * numberOfTrials / 2, sum, 1e-10); // Check the mean
        assertEquals(5 * numberOfTrials * (5 * numberOfTrials - 1) / 12, sumSquare, 1e-10); // Check the variance
    }
}
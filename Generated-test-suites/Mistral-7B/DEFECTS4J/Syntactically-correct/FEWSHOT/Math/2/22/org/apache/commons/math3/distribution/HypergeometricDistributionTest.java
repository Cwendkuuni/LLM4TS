package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());

        // Test lower and upper bounds
        int[] domain = distribution.getDomain();
        assertEquals(domain[0], distribution.getSupportLowerBound());
        assertEquals(domain[1], distribution.getSupportUpperBound());

        // Test mean and variance
        assertEquals(5.0, distribution.getNumericalMean(), 0.001);
        double numericalVariance = distribution.getNumericalVariance();
        assertEquals(0.0388888888888889, numericalVariance, 0.000000001);

        // Test probability distribution
        for (int i = domain[0]; i <= domain[1]; i++) {
            double probability = distribution.probability(i);
            double cumulativeProbability = distribution.cumulativeProbability(i);
            double upperCumulativeProbability = distribution.upperCumulativeProbability(i);
            assertEquals(cumulativeProbability, upperCumulativeProbability, 0.0001);
            assertEquals(probability, cumulativeProbability - distribution.cumulativeProbability(i - 1), 0.0001);
        }

        // Test negative values and invalid parameters
        try {
            new HypergeometricDistribution(-1, 5, 5);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, -5, 5);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(0, 5, 5);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(10, 11, 5);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(10, 5, 11);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }
}
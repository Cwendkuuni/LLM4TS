package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;

import java.util.Random;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        // Test population size > 0
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertNotNull(distribution);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());

        // Test population size <= 0
        populationSize = 0;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        // Test numberOfSuccesses < 0
        numberOfSuccesses = -1;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }

        // Test sampleSize < 0
        sampleSize = -1;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }

        // Test numberOfSuccesses > populationSize
        numberOfSuccesses = populationSize + 1;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        // Test sampleSize > populationSize
        sampleSize = populationSize + 1;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        Random random = new Random();
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);

        int[] domain = distribution.getDomain();

        // Test x < domain[0]
        int x = 0;
        double result = distribution.cumulativeProbability(x);
        assertEquals(0.0, result, 1e-9);

        // Test x >= domain[1]
        x = domain[1] + 1;
        result = distribution.cumulativeProbability(x);
        assertEquals(1.0, result, 1e-9);

        // Test x in domain
        x = domain[0];
        result = distribution.cumulativeProbability(x);
        assertTrue(result > 0.0 && result < 1.0);

        int xExpected = 0;
        for (int i = x; i <= domain[1]; i++) {
            xExpected += distribution.probability(i);
        }
        result = distribution.cumulativeProbability(x);
        assertEquals(xExpected, (int) result, 1);

        // Test random values
        for (int i = 0; i < 1000; i++) {
            int xSample = distribution.nextInt();
            int xTest = random.nextInt(domain[1] + 1);
            double resultSample = distribution.cumulativeProbability(xSample);
            double resultTest = distribution.cumulativeProbability(xTest);
            assertTrue(resultSample <= resultTest);
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        Random random = new Random();
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);

        int[] domain = distribution.getDomain();

        for (int i = domain[0]; i <= domain[1]; i++) {
            double result = distribution.probability(i);
            double p = (double) i / (double) domain[1];
            double q = (double) (domain[1] - i) / (double) domain[1];
            double expected = SaddlePointExpansion.logBinomialProbability(i, 5, p, q);
            double eps = 1e-8;
            assertEquals(expected, result, eps);
        }

        // Test x < domain[0]
        int x = 0;
        double result = distribution.probability(x);
        assertEquals(0.0, result, 1e-9);

        // Test x > domain[1]
        x = domain[1] + 1;
        result = distribution.probability(x);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = distribution.getNumericalVariance();
        double expectedVariance = (double) sampleSize * (double) numberOfSuccesses * (double) (populationSize - sampleSize) * (double) (populationSize - numberOfSuccesses) / (double) (populationSize * populationSize * (populationSize - 1));
        assertEquals(expectedVariance, numericalVariance, 1e-9);
    }
}
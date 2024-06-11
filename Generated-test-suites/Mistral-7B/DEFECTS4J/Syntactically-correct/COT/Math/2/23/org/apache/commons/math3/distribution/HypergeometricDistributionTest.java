package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import java.util.Random;

public class HypergeometricDistributionTest {

    @org.junit.Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Exception should not be thrown");
        }

        // Test negative population size
        try {
            new HypergeometricDistribution(-1, numberOfSuccesses, sampleSize);
            fail("Negative population size should cause exception");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test zero population size
        try {
            new HypergeometricDistribution(0, numberOfSuccesses, sampleSize);
            fail("Zero population size should cause exception");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test negative number of successes
        try {
            new HypergeometricDistribution(populationSize, -1, sampleSize);
            fail("Negative number of successes should cause exception");
        } catch (NotPositiveException e) {
            // Expected
        }

        // Test zero number of successes
        try {
            new HypergeometricDistribution(populationSize, 0, sampleSize);
            fail("Zero number of successes should cause exception");
        } catch (NotPositiveException e) {
            // Expected
        }

        // Test sample size larger than population size
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, populationSize + 1);
            fail("Sample size larger than population size should cause exception");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int i = 0; i <= sampleSize; i++) {
            double cp = dist.cumulativeProbability(i);
            double np = dist.probability(i);
            double sum = cp + np;
            double eps = 1e-12;
            assertTrue("Error in cumulative probabilities. i = " + i,
                       Math.abs(cp - np) <= eps || Math.abs(sum - 1.0) <= eps);
        }
    }

    @org.junit.Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int i = 0; i <= sampleSize; i++) {
            double p = dist.probability(i);
            double sum = 0.0;
            for (int j = 0; j <= sampleSize; j++) {
                sum += dist.cumulativeProbability(j);
            }
            assertTrue("Error in probabilities. i = " + i, Math.abs(sum - 1.0) <= 1e-12);
            assertEquals("Error in probability for i = " + i, p, dist.cumulativeProbability(i) - dist.cumulativeProbability(i - 1), 1e-12);
        }
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals("Error in number of successes", numberOfSuccesses, dist.getNumberOfSuccesses());
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals("Error in population size", populationSize, dist.getPopulationSize());
    }

    @org.junit.Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals("Error in sample size", sampleSize, dist.getSampleSize());
    }

    @org.junit.Test
    public void testNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double mean = (double) sampleSize * numberOfSuccesses / (double) populationSize;
        assertEquals("Error in numerical mean", mean, dist.getNumericalMean(), 1e-12);
    }

    @org.junit.Test
    public void testNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double variance = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        assertEquals("Error in numerical variance", variance, dist.getNumericalVariance(), 1e-12);
    }

    @org.junit.Test
    public void testRandomValues() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 10000;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        RandomGenerator rng = new Well19937c();
        Random random = new Random(rng);

        int[] values = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            values[i] = dist.sample();
        }

        int sum = 0;
        int successes = 0;
        for (int i = 0; i < sampleSize; i++) {
            sum += values[i];
            if (values[i] == 1) {
                successes++;
            }
        }

        double mean = (double) sum / sampleSize;
        double variance = ((double) sum * (sampleSize - sum) / (sampleSize * sampleSize) + (numberOfSuccesses * (populationSize - numberOfSuccesses) / (populationSize * populationSize * (populationSize - 1))));
        assertEquals("Error in mean", mean, dist.getNumericalMean(), 1e-6);
        assertEquals("Error in variance", variance, dist.getNumericalVariance(), 1e-6);
        assertEquals("Error in number of successes", numberOfSuccesses, successes);
    }
}
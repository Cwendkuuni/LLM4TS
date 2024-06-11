package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() throws Exception {
        new HypergeometricDistribution(10, -1, 5);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroOrNegativePopulationSize() throws Exception {
        new HypergeometricDistribution(0, 5, 5);
        new HypergeometricDistribution(-1, 5, 5);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSampleSize() throws Exception {
        new HypergeometricDistribution(10, 5, 0);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() throws Exception {
        new HypergeometricDistribution(10, 11, 5);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() throws Exception {
        new HypergeometricDistribution(10, 5, 11);
    }

    @org.junit.Test
    public void testCumulativeProbabilityWithValidX() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double cumulativeProbability = distribution.cumulativeProbability(3);
        assertEquals(cumulativeProbability, 0.1379310315343524, 0.0001);
    }

    @org.junit.Test
    public void testCumulativeProbabilityWithLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBound = distribution.getSupportLowerBound();
        double cumulativeProbability = distribution.cumulativeProbability(lowerBound);
        assertEquals(cumulativeProbability, 0.0, 0.0);
    }

    @org.junit.Test
    public void testCumulativeProbabilityWithUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperBound = distribution.getSupportUpperBound();
        double cumulativeProbability = distribution.cumulativeProbability(upperBound);
        assertEquals(cumulativeProbability, 1.0, 0.0);
    }

    @org.junit.Test
    public void testProbabilityWithValidX() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double probability = distribution.probability(3);
        assertEquals(probability, 0.1379310315343524, 0.0001);
    }

    @org.junit.Test
    public void testProbabilityWithLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBound = distribution.getSupportLowerBound();
        double probability = distribution.probability(lowerBound);
        assertEquals(probability, 0.0, 0.0);
    }

    @org.junit.Test
    public void testProbabilityWithUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperBound = distribution.getSupportUpperBound();
        double probability = distribution.probability(upperBound);
        assertEquals(probability, 0.1379310315343524, 0.0001);
    }

    @org.junit.Test
    public void testUpperCumulativeProbabilityWithValidX() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double upperCumulativeProbability = distribution.upperCumulativeProbability(3);
        assertEquals(1.0 - upperCumulativeProbability, 0.1379310315343524, 0.0001);
    }

    @org.junit.Test
    public void testUpperCumulativeProbabilityWithLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBound = distribution.getSupportLowerBound();
        double upperCumulativeProbability = distribution.upperCumulativeProbability(lowerBound);
        assertEquals(upperCumulativeProbability, 1.0, 0.0);
    }

    @org.junit.Test
    public void testUpperCumulativeProbabilityWithUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperBound = distribution.getSupportUpperBound();
        double upperCumulativeProbability = distribution.upperCumulativeProbability(upperBound);
        assertEquals(upperCumulativeProbability, 0.0, 0.0);
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int actualNumberOfSuccesses = distribution.getNumberOfSuccesses();
        assertEquals(actualNumberOfSuccesses, numberOfSuccesses);
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int actualPopulationSize = distribution.getPopulationSize();
        assertEquals(actualPopulationSize, populationSize);
    }

    @org.junit.Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int actualSampleSize = distribution.getSampleSize();
        assertEquals(actualSampleSize, sampleSize);
    }

    @org.junit.Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double numericalMean = distribution.getNumericalMean();
        assertEquals(numericalMean, 5.0, 0.0);
    }

    @org.junit.Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double numericalVariance = distribution.getNumericalVariance();
        assertEquals(numericalVariance, 0.03531156338535786, 0.0001);
    }

    @org.junit.Test
    public void testGenerateRandomNumber() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        int actualNumberOfSuccesses = 0;
        for (int i = 0; i < 1000; i++) {
            actualNumberOfSuccesses += distribution.nextInt();
        }
        double expectedNumberOfSuccesses = 5.0 * 1000.0 / 10.0;
        assertEquals(expectedNumberOfSuccesses, actualNumberOfSuccesses, 1);
    }
}
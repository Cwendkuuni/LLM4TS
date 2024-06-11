package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setup() throws NotPositiveException, NumberIsTooLargeException, NotStrictlyPositiveException {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 10;
        distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(50, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(100, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        assertEquals(10, distribution.getSampleSize());
    }

    @Test
    public void testGetNumericalMean() {
        double expectedMean = 5.0;
        assertEquals(expectedMean, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        double expectedVariance = 3.3333;
        assertEquals(expectedVariance, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(10, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testCumulativeProbability() {
        double expectedProbability = 0.9999999999;
        assertEquals(expectedProbability, distribution.cumulativeProbability(10), 0.0001);
    }

    @Test
    public void testProbability() {
        double expectedProbability = 0.0028281081;
        assertEquals(expectedProbability, distribution.probability(5), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        double expectedProbability = 0.0;
        assertEquals(expectedProbability, distribution.upperCumulativeProbability(0), 0.0001);
    }
}
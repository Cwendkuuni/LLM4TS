package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructHypergeometricDistribution() throws NotPositiveException,
            NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertNotNull(dist);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativePopulationSize() throws NotStrictlyPositiveException {
        int populationSize = -1;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() throws NotPositiveException {
        int populationSize = 3;
        int numberOfSuccesses = -1;
        int sampleSize = 4;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeSampleSize() throws NotPositiveException {
        int populationSize = 3;
        int numberOfSuccesses = 3;
        int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() throws NotPositiveException,
            NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 3;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() throws NotPositiveException,
            NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 3;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanNumberOfSuccesses() throws NotPositiveException,
            NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 3;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(3.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(0.0129, numericalVariance, 0.0001);
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 1;
        double p = dist.probability(x);
        assertEquals(0.2734375, p, 0.0001);

        x = 4;
        p = dist.probability(x);
        assertEquals(0.205078125, p, 0.0001);

        x = 6;
        p = dist.probability(x);
        assertEquals(0.12941176471e-7, p, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 1;
        double cdf = dist.cumulativeProbability(x);
        assertEquals(0.1765625, cdf, 0.0001);

        x = 4;
        cdf = dist.cumulativeProbability(x);
        assertEquals(0.62890625, cdf, 0.0001);

        x = 6;
        cdf = dist.cumulativeProbability(x);
        assertEquals(0.9453125, cdf, 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 1;
        double cdf = dist.upperCumulativeProbability(x);
        assertEquals(0.8234375, cdf, 0.0001);

        x = 4;
        cdf = dist.upperCumulativeProbability(x);
        assertEquals(0.37109375, cdf, 0.0001);

        x = 6;
        cdf = dist.upperCumulativeProbability(x);
        assertEquals(0.0546875, cdf, 0.0001);
    }

    @Test
    public void testGetDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = dist.getDomain();
        assertEquals(4, domain.length);
        assertEquals(3, domain[0]);
        assertEquals(4, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerDomain = dist.getSupportLowerBound();
        assertEquals(2, lowerDomain);
    }

    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperDomain = dist.getSupportUpperBound();
        assertEquals(4, upperDomain);
    }

    @Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertTrue(dist.isSupportConnected());
    }
}
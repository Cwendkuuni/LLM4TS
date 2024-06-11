package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Tests the {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Tests the creation of an F distribution with positive degrees of freedom.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @org.junit.Test
    public void testCreateFDistribution() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the creation of an F distribution with inverse cumulative probability accuracy.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @org.junit.Test
    public void testCreateFDistributionWithAccuracy() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        double inverseCumulativeProbabilityAccuracy = 0.001;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, inverseCumulativeProbabilityAccuracy);
        assertEquals(numeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(inverseCumulativeProbabilityAccuracy, fDistribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Tests the creation of an F distribution with a random number generator.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @org.junit.Test
    public void testCreateFDistributionWithRandomNumberGenerator() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        double inverseCumulativeProbabilityAccuracy = 0.001;
        RandomGenerator rng = new Well19937c();
        FDistribution fDistribution = new FDistribution(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, inverseCumulativeProbabilityAccuracy);
        assertEquals(numeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(inverseCumulativeProbabilityAccuracy, fDistribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Tests the calculation of the mean for an F distribution with denominator degrees of freedom > 2.
     */
    @org.junit.Test
    public void testCalculateNumericalMean() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double mean = fDistribution.getNumericalMean();
        double expectedMean = denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2);
        assertEquals(expectedMean, mean, 0.0001);
    }

    /**
     * Tests the calculation of the mean for an F distribution with denominator degrees of freedom <= 2.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCalculateNumericalMeanWithInvalidDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 1.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        fDistribution.getNumericalMean();
    }

    /**
     * Tests the calculation of the variance for an F distribution with denominator degrees of freedom > 4.
     */
    @org.junit.Test
    public void testCalculateNumericalVariance() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 6.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double variance = fDistribution.getNumericalVariance();
        double expectedVariance = (2 * (denominatorDegreesOfFreedom * denominatorDegreesOfFreedom) * (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) / ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) * (denominatorDegreesOfFreedom - 4)));
        assertEquals(expectedVariance, variance, 0.0001);
    }

    /**
     * Tests the calculation of the variance for an F distribution with denominator degrees of freedom <= 4.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCalculateNumericalVarianceWithInvalidDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 2.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        fDistribution.getNumericalVariance();
    }

    /**
     * Tests the density calculation for an F distribution.
     */
    @org.junit.Test
    public void testDensityCalculation() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expectedDensity = 0.1253457645543367;
        double density = fDistribution.density(1.5);
        assertEquals(expectedDensity, density, 0.0001);
    }

    /**
     * Tests the cumulative probability calculation for an F distribution.
     */
    @org.junit.Test
    public void testCumulativeProbabilityCalculation() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expectedCumulativeProbability = 0.932471521444665;
        double cumulativeProbability = fDistribution.cumulativeProbability(2.5);
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
    }

    /**
     * Tests the generation of random values from an F distribution.
     */
    @org.junit.Test
    public void testGenerateRandomValues() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        RandomGenerator rng = new Well19937c();
        int numberOfValuesToGenerate = 1000;
        double[] generatedValues = new double[numberOfValuesToGenerate];
        for (int i = 0; i < numberOfValuesToGenerate; i++) {
            generatedValues[i] = fDistribution.inverseCumulativeProbability(rng.nextDouble());
        }
        // check mean and variance are approximately correct
        double mean = 0.0;
        for (int i = 0; i < numberOfValuesToGenerate; i++) {
            mean += generatedValues[i];
        }
        mean /= numberOfValuesToGenerate;
        double variance = 0.0;
        for (int i = 0; i < numberOfValuesToGenerate; i++) {
            variance += Math.pow(generatedValues[i] - mean, 2);
        }
        variance /= numberOfValuesToGenerate;
        double expectedMean = denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2);
        double expectedVariance = (2 * (denominatorDegreesOfFreedom * denominatorDegreesOfFreedom) * (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) / ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) * (denominatorDegreesOfFreedom - 4)));
        assertEquals(expectedMean, mean, 0.01);
        assertEquals(expectedVariance, variance, 0.01);
    }
}
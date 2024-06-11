package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() throws NotStrictlyPositiveException {
        final int numTrials = 10000;
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;

        RandomGenerator rng = new Random();
        RealDistribution distribution = new FDistribution(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);

        for (int i = 0; i < numTrials; i++) {
            double x = distribution.inverseCumulativeProbability(new Random().nextDouble());
            assertTrue(x >= 0.0);
        }
    }

    @Test
    public void testFDistributionDensity() {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;

        double maxError = 0.0;
        for (double x = 0.0; x <= 5.0; x += 0.01) {
            double density = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom).density(x);
            double cumulativeProbability = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom).cumulativeProbability(x);
            double difference = Math.abs(density * cumulativeProbability);
            maxError = Math.max(maxError, difference);
        }

        System.out.println("Max error: " + maxError);
        assertTrue(maxError <= 1e-6);
    }

    @Test
    public void testFDistributionMean() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 3.5;

        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double mean = distribution.getNumericalMean();
        assertEquals(mean, 3.0 / 2.0, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-1.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeNumeratorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(1.0, -3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroNumeratorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 3.0);
    }

    @Test
    public void testFDistributionVariance() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 4.5;

        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double variance = distribution.getNumericalVariance();
        assertEquals(variance, 1.5 * (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2) /
                (numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) * (denominatorDegreesOfFreedom - 4)), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeVarianceDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(3.0, -2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroVarianceDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(3.0, 0.0);
    }

    @Test
    public void testFDistributionCumulativeProbability() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;

        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double cumulativeProbability = distribution.cumulativeProbability(x);
        assertEquals(cumulativeProbability, Beta.regularizedBeta((numeratorDegreesOfFreedom * x) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * x), 0.5 * numeratorDegreesOfFreedom, 0.5 * denominatorDegreesOfFreedom), 1e-6);
    }

    @Test
    public void testFDistributionDistributionFunction() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final int numTrials = 10000;

        RandomGenerator rng = new Random();
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        for (int i = 0; i < numTrials; i++) {
            double x = distribution.inverseCumulativeProbability(rng.nextDouble());
            double density = distribution.density(x);
            double observed = 0;
            for (int j = 0; j < 1000; j++) {
                observed += (distribution.cumulativeProbability(x + rng.nextGaussian(0.0, 1.0)) > observed) ? 1 : 0;
            }
            assertEquals(density, observed / 1000.0, 1e-3);
        }
    }
}
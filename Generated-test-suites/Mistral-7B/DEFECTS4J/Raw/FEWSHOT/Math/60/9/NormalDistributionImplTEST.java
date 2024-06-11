package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math3.SpecialFunctions;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testDefaultConstruction() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testConstructionWithMeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(2.5, dist.getMean(), 0.001);
        assertEquals(0.5, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testConstructionWithMeanStandardDeviationAndAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 0.5, 1e-6);
        assertEquals(2.5, dist.getMean(), 0.001);
        assertEquals(0.5, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveStandardDeviation() {
        new NormalDistributionImpl(-1.0, 0.5);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double density = dist.density(0.5);
        assertEquals(0.1129844523406843, density, 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double prob = dist.cumulativeProbability(0.5);
        assertEquals(0.6826894921373936, prob, 0.001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double sample = dist.sample();
        assertEquals(0.287185945, sample, 0.05);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.9;
        double q = dist.inverseCumulativeProbability(p);
        assertEquals(1.280666323954565, q, 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilitySmallProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.01;
        double q = dist.inverseCumulativeProbability(p);
        assertEquals(-1.958925195678266, q, 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilityLargeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.99;
        double q = dist.inverseCumulativeProbability(p);
        assertEquals(1.315235527072195, q, 0.001);
    }

    @Test
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        try {
            dist.inverseCumulativeProbability(-1.0);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("p must be between 0.0 and 1.0", e.getMessage());
        }

        try {
            dist.inverseCumulativeProbability(2.0);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("p must be between 0.0 and 1.0", e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbabilityMathException() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        try {
            dist.inverseCumulativeProbability(0.51);
        } catch (MathException e) {
            assertEquals("Failed to converge: root not found", e.getMessage());
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.1;
        double lowerBound = dist.getDomainLowerBound(p);
        assertEquals(-2.326347893884709, lowerBound, 0.01);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.9;
        double upperBound = dist.getDomainUpperBound(p);
        assertEquals(1.671322954388235, upperBound, 0.01);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.5;
        double initialDomain = dist.getInitialDomain(p);
        assertEquals(0.0, initialDomain, 0.01);
    }

    @Test
    public void testSampleMultipleTimes() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        int numSamples = 10000;
        double[] samples = new double[numSamples];
        for (int i = 0; i < numSamples; i++) {
            samples[i] = dist.sample();
        }
        double mean = 0.0;
        for (int i = 0; i < numSamples; i++) {
            mean += samples[i];
        }
        mean /= numSamples;
        double variance = 0.0;
        for (int i = 0; i < numSamples; i++) {
            variance += Math.pow(samples[i] - mean, 2.0);
        }
        variance /= numSamples;
        assertEquals(1.0, variance, 0.01);
    }

    @Test
    public void testSpecialFunctionValues() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841344746, dist.cumulativeProbability(1.0), 0.001);
        assertEquals(SpecialFunctions.ERF_INVERSE(0.5), dist.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(SpecialFunctions.ERF_INVERSE(0.841344746), dist.inverseCumulativeProbability(0.841344746), 0.001);
    }
}
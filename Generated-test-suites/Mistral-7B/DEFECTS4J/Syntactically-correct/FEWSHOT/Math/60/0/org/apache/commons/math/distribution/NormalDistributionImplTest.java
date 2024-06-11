package org.apache.commons.cli.test;

import static org.apache.commons.math3.util.Precision.abs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionCreation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);

        distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);

        distribution = new NormalDistributionImpl(0.0, 0.0);
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(0.0, distribution.getStandardDeviation(), 0.0);

        try {
            distribution = new NormalDistributionImpl(-1.0, 1.0);
            fail("Should have thrown NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            distribution = new NormalDistributionImpl(0.0, -1.0);
            fail("Should have thrown NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            double x = random.nextGaussian(0.0, 1.0);
            double density = distribution.density(x);
            double sample = distribution.sample();
            double probability = distribution.cumulativeProbability(sample);
            assertEquals(density, probability, 1e-9);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        double eps = 1e-8;
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double cdf = distribution.cumulativeProbability(x);
            double density = distribution.density(x);
            assertEquals(cdf, Precision.sum(cdf, density * eps), density * eps);
        }

        try {
            distribution.cumulativeProbability(-10.0);
            fail("Should have thrown MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
            fail("Should have thrown MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException, OutOfRangeException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        double eps = 1e-8;
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double inverseCdf = distribution.inverseCumulativeProbability(p);
            double cdf = distribution.cumulativeProbability(inverseCdf);
            assertEquals(p, cdf, eps);
        }

        try {
            distribution.inverseCumulativeProbability(-1.0);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            distribution.inverseCumulativeProbability(2.0);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            double sample1 = distribution.sample();
            double sample2 = distribution.sample();
            double density1 = distribution.density(sample1);
            double density2 = distribution.density(sample2);
            double probability1 = distribution.cumulativeProbability(sample1);
            double probability2 = distribution.cumulativeProbability(sample2);
            assertTrue(density1 >= 0.0 && density2 >= 0.0);
            assertTrue(abs(density1 - density2) < 1e-6);
            assertTrue(abs(probability1 - probability2) < 1e-6);
            assertTrue(probability1 + probability2 <= 1.0);
        }

        try {
            distribution.sample();
            fail("Should have thrown MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testRootFinding() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        double p = 0.5;
        double lowerBound = distribution.getDomainLowerBound(p);
        double upperBound = distribution.getDomainUpperBound(p);
        double initialDomain = distribution.getInitialDomain(p);

        double root = distribution.inverseCumulativeProbability(p);

        assertFalse(root < lowerBound);
        assertFalse(root > upperBound);
        assertFalse(abs(root - initialDomain) > 1e-6);
    }

    @Test
    public void testMaxIterationsExceededException() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        try {
            distribution.inverseCumulativeProbability(0.0);
        } catch (MaxIterationsExceededException e) {
            // Expected
            return;
        }

        fail("Should have thrown MaxIterationsExceededException");
    }
}
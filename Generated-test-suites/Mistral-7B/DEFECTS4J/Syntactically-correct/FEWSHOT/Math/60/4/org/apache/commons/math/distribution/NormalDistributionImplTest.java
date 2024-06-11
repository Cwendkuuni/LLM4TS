package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_MeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0.001);
        assertEquals(1, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_NegativeMeanAndNonNegativeStandardDeviation() {
        try {
            new NormalDistributionImpl(-1, 1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testNormalDistributionImpl_NegativeStandardDeviation() {
        try {
            new NormalDistributionImpl(0, -1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testNormalDistributionImpl_MeanAndStandardDeviation_CustomAccuracy() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1, 1e-12);
        assertEquals(0, distribution.getMean(), 1e-13);
        assertEquals(1, distribution.getStandardDeviation(), 1e-13);
    }

    @Test
    public void testDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double density = distribution.density(0);
        assertEquals(0.39894228040143271, density, 1e-8);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        assertEquals(0.5, distribution.cumulativeProbability(0), 1e-8);
        assertEquals(0.8413453131615648, distribution.cumulativeProbability(1), 1e-8);
        assertEquals(0, distribution.cumulativeProbability(-1), 1e-8);

        try {
            distribution.cumulativeProbability(3);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        assertEquals(-1.28212609436938968, distribution.inverseCumulativeProbability(0.1587), 1e-8);
        assertEquals(0, distribution.inverseCumulativeProbability(0), 1e-8);
        assertEquals(1.28212609436938968, distribution.inverseCumulativeProbability(0.8413), 1e-8);

        try {
            distribution.inverseCumulativeProbability(1.5);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        RandomDataGenerator rdg = new RandomDataGenerator();
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(Precision.isClose(sample, distribution.cumulativeProbability(rdg.nextDouble()), 1e-8));
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 1e-13);
    }
}
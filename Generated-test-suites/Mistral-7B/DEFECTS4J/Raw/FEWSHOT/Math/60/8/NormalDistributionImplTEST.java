package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.distribution.NormalDistribution;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testCreateNormalDistribution() {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);
        NormalDistribution normalDistMeanOne = new NormalDistributionImpl(1, 1);
        NormalDistribution normalDistMeanZeroSDTwo = new NormalDistributionImpl(0, 2);

        assertEquals(0.0, normalDist.getMean(), 0.001);
        assertEquals(1.0, normalDistMeanOne.getMean(), 0.001);
        assertEquals(2.0, normalDistMeanZeroSDTwo.getMean(), 0.001);
        assertEquals(1.0, normalDistMeanZeroSDTwo.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double density = normalDist.density(x);
            double expectedDensity = FastMath.exp(-0.5 * Math.pow(x, 2)) / (Math.sqrt(2 * Math.PI));

            assertEquals(expectedDensity, density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double cumulativeProbability = normalDist.cumulativeProbability(x);
            double expectedCumulativeProbability = 0.5 * (1.0 + Erf.erf(x / (Math.sqrt(2.0))));

            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double inverseCumulativeProbability = normalDist.inverseCumulativeProbability(p);
            double expectedInverseCumulativeProbability = normalDist.getMean() + p * normalDist.getStandardDeviation();

            assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbability, 0.001);
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);
        double expectedValue;
        double actualValue;

        for (int i = 0; i < 10; i++) {
            expectedValue = normalDist.sample();
            actualValue = normalDist.sample();
            assertEquals(expectedValue, actualValue, 0.001);
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        assertEquals(1e-9, normalDist.getSolverAbsoluteAccuracy(), 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double domainLowerBound = normalDist.getDomainLowerBound(p);

            if (p < 0.5) {
                assertEquals(-Double.MAX_VALUE, domainLowerBound, 0.0);
            } else {
                assertEquals(normalDist.getMean(), domainLowerBound, 0.001);
            }
        }
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double domainUpperBound = normalDist.getDomainUpperBound(p);

            if (p < 0.5) {
                assertEquals(normalDist.getMean(), domainUpperBound, 0.001);
            } else {
                assertEquals(Double.MAX_VALUE, domainUpperBound, 0.0);
            }
        }
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);

        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double initialDomain = normalDist.getInitialDomain(p);

            if (p < 0.5) {
                assertTrue(initialDomain < normalDist.getMean());
            } else if (p > 0.5) {
                assertTrue(initialDomain > normalDist.getMean());
            } else {
                assertEquals(normalDist.getMean(), initialDomain, 0.001);
            }
        }
    }

    @Test
    public void testSampleMultipleTimes() throws MathException {
        NormalDistribution normalDist = new NormalDistributionImpl(0, 1);
        double[] expectedValues = new double[1000];

        for (int i = 0; i < 1000; i++) {
            expectedValues[i] = normalDist.sample();
        }

        double[] actualValues = new double[1000];

        for (int i = 0; i < 1000; i++) {
            actualValues[i] = normalDist.sample();
        }

        assertArrayEquals(expectedValues, actualValues, 0.001);
    }

    @Test
    public void testSampleMultipleDistributions() throws MathException {
        NormalDistribution normalDist1 = new NormalDistributionImpl(0, 1);
        NormalDistribution normalDist2 = new NormalDistributionImpl(1, 2);
        double[] expectedValues1 = new double[1000];
        double[] expectedValues2 = new double[1000];
        double[] actualValues1 = new double[1000];
        double[] actualValues2 = new double[1000];

        for (int i = 0; i < 1000; i++) {
            expectedValues1[i] = normalDist1.sample();
            expectedValues2[i] = normalDist2.sample();
        }

        for (int i = 0; i < 1000; i++) {
            actualValues1[i] = normalDist1.sample();
            actualValues2[i] = normalDist2.sample();
        }

        assertArrayEquals(expectedValues1, actualValues1, 0.001);
        assertArrayEquals(expectedValues2, actualValues2, 0.001);

        // Check if samples from different distributions are distinct
        for (int i = 0; i < 1000; i++) {
            for (int j = i + 1; j < 1000; j++) {
                if (expectedValues1[i] == expectedValues1[j]) {
                    fail("Sample from distribution 1 is not distinct: " + expectedValues1[i]);
                }
                if (expectedValues2[i] == expectedValues2[j]) {
                    fail("Sample from distribution 2 is not distinct: " + expectedValues2[i]);
                }
                if (expectedValues1[i] == expectedValues2[j]) {
                    fail("Samples from different distributions are the same: " + expectedValues1[i]);
                }
                if (expectedValues1[j] == expectedValues2[i]) {
                    fail("Samples from different distributions are the same: " + expectedValues1[j]);
                }
            }
        }
    }
}
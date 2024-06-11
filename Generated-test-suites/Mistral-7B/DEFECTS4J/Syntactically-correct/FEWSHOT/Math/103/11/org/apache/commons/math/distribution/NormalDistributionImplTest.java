package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);

        NormalDistribution distWithMean = new NormalDistributionImpl(1.5, 0.5);
        assertEquals(1.5, distWithMean.getMean(), 0.001);
        assertEquals(0.5, distWithMean.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        for (double x = -5; x <= 5; x += 0.1) {
            double prob = dist.cumulativeProbability(x);
            double expectedProb = 0.5 * (1.0 + Erf.erf(x / (Math.sqrt(2.0) * 1.0)));
            assertEquals(expectedProb, prob, 0.001);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expectedX = dist.getMean() + p * dist.getStandardDeviation();
            assertEquals(expectedX, x, 0.01);
        }

        try {
            dist.inverseCumulativeProbability(-0.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.inverseCumulativeProbability(1.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double lowerBound = dist.getDomainLowerBound(p);
            if (p < 0.5) {
                assertTrue(Double.isNegativeFinite(lowerBound));
            } else {
                assertEquals(0.0, lowerBound, 0.001);
            }
        }
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double upperBound = dist.getDomainUpperBound(p);
            if (p < 0.5) {
                assertEquals(0.0, upperBound, 0.001);
            } else {
                assertTrue(Double.isPositiveFinite(upperBound));
            }
        }
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double initialDomain = dist.getInitialDomain(p);
            if (p < 0.5) {
                double lowerBound = dist.getDomainLowerBound(p);
                double expectedInitialDomain = (lowerBound + getMean()) / 2.0;
                assertEquals(expectedInitialDomain, initialDomain, 0.01);
            } else if (p > 0.5) {
                double upperBound = dist.getDomainUpperBound(p);
                double expectedInitialDomain = (getMean() + upperBound) / 2.0;
                assertEquals(expectedInitialDomain, initialDomain, 0.01);
            } else {
                assertEquals(getMean(), initialDomain, 0.01);
            }
        }
    }
}
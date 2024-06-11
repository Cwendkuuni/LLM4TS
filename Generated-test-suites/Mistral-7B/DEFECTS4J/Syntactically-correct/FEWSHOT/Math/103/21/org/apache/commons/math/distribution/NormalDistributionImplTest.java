package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);

        NormalDistributionImpl distributionWithMeanAndStdDev = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(2.5, distributionWithMeanAndStdDev.getMean(), 0.001);
        assertEquals(1.5, distributionWithMeanAndStdDev.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();

        for (double x = -10.0; x <= 10.0; x += 0.1) {
            double p = distribution.cumulativeProbability(x);
            assertEquals(0.5 + Erf.erf(x / (Math.sqrt(2.0) * distribution.getStandardDeviation())), p, 0.001);
        }

        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Double.POSITIVE_INFINITY));
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Double.NEGATIVE_INFINITY));
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(20.0 * distribution.getStandardDeviation() + distribution.getMean()));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            assertEquals(Erf.erfinv(2 * p - 1), x, 0.001);
        }

        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(-0.1));
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(1.1));
    }

    @Test
    public void testMeanAndStandardDeviationAccessors() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();

        distribution.setMean(2.5);
        assertEquals(2.5, distribution.getMean(), 0.001);

        distribution.setStandardDeviation(1.5);
        assertEquals(1.5, distribution.getStandardDeviation(), 0.001);

        assertThrows(IllegalArgumentException.class, () -> distribution.setStandardDeviation(-1.5));
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();

        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double lowerBound = distribution.getDomainLowerBound(p);
            if (p < 0.5) {
                assertEquals(-Double.MAX_VALUE, lowerBound, 0.0);
            } else {
                assertEquals(distribution.getMean(), lowerBound, 0.001);
            }
        }
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();

        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double upperBound = distribution.getDomainUpperBound(p);
            if (p < 0.5) {
                assertEquals(distribution.getMean(), upperBound, 0.001);
            } else {
                assertEquals(Double.MAX_VALUE, upperBound);
            }
        }
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();

        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double initialDomain = distribution.getInitialDomain(p);
            if (p < 0.5) {
                double mean = distribution.getMean();
                double stdDev = distribution.getStandardDeviation();
                assertEquals(mean - stdDev, initialDomain, 0.001);
            } else if (p > 0.5) {
                double mean = distribution.getMean();
                double stdDev = distribution.getStandardDeviation();
                assertEquals(mean + stdDev, initialDomain, 0.001);
            } else {
                assertEquals(mean, initialDomain, 0.001);
            }
        }
    }

}
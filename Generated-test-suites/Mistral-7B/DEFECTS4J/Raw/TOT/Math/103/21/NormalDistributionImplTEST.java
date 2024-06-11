package org.apache.commons.math.distribution.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testCreation() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(NormalDistribution.DIMENSION, dist.getSupportSize());
        assertEquals(Double.class, dist.getSupportElementType());
        assertEquals(0.0, dist.getMean(), 1e-6);
        assertEquals(1.0, dist.getStandardDeviation(), 1e-6);

        NormalDistribution distMeanZeroSDOne = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(NormalDistribution.DIMENSION, distMeanZeroSDOne.getSupportSize());
        assertEquals(Double.class, distMeanZeroSDOne.getSupportElementType());
        assertEquals(0.0, distMeanZeroSDOne.getMean(), 1e-6);
        assertEquals(1.0, distMeanZeroSDOne.getStandardDeviation(), 1e-6);

        NormalDistribution distMeanOneSDOne = new NormalDistributionImpl(1.0, 1.0);
        assertEquals(NormalDistribution.DIMENSION, distMeanOneSDOne.getSupportSize());
        assertEquals(Double.class, distMeanOneSDOne.getSupportElementType());
        assertEquals(1.0, distMeanOneSDOne.getMean(), 1e-6);
        assertEquals(1.0, distMeanOneSDOne.getStandardDeviation(), 1e-6);

        NormalDistribution distMeanNegOneSDTwo = new NormalDistributionImpl(-1.0, 2.0);
        assertEquals(NormalDistribution.DIMENSION, distMeanNegOneSDTwo.getSupportSize());
        assertEquals(Double.class, distMeanNegOneSDTwo.getSupportElementType());
        assertEquals(-1.0, distMeanNegOneSDTwo.getMean(), 1e-6);
        assertEquals(2.0, distMeanNegOneSDTwo.getStandardDeviation(), 1e-6);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();

        for (double x = -10; x <= 10; x += 0.1) {
            double p = dist.cumulativeProbability(x);
            double expectedCDF = 0.5 * (1 + Erf.erf((x - dist.getMean()) / (dist.getStandardDeviation() * Math.sqrt(2.0))));
            assertEquals(expectedCDF, p, 1e-6);
        }

        // Test extreme values
        assertEquals(0.0, dist.cumulativeProbability(Double.NEGATIVE_INFINITY), 1e-6);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 1e-6);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.cumulativeProbability(Double.POSITIVE_INFINITY + 1);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expectedQuantile = dist.getMean() + dist.getStandardDeviation() * Erf.erfInverse(2 * p - 1);
            assertEquals(expectedQuantile, x, 1e-6);
        }

        // Test extreme values
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 1e-6);
    }

    @Test
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.inverseCumulativeProbability(-0.1);
    }

    @Test
    public void testStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        StandardDeviation stdDev = new StandardDeviation();
        double[] values = new double[1000];
        for (int i = 0; i < 1000; i++) {
            values[i] = dist.inverseCumulativeProbability(i / 1000.0);
        }
        double calculatedStdDev = stdDev.evaluate(values);
        assertEquals(dist.getStandardDeviation(), calculatedStdDev, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }
}
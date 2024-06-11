package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        // Test default constructor
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);

        // Test constructor with mean and standard deviation
        NormalDistributionImpl distributionWithParams = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(2.5, distributionWithParams.getMean(), 0.001);
        assertEquals(0.5, distributionWithParams.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        for (double x = -5; x <= 5; x += 0.1) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            double expectedCumulativeProbability = 0.5 * (1 + Erf.erf((x - distribution.getMean()) / (distribution.getStandardDeviation() * Math.sqrt(2.0))));
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
            double expectedInverseCumulativeProbability = getInverseCumulativeProbability(p, distribution.getMean(), distribution.getStandardDeviation());
            assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbability, 0.001);
        }
    }

    @Test
    public void testSetMeanAndStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.5);
        distribution.setStandardDeviation(1.2);
        assertEquals(3.5, distribution.getMean(), 0.001);
        assertEquals(1.2, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.2);
    }

    private double getInverseCumulativeProbability(double p, double mean, double standardDeviation) {
        double z = Math.sqrt(2.0) * Erf.erfInv(2 * p - 1.0);
        return mean + z * standardDeviation;
    }
}
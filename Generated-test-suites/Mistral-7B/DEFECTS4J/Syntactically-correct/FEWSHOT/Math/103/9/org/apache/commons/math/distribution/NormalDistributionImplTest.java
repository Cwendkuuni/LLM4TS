package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        NormalDistributionImpl distMeanOne = new NormalDistributionImpl(1.0, 1.0);
        assertEquals(1.0, distMeanOne.getMean(), 0.0);
        assertEquals(1.0, distMeanOne.getStandardDeviation(), 0.0);

        NormalDistributionImpl distMeanNegOneSDOne = new NormalDistributionImpl(-1.0, 1.0);
        assertEquals(-1.0, distMeanNegOneSDOne.getMean(), 0.0);
        assertEquals(1.0, distMeanNegOneSDOne.getStandardDeviation(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();

        // Test values within 3 standard deviations of mean
        for (int i = -3; i <= 3; i++) {
            double x = i;
            double cdf = dist.cumulativeProbability(x);
            double expectedCdf = (1.0 + Erf.erf((x - dist.getMean()) / (dist.getStandardDeviation() * Math.sqrt(2.0)))) / 2.0;
            assertEquals(expectedCdf, cdf, 0.001);
        }

        // Test values more than 3 standard deviations from mean
        double x = 5;
        double cdf = dist.cumulativeProbability(x);
        assertTrue(cdf <= 0.001 || cdf >= 0.999);

        x = -5;
        cdf = dist.cumulativeProbability(x);
        assertTrue(cdf <= 0.001 || cdf >= 0.999);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();

        // Test extreme probabilities
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0);

        // Test probabilities within mean +-3 standard deviations
        double p = 0.1;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(-1.282126, x, 0.001);

        p = 0.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.0);

        p = 0.9;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(1.282126, x, 0.001);

        // Test probabilities outside mean +-3 standard deviations
        p = 0.0;
        x = dist.inverseCumulativeProbability(p);
        assertTrue(Double.isNaN(x));

        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertTrue(Double.isNaN(x));
    }
}
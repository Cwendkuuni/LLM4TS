package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i < 10; i++) {
            double probability = dist.probability(i);
            double expected = MathUtils.factorial(i) / Math.pow(Math.E, 1.0) / Math.pow(Math.E, 1.0 * (1.0 + i));
            assertEquals(expected, probability, 1e-10);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        double[] xValues = new double[]{0.0, 0.5, 1.0, 1.5, 2.0, 3.0, 4.0, 5.0, 10.0};
        double[] expectedValues = new double[]{0.0, 0.36787944117144233, 0.3680241326219631, 0.47123891308974875, 0.5403023058687552, 0.6231251822712639, 0.6756234686955415, 0.8318658757942176, 0.999442339547147};
        for (int i = 0; i < xValues.length; i++) {
            double value = dist.cumulativeProbability(i);
            assertEquals(expectedValues[i], value, 1e-12);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        double[] xValues = new double[]{0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5};
        NormalDistribution normalDist = new NormalDistributionImpl(1.0, Math.sqrt(1.0));
        for (int i = 0; i < xValues.length; i++) {
            double value = dist.normalApproximateProbability((int) xValues[i]);
            double expectedValue = normalDist.cumulativeProbability(xValues[i] + 0.5);
            assertEquals(expectedValue, value, 1e-12);
        }
    }

    @Test
    public void testMean() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, dist.getMean(), 1e-10);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        int samples[] = new int[10];
        for (int i = 0; i < 10; i++) {
            samples[i] = dist.sample();
        }
        StandardDeviation stdDev = new StandardDeviation();
        double mean = 0.0;
        for (int i = 0; i < 10; i++) {
            mean += samples[i];
        }
        mean /= 10.0;
        double expected = 1.0;
        assertEquals(expected, mean, 1e-10);
        double variance = 1.0;
        for (int i = 0; i < 10; i++) {
            variance += Math.pow(samples[i] - mean, 2);
        }
        variance /= 9.0;
        double stdDevValue = Math.sqrt(variance);
        assertEquals(stdDevValue, Precision.doubleValue(stdDev.evaluate(samples)), 1e-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        new PoissonDistributionImpl(-1.0);
    }
}
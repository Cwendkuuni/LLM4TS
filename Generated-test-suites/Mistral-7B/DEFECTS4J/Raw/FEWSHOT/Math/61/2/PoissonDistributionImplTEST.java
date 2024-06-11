package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.PoissonDistribution;
import org.apache.commons.math.special.Gamma;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double probability = distribution.probability(i);
            double expected = Gamma.regularizedGammaP(i + 1, 2.5);
            assertEquals("Error in probability calculation for x = " + i, expected, probability, 0.001);
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testProbabilityWithNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = distribution.cumulativeProbability(i);
            double expected = Gamma.regularizedGammaQ(i + 1, 2.5);
            assertEquals("Error in cumulative probability calculation for x = " + i, expected, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(5.0);
        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = distribution.normalApproximateProbability(i);
            double probability = distribution.probability(i);
            double error = Math.abs(probability - normalApproximateProbability);
            double expectedError = Math.exp(-0.5 * Math.pow(5.0 - i, 2)) * Math.sqrt(2.0 / (i * Math.PI));
            assertEquals("Error in normal approximation probability calculation for x = " + i, expectedError, error, 0.01);
        }
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        int expected = randomData.nextInt(1000 * 2.5);
        assertEquals("Error in sample calculation", expected, sample);
    }
}
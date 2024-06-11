package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(1.5);
        for (int i = 0; i < 10; i++) {
            double prob = poisson.probability(i);
            double expected = Gamma.regularizedGammaP(i + 1, 1.5);
            assertEquals("Unexpected probability for i = " + i, expected, prob, 1e-5);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(1.5);
        double[] expected = new double[]{0.0, 0.10032113211321132, 0.2169968193387135, 0.3299833720360521, 0.442141575281696, 0.553623723788933, 0.6647403143430355, 0.7755513440937904, 0.886026814736532, 0.9969464648563486};
        for (int i = 0; i < 10; i++) {
            double actual = poisson.cumulativeProbability(i);
            assertEquals("Unexpected cumulative probability for i = " + i, expected[i], actual, 1e-5);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(1.5);
        NormalDistribution normal = new NormalDistributionImpl(1.5, Math.sqrt(1.5));
        for (int i = 0; i < 10; i++) {
            double actual = poisson.normalApproximateProbability(i);
            double expected = normal.cumulativeProbability(i + 0.5);
            assertEquals("Unexpected normal approximation probability for i = " + i, expected, actual, 1e-5);
        }
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(1.5);
        for (int i = 0; i < 10; i++) {
            int actual = poisson.sample();
            int expected = (int) Math.round(poisson.cumulativeProbability(actual));
            assertEquals("Unexpected sample value", expected, actual);
        }
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(1.5);
        assertEquals("Unexpected mean", 1.5, poisson.getMean(), 1e-5);
    }
}
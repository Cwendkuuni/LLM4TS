package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerator;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionConstruction() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        assertNotNull(pd);
        assertEquals(1.0, pd.getMean(), 0.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testPoissonDistributionNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbability() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 5; i++) {
            double prob = pd.probability(i);
            assertEquals(FastMath.exp(-1.0) * FastMath.pow(FastMath.E, i) / MathUtils.SQRT_2_PI, prob, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double[] expected = {0.0, 0.0498, 0.1356, 0.2391, 0.3679, 0.4913, 0.6054, 0.6989, 0.7713, 0.8215, 0.8551, 0.8756, 0.8894, 0.8967, 0.9014, 0.9043, 0.9061, 0.9074, 0.9083, 0.9089, 0.9093, 0.9095, 0.9096, 0.9097, 0.9097, 0.9097, 0.9097, 0.9097, 0.9097, 0.9097, 0.9096, 0.9095, 0.9093, 0.9090, 0.9085, 0.9076, 0.9065, 0.9051, 0.9033, 0.9011, 0.8985, 0.8954, 0.8920, 0.8882, 0.8840, 0.8794, 0.8744, 0.8690, 0.8632, 0.8570, 0.8504, 0.8434, 0.8360, 0.8282, 0.8199, 0.8112, 0.8021, 0.7926, 0.7827, 0.7724, 0.7618, 0.7507, 0.7392, 0.7273, 0.7150, 0.7023, 0.6892, 0.6757, 0.6619, 0.6477, 0.6332, 0.6183, 0.6031, 0.5876, 0.5717, 0.5555, 0.5390, 0.5217, 0.5039, 0.4857, 0.4671, 0.4479, 0.4282, 0.4079, 0.3869, 0.3653, 0.3431, 0.3195, 0.2942, 0.2673, 0.2390, 0.2089, 0.1766, 0.1423, 0.1057, 0.0668, 0.0256, 0.0000};
        for (int i = 0; i <= 30; i++) {
            double cumProb = pd.cumulativeProbability(i);
            assertEquals(Gamma.regularizedGammaQ(i + 1, 1.0), cumProb, Precision.EPSILON);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        pd.cumulativeProbability(-1);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        NormalDistribution normal = new NormalDistributionImpl(1.0, 1.0);
        for (int i = 0; i <= 10; i++) {
            double normalApprox = pd.normalApproximateProbability(i);
            double actual = normal.cumulativeProbability(i + 0.5);
            assertEquals(actual, normalApprox, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityOutOfRange() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        pd.normalApproximateProbability(-1);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        RandomDataGenerator rdg = new RandomDataGenerator();
        for (int i = 0; i < 100; i++) {
            int sample = pd.sample();
            assertEquals(sample, pd.sample(), 0);
        }
    }

    @Test
    public void testSampleLargerMean() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(10.0);
        RandomDataGenerator rdg = new RandomDataGenerator();
        for (int i = 0; i < 100; i++) {
            int sample = pd.sample();
            assertEquals(0, sample % 10);
            assertEquals(10, pd.mean());
        }
    }

    @Test
    public void testSampleSmallMean() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(0.5);
        RandomDataGenerator rdg = new RandomDataGenerator();
        for (int i = 0; i < 100; i++) {
            int sample = pd.sample();
            assertEquals(sample, pd.sample(), 0);
            assertEquals(sample, Math.round(Math.random()), 0);
        }
    }
}
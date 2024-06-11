package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.distribution.PoissonDistribution;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathRuntimeException;
import org.apache.commons.math.special.Gamma;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.random.RandomDataGenerators;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RunWith(Parameterized.class)
public class PoissonDistributionImplTest {
    private double mean;
    private PoissonDistribution distribution;

    @Parameterized.Parameters(name = "{index}: mean={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0.1},
                {3.5},
                {-1.0},
                {5.2},
                {12.3},
                {Long.MAX_VALUE}
        });
    }

    public PoissonDistributionImplTest(double mean) {
        this.mean = mean;
    }

    @Test
    public void testConstructor() {
        try {
            distribution = new PoissonDistributionImpl(mean);
        } catch (MathRuntimeException e) {
            if (mean <= 0) {
                assertEquals(LocalizedFormats.NOT_POSITIVE_POISSON_MEAN, e.getMessage());
            } else {
                throw e;
            }
        }
    }

    @Test
    public void testProbability() {
        distribution = new PoissonDistributionImpl(mean);

        int[] xValues = {-1, 0, 1, 2, 3, Integer.MAX_VALUE};
        double[] expectedProbabilities = new double[]{0.0, FastMath.exp(-mean), FastMath.exp(-mean + Mean.EulerNumber[mean]), FastMath.exp(-mean + Mean.EulerNumber[mean + 1]) / FastMath.sqrt(MathUtils.TWO_PI * (mean + 1)), FastMath.ONE};

        for (int i = 0; i < xValues.length; i++) {
            double prob = distribution.probability(xValues[i]);
            assertEquals(expectedProbabilities[i], prob, 1e-9);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        distribution = new PoissonDistributionImpl(mean);

        int[] xValues = {-1, 0, 1, 2, 3, Integer.MAX_VALUE};
        double[] expectedCumulativeProbabilities = new double[]{0.0, FastMath.exp(-mean), FastMath.exp(-mean) + FastMath.exp(-mean + Mean.EulerNumber[mean]), FastMath.exp(-mean) + FastMath.exp(-mean + Mean.EulerNumber[mean]) + FastMath.exp(-mean + Mean.EulerNumber[mean + 1]) / MathUtils.SQRT_2_PI, 1.0};

        for (int i = 0; i < xValues.length; i++) {
            double cumProb = distribution.cumulativeProbability(xValues[i]);
            assertEquals(expectedCumulativeProbabilities[i], cumProb, 1e-9);
        }

        Random rng = new Random();
        double[] samples = new double[10000];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = distribution.sample();
        }
        double cumulativeProbability = 0.0;
        for (int sample : samples) {
            cumulativeProbability += distribution.probability(sample);
        }
        assertTrue(Math.abs(1.0 - cumulativeProbability) < 1e-6);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        distribution = new PoissonDistributionImpl(mean);

        int[] xValues = {-1, 0, 1, 2, 3, Integer.MAX_VALUE};
        double[] expectedNormalApproxProbabilities = new double[xValues.length];
        NormalDistribution normal = new NormalDistributionImpl(mean, Math.sqrt(mean));

        for (int i = 0; i < xValues.length; i++) {
            double prob = distribution.normalApproximateProbability(xValues[i]);
            expectedNormalApproxProbabilities[i] = normal.cumulativeProbability(xValues[i] + 0.5);
        }

        assertArrayEquals(expectedNormalApproxProbabilities, new double[xValues.length], 1e-9);
    }

    @Test
    public void testSample() throws MathException {
        distribution = new PoissonDistributionImpl(mean);

        int sample;
        if (mean < 20) {
            sample = RandomDataGenerators.nextInt(new Random(), (int) (mean * 10));
        } else {
            sample = distribution.sample();
        }

        assertEquals(mean, distribution.getMean());
        assertEquals(sample, distribution.sample());
    }

    @Test(expected = MathRuntimeException.class)
    public void testSampleWithIllegalMean() throws MathException {
        distribution = new PoissonDistributionImpl(-1.0);
        distribution.sample();
    }
}
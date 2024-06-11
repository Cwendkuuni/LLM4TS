package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Random;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with positive mean.
     */
    @Test
    public void testConstructionPositiveMean() {
        final double mean = 2.5;
        PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, distribution.getMean(), 0.0);
    }

    /**
     * Test constructor with negative mean.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructionNegativeMean() {
        final double mean = -1.5;
        new PoissonDistributionImpl(mean);
    }

    /**
     * Test {@code probability} method.
     */
    @Test
    public void testProbability() {
        final double mean = 3.0;
        PoissonDistribution distribution = new PoissonDistributionImpl(mean);

        double expectedProbability = FastMath.exp(-mean) / FastMath.sqrt(2 * Math.PI * mean);

        for (int i = 0; i <= 10; i++) {
            double probability = distribution.probability(i);
            assertEquals(expectedProbability * Math.pow(FastMath.E, i - mean), probability, 1e-3);
        }
    }

    /**
     * Test {@code cumulativeProbability} method with valid argument.
     */
    @Test
    public void testCumulativeProbabilityValidArgument() throws MathException {
        final double mean = 5.0;
        PoissonDistribution distribution = new PoissonDistributionImpl(mean);

        double expectedCumulativeProbability = Gamma.regularizedGammaP(1.0, mean, PoissonDistributionImpl.DEFAULT_EPSILON, PoissonDistributionImpl.DEFAULT_MAX_ITERATIONS);

        double actualCumulativeProbability = distribution.cumulativeProbability(0);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-6);

        for (int i = 1; i <= 10; i++) {
            expectedCumulativeProbability += distribution.probability(i);
            actualCumulativeProbability = distribution.cumulativeProbability(i);
            assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-6);
        }
    }

    /**
     * Test {@code cumulativeProbability} method with invalid argument (negative number).
     */
    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbabilityInvalidArgumentNegativeNumber() throws MathException {
        final double mean = 5.0;
        PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        distribution.cumulativeProbability(-1);
    }

    /**
     * Test {@code normalApproximateProbability} method.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        final double mean = 5.0;
        PoissonDistribution distribution = new PoissonDistributionImpl(mean);

        Random random = new Random();

        for (int i = 0; i <= 10; i++) {
            double probability = distribution.normalApproximateProbability(i);
            double expectedProbability = normalApproximateProbability(mean, i);
            assertEquals(expectedProbability, probability, 1e-4);
            assertEquals(probability, distribution.probability(i), 1e-4);
            // check that the sample generated from the distribution matches the approximated probability
            int sample = distribution.sample(random);
            assertEquals(i, sample);
        }
    }

    /**
     * Helper method for {@code normalApproximateProbability} test.
     *
     * @param mean Poisson mean
     * @param x integer value
     * @return probability of normal approximation
     */
    private static double normalApproximateProbability(double mean, int x) {
        NormalDistribution normal = new NormalDistributionImpl(mean, Math.sqrt(mean));
        return normal.cumulativeProbability(x + 0.5);
    }

    /**
     * Test {@code sample} method.
     */
    @Test
    public void testSample() throws MathException {
        final double mean = 3.0;
        PoissonDistribution distribution = new PoissonDistributionImpl(mean);

        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int sample = distribution.sample(random);
            assertEquals(Math.round(mean), sample);
        }
    }
}
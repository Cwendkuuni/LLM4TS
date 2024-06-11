package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test the constructor with valid input.
     */
    @Test
    public void testValidConstruction() {
        final double mean = 3.0;
        PoissonDistribution poissonDist = new PoissonDistributionImpl(mean);
    }

    /**
     * Test the constructor with negative mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testInvalidConstructionNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    /**
     * Test {@code probability(x)} method with valid input.
     */
    @Test
    public void testProbability() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);

        for (int x = 0; x <= 5; x++) {
            double p = poissonDist.probability(x);
            double expected = Gamma.regularizedGammaP(x + 1, 1.0);
            assertEquals("Unexpected probability for x = " + x, expected, p, Precision.EPSILON);
        }
    }

    /**
     * Test {@code probability(x)} method with negative x.
     */
    @Test
    public void testProbabilityNegativeX() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);

        double p = poissonDist.probability(-1);
        assertEquals("Unexpected probability for negative x", 0.0, p, Precision.EPSILON);
    }

    /**
     * Test {@code probability(x)} method with x equal to {@code Integer.MAX_VALUE}.
     */
    @Test
    public void testProbabilityMaxValue() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);

        double p = poissonDist.probability(Integer.MAX_VALUE);
        assertEquals("Unexpected probability for maximum value", 1.0, p, Precision.EPSILON);
    }

    /**
     * Test {@code cumulativeProbability(x)} method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValid() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);

        for (int x = 0; x <= 5; x++) {
            double p = poissonDist.cumulativeProbability(x);
            double expected = Gamma.regularizedGammaQ(x + 1, 1.0);
            assertEquals("Unexpected cumulative probability for x = " + x, expected, p, Precision.EPSILON);
        }
    }

    /**
     * Test {@code cumulativeProbability(x)} method with negative x.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCumulativeProbabilityNegativeX() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);

        poissonDist.cumulativeProbability(-1);
    }

    /**
     * Test {@code cumulativeProbability(x)} method with x equal to {@code Integer.MAX_VALUE}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCumulativeProbabilityMaxValue() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);

        poissonDist.cumulativeProbability(Integer.MAX_VALUE);
    }

    /**
     * Test {@code normalApproximateProbability(x)} method with valid input.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);

        for (int x = 0; x <= 5; x++) {
            double p = poissonDist.normalApproximateProbability(x);
            double expected = FastMath.erf(FastMath.sqrt(1.0) * (x - 1.0)) / 2.0;
            assertEquals("Unexpected normal approximation probability for x = " + x, expected, p, 0.001);
        }
    }

    /**
     * Test {@code normalApproximateProbability(x)} method with negative x.
     */
    @Test
    public void testNormalApproximateProbabilityNegativeX() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);

        double p = poissonDist.normalApproximateProbability(-1);
        assertEquals("Unexpected normal approximation probability for negative x", 0.0, p, Precision.EPSILON);
    }

    /**
     * Test {@code normalApproximateProbability(x)} method with x equal to {@code Integer.MAX_VALUE}.
     */
    @Test
    public void testNormalApproximateProbabilityMaxValue() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);

        double p = poissonDist.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals("Unexpected normal approximation probability for maximum value", 1.0, p, Precision.EPSILON);
    }

    /**
     * Test {@code sample()} method with valid input.
     */
    @Test
    public void testSampleValid() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.0);
        RandomGenerator generator = new TestingRandomGenerator();
        poissonDist.setseed(generator);

        for (int i = 0; i < 100; i++) {
            int sample = poissonDist.sample();
            assertEquals("Unexpected sample", i, sample);
        }
    }

    /**
     * Test {@code sample()} method with invalid input (non-positive mean).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSampleInvalid() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(-1.0);
        RandomGenerator generator = new TestingRandomGenerator();
        poissonDist.setseed(generator);
        poissonDist.sample();
    }
}
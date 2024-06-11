package org.apache.commons.math.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class PoissonDistributionImplTest {

    /** The constant epsilon. */
    private static final double EPSILON = 1e-12;

    /** The constant max iterations. */
    private static final int MAX_ITERATIONS = 10;

    /** The constant mean. */
    private static final double MEAN = 3.0;

    /** The constant mean max. */
    private static final double MEAN_MAX = 100.0;

    /** The constant mean min. */
    private static final double MEAN_MIN = 0.1;

    /** The constant variance. */
    private static final double VARIANCE = MEAN;

    /** The random. */
    private Random random;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        random = new Random();
    }

    /**
     * Tests constructor PoissonDistributionImpl.
     *
     * @param p the p
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgumentException(final double p) {
        new PoissonDistributionImpl(p);
    }

    /**
     * Tests constructor PoissonDistributionImpl with mean.
     */
    @Test
    public void testConstructorMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN);
        assertEquals(MEAN, distribution.getMean(), EPSILON);
    }

    /**
     * Tests constructor PoissonDistributionImpl with mean, epsilon, max iterations.
     */
    @Test
    public void testConstructorMeanEpsilonMaxIterations() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN, EPSILON, MAX_ITERATIONS);
        assertEquals(MEAN, distribution.getMean(), EPSILON);
        assertEquals(EPSILON, distribution.epsilon, EPSILON);
        assertEquals(MAX_ITERATIONS, distribution.maxIterations, EPSILON);
    }

    /**
     * Tests constructor PoissonDistributionImpl with mean and epsilon.
     */
    @Test
    public void testConstructorMeanEpsilon() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN, EPSILON);
        assertEquals(MEAN, distribution.getMean(), EPSILON);
        assertEquals(EPSILON, distribution.epsilon, EPSILON);
        assertEquals(DEFAULT_MAX_ITERATIONS, distribution.maxIterations, EPSILON);
    }

    /**
     * Tests constructor PoissonDistributionImpl with mean and max iterations.
     */
    @Test
    public void testConstructorMeanMaxIterations() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN, MAX_ITERATIONS);
        assertEquals(MEAN, distribution.getMean(), EPSILON);
        assertEquals(DEFAULT_EPSILON, distribution.epsilon, EPSILON);
        assertEquals(MAX_ITERATIONS, distribution.maxIterations, EPSILON);
    }

    /**
     * Tests method probability.
     */
    @Test
    public void testMethodProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN);
        for (int x = 0; x <= 10; x++) {
            double probability = distribution.probability(x);
            double normalApproximateProbability = 0.0;
            if (x > 0) {
                NormalDistribution normal = new NormalDistributionImpl(MEAN, Math.sqrt(VARIANCE));
                normalApproximateProbability = normal.probability(x);
            }
            assertEquals(Precision.round(probability, 12), normalApproximateProbability, EPSILON);
        }
    }

    /**
     * Tests method cumulativeProbability.
     */
    @Test
    public void testMethodCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN);
        double[] expected = new double[] { 0.0, 0.0498588366136, 0.1203134583095, 0.2162154928758,
                0.3118918361482, 0.4068772552599, 0.4973743840219, 0.5916719319423, 0.6900224233348,
                0.7898806274669, 0.8831264932817, 0.9777242363205, 1.0 };
        double[] actual = new double[expected.length];
        for (int i = 0; i < expected.length; i++) {
            actual[i] = distribution.cumulativeProbability(i);
        }
        assertArrayEquals(expected, actual, EPSILON);
    }

    /**
     * Tests method normalApproximateProbability.
     */
    @Test
    public void testMethodNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN);
        double[] expected = new double[] { 0.0498588366136, 0.1203134583095, 0.1806351620673, 0.2368211577698,
                0.2861265366537, 0.3301116276774, 0.3703746477668, 0.4075127173605, 0.4420849902017,
                0.4746531367551, 0.5066179563886, 0.5384863612743, 0.5716004754412, 0.6061938436981,
                0.6423193162611, 0.6803212442201, 0.7197626208767, 0.7615278622828, 0.8064977879887,
                0.8548211636724, 0.9075770628227, 0.9653355779541, 1.0 };
        double[] actual = new double[expected.length];
        for (int i = 0; i < expected.length; i++) {
            actual[i] = distribution.normalApproximateProbability(i);
        }
        assertArrayEquals(expected, actual, EPSILON);
    }

    /**
     * Tests method sample.
     */
    @Test
    public void testMethodSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN);
        for (int i = 0; i < 10; i++) {
            int sample = distribution.sample();
            assertTrue(sample >= 0 && sample <= 1000 * MEAN);
        }
    }

    /**
     * Tests method inverseCumulativeProbability.
     */
    @Test
    public void testMethodInverseCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN);
        double[] expected = new double[] { 0.0, 0.0498588366136, 0.1203134583095, 0.1806351620673,
                0.2368211577698, 0.2861265366537, 0.3118918361482, 0.3563913935836, 0.3970331148134,
                0.4364872869304, 0.4757614114802, 0.5160633537228, 0.5583197315944, 0.5997884440549,
                0.6434022858537, 0.6894870816538, 0.739335406445, 0.7943202812683, 0.8556805611157,
                0.9191662579814, 0.9817680837839, 1.0 };
        double[] actual = new double[expected.length];
        for (int i = 0; i < expected.length; i++) {
            actual[i] = distribution.inverseCumulativeProbability(i / 100.0);
        }
        assertArrayEquals(expected, actual, EPSILON);
    }

    /**
     * Tests method getDomainLowerBound.
     */
    @Test
    public void testMethodGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN);
        assertEquals(0, distribution.getDomainLowerBound(0.0));
    }

    /**
     * Tests method getDomainUpperBound.
     */
    @Test
    public void testMethodGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1.0));
    }

    /**
     * Tests method getDomainLowerBound for negative mean.
     */
    @Test(expected = IllegalStateException.class)
    public void testMethodGetDomainLowerBoundNegativeMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(-MEAN_MIN);
        distribution.getDomainLowerBound(0.0);
    }

    /**
     * Tests method getDomainUpperBound for negative mean.
     */
    @Test(expected = IllegalStateException.class)
    public void testMethodGetDomainUpperBoundNegativeMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(-MEAN_MIN);
        distribution.getDomainUpperBound(0.0);
    }

    /**
     * Tests method getDomainLowerBound for mean equals to zero.
     */
    @Test
    public void testMethodGetDomainLowerBoundMeanZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.0);
        assertEquals(0, distribution.getDomainLowerBound(0.0));
    }

    /**
     * Tests method getDomainUpperBound for mean equals to zero.
     */
    @Test
    public void testMethodGetDomainUpperBoundMeanZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0));
    }

    /**
     * Tests method getDomainLowerBound for mean equals to one.
     */
    @Test
    public void testMethodGetDomainLowerBoundMeanOne() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0, distribution.getDomainLowerBound(0.0));
    }

    /**
     * Tests method getDomainUpperBound for mean equals to one.
     */
    @Test
    public void testMethodGetDomainUpperBoundMeanOne() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0));
    }

    /**
     * Tests method getDomainLowerBound for mean equals to maximum.
     */
    @Test
    public void testMethodGetDomainLowerBoundMeanMax() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN_MAX);
        assertEquals(0, distribution.getDomainLowerBound(0.0));
    }

    /**
     * Tests method getDomainUpperBound for mean equals to maximum.
     */
    @Test
    public void testMethodGetDomainUpperBoundMeanMax() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN_MAX);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0));
    }

    /**
     * Tests method getDomainLowerBound for mean equals to minimum.
     */
    @Test
    public void testMethodGetDomainLowerBoundMeanMin() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN_MIN);
        assertEquals(0, distribution.getDomainLowerBound(0.0));
    }

    /**
     * Tests method getDomainUpperBound for mean equals to minimum.
     */
    @Test
    public void testMethodGetDomainUpperBoundMeanMin() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(MEAN_MIN);
        assertEquals(0, distribution.getDomainUpperBound(0.0));
    }

    /**
     * Tests method getDomainLowerBound for mean equals to negative minimum.
     */
    @Test(expected = IllegalStateException.class)
    public void testMethodGetDomainLowerBoundMeanNegativeMin() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(-MEAN_MIN);
        distribution.getDomainLowerBound(0.0);
    }

    /**
     * Tests method getDomainUpperBound for mean equals to negative minimum.
     */
    @Test(expected = IllegalStateException.class)
    public void testMethodGetDomainUpperBoundMeanNegativeMin() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(-MEAN_MIN);
        distribution.getDomainUpperBound(0.0);
    }
}
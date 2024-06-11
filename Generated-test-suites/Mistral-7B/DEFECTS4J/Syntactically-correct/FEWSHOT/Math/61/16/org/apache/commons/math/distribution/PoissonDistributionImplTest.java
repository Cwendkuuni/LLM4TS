package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math.distribution.PoissonDistribution;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathUnsupportedOperationException;
import org.apache.commons.math.special.Gamma;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private static final double EPSILON = 1.0E-12;
    private static final int MAX_ITERATIONS = 10000000;
    private static final double MEAN = 2.5;
    private static final double MEAN_ZERO = 0.0;
    private static final double MEAN_NEGATIVE = -1.5;
    private static final int X_MINUS_ONE = 0;
    private static final int X_PLUS_ONE = 1;
    private static final int X_MAX_VALUE = Integer.MAX_VALUE;

    private PoissonDistributionImpl poissonDistribution;
    private NormalDistribution normal;
    private Random random;

    @Before
    public void setUp() {
        this.poissonDistribution = new PoissonDistributionImpl(MEAN);
        this.normal = new NormalDistributionImpl(MEAN, FastMath.sqrt(MEAN));
        this.random = new Random();
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(MEAN_NEGATIVE);
    }

    @Test
    public void testConstructor() {
        assertNotNull(poissonDistribution.getMean());
        assertEquals(MEAN, poissonDistribution.getMean(), EPSILON);
    }

    @Test
    public void testProbabilityZero() {
        assertEquals(FastMath.exp(-MEAN), poissonDistribution.probability(X_MINUS_ONE), EPSILON);
    }

    @Test
    public void testProbabilityOne() {
        assertEquals(FastMath.exp(-MEAN) + FastMath.exp(-MEAN) / FastMath.sqrt(2 * FastMath.PI * MEAN),
                poissonDistribution.probability(X_PLUS_ONE), EPSILON);
    }

    @Test
    public void testProbabilityGreaterThanZero() {
        int x = 10;
        double probability = poissonDistribution.probability(x);
        assertTrue(probability > 0);
    }

    @Test
    public void testProbabilityGreaterThanMaxValue() {
        int x = Integer.MAX_VALUE;
        double probability = poissonDistribution.probability(x);
        assertEquals(1.0, probability, EPSILON);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        assertEquals(0.0, poissonDistribution.cumulativeProbability(X_MINUS_ONE), EPSILON);
    }

    @Test
    public void testCumulativeProbabilityOne() throws MathException {
        assertEquals(FastMath.exp(-MEAN), poissonDistribution.cumulativeProbability(X_PLUS_ONE), EPSILON);
    }

    @Test
    public void testCumulativeProbabilityGreaterThanOne() throws MathException {
        int x = 20;
        double cumulativeProbability = poissonDistribution.cumulativeProbability(x);
        assertTrue(cumulativeProbability > FastMath.exp(-MEAN));
    }

    @Test
    public void testCumulativeProbabilityGreaterThanMaxValue() throws MathException {
        assertEquals(1.0, poissonDistribution.cumulativeProbability(X_MAX_VALUE), EPSILON);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        assertEquals(FastMath.exp(-MEAN), normal.cumulativeProbability(X_MINUS_ONE), EPSILON);
    }

    @Test
    public void testNormalApproximateProbabilityOne() throws MathException {
        assertEquals(FastMath.exp(-MEAN) + FastMath.exp(-MEAN) / FastMath.sqrt(2 * FastMath.PI * MEAN),
                normal.cumulativeProbability(X_PLUS_ONE), EPSILON);
    }

    @Test
    public void testNormalApproximateProbabilityGreaterThanZero() throws MathException {
        int x = 10;
        double normalApproximateProbability = normal.cumulativeProbability(x + 0.5);
        assertTrue(normalApproximateProbability > 0.5);
    }

    @Test
    public void testNormalApproximateProbabilityGreaterThanMaxValue() throws MathException {
        assertEquals(1.0, normal.cumulativeProbability(Double.MAX_VALUE), EPSILON);
    }

    @Test
    public void testSampleZero() throws MathException {
        assertEquals(X_MINUS_ONE, poissonDistribution.sample());
    }

    @Test
    public void testSampleOne() throws MathException {
        assertEquals(X_PLUS_ONE, poissonDistribution.sample());
    }

    @Test
    public void testSampleGreaterThanZero() throws MathException {
        int x = 10;
        int sampledValue = poissonDistribution.sample();
        assertTrue(sampledValue > 0);
        assertTrue(sampledValue <= x);
    }

    @Test
    public void testSampleGreaterThanMaxValue() throws MathException {
        assertFalse(poissonDistribution.sample() > X_MAX_VALUE);
    }

    @Test(expected = MathUnsupportedOperationException.class)
    public void testInverseCumulativeProbabilityNegativeProbability() {
        poissonDistribution.inverseCumulativeProbability(-1.0);
    }

    @Test(expected = MathUnsupportedOperationException.class)
    public void testInverseCumulativeProbabilityZeroProbability() {
        poissonDistribution.inverseCumulativeProbability(0.0);
    }

    @Test(expected = MathUnsupportedOperationException.class)
    public void testInverseCumulativeProbabilityOneProbability() {
        poissonDistribution.inverseCumulativeProbability(1.0);
    }

    @Test
    public void testInverseCumulativeProbabilityProbabilityGreaterThanZero() throws MathException {
        double probability = 0.5;
        int result = poissonDistribution.inverseCumulativeProbability(probability);
        assertTrue(result > 0);
        assertEquals(result, poissonDistribution.cumulativeProbability(result), EPSILON);
    }

    @Test
    public void testInverseCumulativeProbabilityProbabilityGreaterThanOne() throws MathException {
        double probability = 1.5;
        assertEquals(X_PLUS_ONE, poissonDistribution.inverseCumulativeProbability(probability));
    }

    @Test
    public void testInverseCumulativeProbabilityProbabilityGreaterThanMaxValue() throws MathException {
        double probability = 1.5 + MEAN;
        assertEquals(X_MAX_VALUE, poissonDistribution.inverseCumulativeProbability(probability));
    }

    @Test(expected = MathUnsupportedOperationException.class)
    public void testInverseCumulativeProbabilityProbabilityGreaterThanMaxValuePlusOne() throws MathException {
        double probability = 1.5 + MEAN + 1.0;
        poissonDistribution.inverseCumulativeProbability(probability);
    }

    @Test
    public void testCombineWithSameMean() {
        PoissonDistributionImpl poissonDistribution1 = new PoissonDistributionImpl(MEAN);
        PoissonDistributionImpl poissonDistribution2 = new PoissonDistributionImpl(MEAN);
        PoissonDistribution combinedDistribution = new PoissonDistribution(poissonDistribution1, poissonDistribution2);
        assertEquals(MEAN, combinedDistribution.getMean(), EPSILON);
    }

    @Test
    public void testCombineWithDifferentMeans() {
        PoissonDistributionImpl poissonDistribution1 = new PoissonDistributionImpl(MEAN);
        PoissonDistributionImpl poissonDistribution2 = new PoissonDistributionImpl(MEAN + 1.0);
        PoissonDistribution combinedDistribution = new PoissonDistribution(poissonDistribution1, poissonDistribution2);
        assertEquals(MEAN + 1.0, combinedDistribution.getMean(), EPSILON);
    }

    @Test
    public void testCombineWithDifferentDistributions() {
        PoissonDistributionImpl poissonDistribution1 = new PoissonDistributionImpl(MEAN);
        PoissonDistributionImpl poissonDistribution2 = new PoissonDistributionImpl(MEAN_ZERO);
        PoissonDistribution combinedDistribution = new PoissonDistribution(poissonDistribution1, poissonDistribution2);
        assertNull(combinedDistribution.getMean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombineWithNullDistribution() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(MEAN);
        PoissonDistribution nullDistribution = null;
        new PoissonDistribution(poissonDistribution, nullDistribution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombineWithSelf() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(MEAN);
        new PoissonDistribution(poissonDistribution, poissonDistribution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombineWithSelfNegativeMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(MEAN_NEGATIVE);
        new PoissonDistribution(poissonDistribution, poissonDistribution);
    }

    @Test
    public void testCombineWithDifferentMaxIterations() {
        PoissonDistributionImpl poissonDistribution1 = new PoissonDistributionImpl(MEAN, MAX_ITERATIONS / 2);
        PoissonDistributionImpl poissonDistribution2 = new PoissonDistributionImpl(MEAN, MAX_ITERATIONS * 2);
        PoissonDistribution combinedDistribution = new PoissonDistribution(poissonDistribution1, poissonDistribution2);
        assertEquals(MAX_ITERATIONS, combinedDistribution.getMaxIterations(), EPSILON);
    }

    @Test
    public void testCombineWithDifferentEpsilons() {
        PoissonDistributionImpl poissonDistribution1 = new PoissonDistributionImpl(MEAN, EPSILON / 2);
        PoissonDistributionImpl poissonDistribution2 = new PoissonDistributionImpl(MEAN, EPSILON * 2);
        PoissonDistribution combinedDistribution = new PoissonDistribution(poissonDistribution1, poissonDistribution2);
        assertEquals(EPSILON, combinedDistribution.getEpsilon(), EPSILON);
    }

    @Test
    public void testCombineWithSameEpsilons() {
        PoissonDistributionImpl poissonDistribution1 = new PoissonDistributionImpl(MEAN, EPSILON);
        PoissonDistributionImpl poissonDistribution2 = new PoissonDistributionImpl(MEAN, EPSILON);
        PoissonDistribution combinedDistribution = new PoissonDistribution(poissonDistribution1, poissonDistribution2);
        assertEquals(EPSILON, combinedDistribution.getEpsilon(), EPSILON);
    }
}
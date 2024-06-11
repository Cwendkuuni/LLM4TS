package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

public class PoissonDistributionImplTest {
    @Test
    public void testConstructorWithPositiveMean() {
        final double p = 1.25;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(p);
        assertEquals(p, dist.getMean(), Precision.SAFE_EPSILON);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        final double p = -0.5;
        new PoissonDistributionImpl(p);
    }

    @Test
    public void testProbabilityZero() {
        final PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        assertEquals(0.606512098693253, dist.probability(0), Precision.SAFE_EPSILON);
    }

    @Test
    public void testProbabilityGreaterThanZero() {
        final PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        final double probability = dist.probability(1);
        assertTrue(probability > 0.356888407566527);
        assertTrue(probability < 0.623262914641882);
    }

    @Test
    public void testProbabilityNegativeOrMaxIntegerValue() {
        final PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        assertEquals(0.0, dist.probability(-1), Precision.SAFE_EPSILON);
        assertEquals(0.0, dist.probability(Integer.MAX_VALUE), Precision.SAFE_EPSILON);
    }

    @Test
    public void testProbabilityWithNormalApproximation() throws MathException {
        final double p = 0.5;
        final PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(p);
        final NormalDistribution normalDist = new NormalDistributionImpl(p, Math.sqrt(p));
        for (int x = 0; x <= 10; x++) {
            double probPoisson = poissonDist.probability(x);
            double probNormal = normalDist.cumulativeProbability(x + 0.5);
            assertEquals(probNormal, probPoisson, Precision.SAFE_EPSILON);
        }
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        final PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        assertEquals(0.0, dist.cumulativeProbability(0), Precision.SAFE_EPSILON);
    }

    @Test
    public void testCumulativeProbabilityGreaterThanZero() throws MathException {
        final PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        double cdf = dist.cumulativeProbability(1);
        assertTrue(cdf > 0.356888407566527);
        assertTrue(cdf < 0.623262914641882);
    }

    @Test
    public void testCumulativeProbabilityLessThanZero() throws MathException {
        final PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        assertEquals(0.0, dist.cumulativeProbability(-1), Precision.SAFE_EPSILON);
    }

    @Test
    public void testCumulativeProbabilityMaxIntegerValue() throws MathException {
        final PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), Precision.SAFE_EPSILON);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithNegativeX() throws MathException {
        final PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        dist.cumulativeProbability(-1);
    }

    @Test
    public void testSample() throws MathException {
        final PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i < 100; i++) {
            int sample = dist.sample();
            assertTrue(sample >= 0);
            assertTrue(sample <= 100);
            assertTrue(dist.probability(sample) > 0);
        }
    }
}
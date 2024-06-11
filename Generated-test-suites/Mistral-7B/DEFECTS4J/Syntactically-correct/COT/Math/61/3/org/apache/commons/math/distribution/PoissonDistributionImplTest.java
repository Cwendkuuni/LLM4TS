package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertNotNull(distribution);
        assertEquals(1.0, distribution.getMean(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMeanConstructor() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.2400985571646, distribution.probability(0), 1e-6);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        for (int i = 0; i < 10; i++) {
            double prob = distribution.probability(i);
            double expected = FastMath.exp(-1.0) * Gamma.gamma(i + 1) / FastMath.sqrt(FastMath.TWO_PI * i);
            assertEquals(expected, prob, 1e-6);
        }
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.24800976546284, distribution.cumulativeProbability(0), 1e-6);
    }

    @Test
    public void testCumulativeProbabilityException() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.81784693828491, distribution.cumulativeProbability(2), 1e-6);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        for (int i = 0; i < 10; i++) {
            double prob = distribution.normalApproximateProbability(i);
            double expected = FastMath.erf(FastMath.SQRT12 * i) / 2.0;
            assertEquals(expected, prob, 1e-6);
        }
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        for (int i = 0; i < 10; i++) {
            int sample = distribution.sample();
            assertEquals(i, sample);
        }
    }

    @Test
    public void testSampleNegative() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        int sample = distribution.sample();
        assertFalse(sample < 0);
    }

    @Test
    public void testSampleLargerThanMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        int sample = distribution.sample();
        assertTrue(Precision.isClose(sample, 1.0 * distribution.getMean(), 1e-6));
        sample = distribution.sample();
        assertTrue(sample > 1);
    }
}
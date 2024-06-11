package org.apache.commons.math.distribution;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(3.5, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.13534, distribution.probability(2), 0.0001);
        assertEquals(0.27067, distribution.probability(3), 0.0001);
        assertEquals(0.09022, distribution.probability(4), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.42319, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.42319, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.57680, distribution.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(0.28969, distribution.normalApproximateProbability(6), 0.0001);
        assertEquals(0.98927, distribution.normalApproximateProbability(8), 0.0001);
        assertEquals(0.99990, distribution.normalApproximateProbability(15), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        int sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}
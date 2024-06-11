package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PoissonDistributionImplTest {

    private final double EPSILON = 1E-12;

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, distribution.getMean(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMean_IllegalArgumentException() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.2240418076553878, distribution.probability(2), EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.42319008112684364, distribution.cumulativeProbability(2), EPSILON);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.3694413401817648, distribution.normalApproximateProbability(2), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        int sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0, distribution.getDomainLowerBound(0.0), EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0), EPSILON);
    }
}
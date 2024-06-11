package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PoissonDistributionImplTest {
    private static final double EPSILON = 1E-9;

    @Test
    public void testGetMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, dist.getMean(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-5.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(0.03368973499542734, dist.probability(10), EPSILON);
        assertEquals(0.00306566200976202, dist.probability(20), EPSILON);
        assertEquals(6.16085146418488E-7, dist.probability(30), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(0.7052111317686456, dist.cumulativeProbability(10), EPSILON);
        assertEquals(0.916541571961731, dist.cumulativeProbability(20), EPSILON);
        assertEquals(0.9984784823942979, dist.cumulativeProbability(30), EPSILON);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(0.7398031022736341, dist.normalApproximateProbability(10), EPSILON);
        assertEquals(0.961261695938319, dist.normalApproximateProbability(20), EPSILON);
        assertEquals(0.9999037514451281, dist.normalApproximateProbability(30), EPSILON);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        int sample = dist.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(0, dist.getDomainLowerBound(0.0), EPSILON);
        assertEquals(0, dist.getDomainLowerBound(0.5), EPSILON);
        assertEquals(0, dist.getDomainLowerBound(1.0), EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.0), EPSILON);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.5), EPSILON);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(1.0), EPSILON);
    }
}
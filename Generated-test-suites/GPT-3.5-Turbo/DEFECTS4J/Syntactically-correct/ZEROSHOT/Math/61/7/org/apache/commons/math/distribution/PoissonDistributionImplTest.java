package org.apache.commons.math.distribution;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new PoissonDistributionImpl(3.0);
    }

    @Test
    public void testGetMean() {
        assertEquals(3.0, distribution.getMean(), 0.00001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.22404, distribution.probability(3), 0.00001);
        assertEquals(0.01490, distribution.probability(5), 0.00001);
        assertEquals(0.04656, distribution.probability(2), 0.00001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.35277, distribution.cumulativeProbability(3), 0.00001);
        assertEquals(0.88886, distribution.cumulativeProbability(5), 0.00001);
        assertEquals(0.19914, distribution.cumulativeProbability(2), 0.00001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.57306, distribution.normalApproximateProbability(3), 0.00001);
        assertEquals(0.88858, distribution.normalApproximateProbability(5), 0.00001);
        assertEquals(0.50000, distribution.normalApproximateProbability(2), 0.00001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, distribution.getDomainLowerBound(0.0), 0.00001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1.0), 0.00001);
    }

}
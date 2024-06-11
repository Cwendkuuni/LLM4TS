package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class PoissonDistributionImplTest {
    private PoissonDistributionImpl poisson;

    @Before
    public void setUp() {
        poisson = new PoissonDistributionImpl(3.5);
    }

    @After
    public void tearDown() {
        poisson = null;
    }
    
    @Test
    public void testGetMean() {
        assertEquals(3.5, poisson.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0304, poisson.probability(2), 0.0001);
        assertEquals(0.1054, poisson.probability(4), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0803, poisson.cumulativeProbability(2), 0.0001);
        assertEquals(0.1841, poisson.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.8228, poisson.normalApproximateProbability(2), 0.0001);
        assertEquals(0.2644, poisson.normalApproximateProbability(4), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = poisson.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, poisson.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5));
    }
}
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    private PoissonDistributionImpl poisson;

    @Before
    public void setUp() {
        poisson = new PoissonDistributionImpl(1.5);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(1.5, poisson.getMean(), 0.0);
    }

    @Test
    public void testProbability() {
        assertEquals(0.22313016014842982, poisson.probability(0), 1e-15);
        assertEquals(0.3346952402226444, poisson.probability(1), 1e-15);
        assertEquals(0.25102193016698326, poisson.probability(2), 1e-15);
        assertEquals(0.12551096508349163, poisson.probability(3), 1e-15);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.22313016014842982, poisson.cumulativeProbability(0), 1e-15);
        assertEquals(0.5578254003710743, poisson.cumulativeProbability(1), 1e-15);
        assertEquals(0.8088473305380576, poisson.cumulativeProbability(2), 1e-15);
        assertEquals(0.9343582956215492, poisson.cumulativeProbability(3), 1e-15);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.19146246127401317, poisson.normalApproximateProbability(0), 1e-15);
        assertEquals(0.5328072073424562, poisson.normalApproximateProbability(1), 1e-15);
        assertEquals(0.8028329260048262, poisson.normalApproximateProbability(2), 1e-15);
        assertEquals(0.9363291775690445, poisson.normalApproximateProbability(3), 1e-15);
    }

    @Test
    public void testSample() throws MathException {
        int sample = poisson.sample();
        assertTrue(sample >= 0 && sample < Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, poisson.getDomainLowerBound(0.0), 0.0);
        assertEquals(0, poisson.getDomainLowerBound(0.5), 0.0);
        assertEquals(0, poisson.getDomainLowerBound(1.0), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.0), 0.0);
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5), 0.0);
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(1.0), 0.0);
    }
}
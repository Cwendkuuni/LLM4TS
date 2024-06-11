package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    private PoissonDistributionImpl dist;
    
    @Before
    public void setUp() throws Exception {
        dist = new PoissonDistributionImpl(2.5);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(2.5, dist.getMean(), 0);
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.082085, dist.probability(0), 1e-6);
        assertEquals(0.205213, dist.probability(1), 1e-6);
        assertEquals(0.256516, dist.probability(2), 1e-6);
        assertEquals(0.213763, dist.probability(3), 1e-6);
        assertEquals(0.133601, dist.probability(4), 1e-6);
    }
    
    @Test
    public void testProbability_InvalidInput() {
        assertEquals(0, dist.probability(-1), 0);
        assertEquals(0, dist.probability(Integer.MAX_VALUE), 0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.082085, dist.cumulativeProbability(0), 1e-6);
        assertEquals(0.287298, dist.cumulativeProbability(1), 1e-6);
        assertEquals(0.543814, dist.cumulativeProbability(2), 1e-6);
        assertEquals(0.757577, dist.cumulativeProbability(3), 1e-6);
        assertEquals(0.891178, dist.cumulativeProbability(4), 1e-6);
    }
    
    @Test
    public void testCumulativeProbability_InvalidInput() throws MathException {
        assertEquals(0, dist.cumulativeProbability(-1), 0);
        assertEquals(1, dist.cumulativeProbability(Integer.MAX_VALUE), 0);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbability_Error() throws MathException {
        dist.cumulativeProbability(100000000000L);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.080301, dist.normalApproximateProbability(0), 1e-6);
        assertEquals(0.267165, dist.normalApproximateProbability(1), 1e-6);
        assertEquals(0.554298, dist.normalApproximateProbability(2), 1e-6);
        assertEquals(0.797668, dist.normalApproximateProbability(3), 1e-6);
        assertEquals(0.931383, dist.normalApproximateProbability(4), 1e-6);
    }
    
    @Test
    public void testSample() throws MathException {
        int[] values = new int[10000];
        for (int i = 0; i < values.length; i++) {
            values[i] = dist.sample();
        }
        double mean = 0;
        for (int value : values) {
            mean += value;
        }
        mean /= values.length;
        assertTrue(mean >= 2.4 && mean <= 2.6);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, dist.getDomainLowerBound(0.1), 0);
        assertEquals(0, dist.getDomainLowerBound(0.5), 0);
        assertEquals(0, dist.getDomainLowerBound(0.9), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.1), 0);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.5), 0);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.9), 0);
    }
    
    @Test
    public void testConstructor() {
        try {
            new PoissonDistributionImpl(-2.5);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
}
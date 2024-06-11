package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }
    
    @Test
    public void testSetMean() {
        normalDistribution.setMean(1);
        assertEquals(1, normalDistribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.1), 0);
        assertEquals(0, normalDistribution.getDomainLowerBound(0.6), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0, normalDistribution.getDomainUpperBound(0.1), 0);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.6), 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistribution.getInitialDomain(0.1), 0);
        assertEquals(1, normalDistribution.getInitialDomain(0.6), 0);
        assertEquals(0, normalDistribution.getInitialDomain(0.5), 0);
    }
}
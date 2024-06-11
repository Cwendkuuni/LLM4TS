package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private static final double DELTA = 1e-15;
    
    private static NormalDistributionImpl normalDistribution;
    
    @BeforeClass
    public static void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), DELTA);
    }
    
    @Test
    public void testSetMean() {
        normalDistribution.setMean(2.5);
        assertEquals(2.5, normalDistribution.getMean(), DELTA);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double result = normalDistribution.cumulativeProbability(1.0);
        assertEquals(0.8413447460685429, result, DELTA);
    }
    
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability() throws MathException {
        normalDistribution.inverseCumulativeProbability(2.0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double result = normalDistribution.getDomainLowerBound(0.3);
        assertEquals(-1.7976931348623157E308, result, DELTA);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double result = normalDistribution.getDomainUpperBound(0.3);
        assertEquals(0.0, result, DELTA);
    }
    
    @Test
    public void testGetInitialDomain() {
        double result = normalDistribution.getInitialDomain(0.3);
        assertEquals(-0.6834942360905733, result, DELTA);
    }
}
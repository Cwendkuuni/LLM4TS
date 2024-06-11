package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private NormalDistributionImpl normalDistribution;
    
    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0);
    }
    
    @Test
    public void testSetMean() {
        normalDistribution.setMean(1.0);
        assertEquals(1.0, normalDistribution.getMean(), 0.0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        normalDistribution.setStandardDeviation(-1.0);
    }
    
    @Test
    public void testCumulativeProbabilityPositiveX() throws MathException {
        double result = normalDistribution.cumulativeProbability(1.0);
        assertTrue(result > 0.0 && result < 1.0);
    }
    
    @Test
    public void testCumulativeProbabilityNegativeX() throws MathException {
        double result = normalDistribution.cumulativeProbability(-1.0);
        assertTrue(result > 0.0 && result < 1.0);
    }
    
    @Test
    public void testCumulativeProbabilityZeroX() throws MathException {
        double result = normalDistribution.cumulativeProbability(0.0);
        assertTrue(result >= 0.0 && result <= 1.0);
    }
    
    @Test
    public void testInverseCumulativeProbabilityPositiveP() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(0.5);
        assertTrue(result >= -Double.MAX_VALUE && result <= Double.MAX_VALUE);
    }
    
    @Test
    public void testInverseCumulativeProbabilityNegativeP() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(-0.5);
        assertTrue(result >= -Double.MAX_VALUE && result <= Double.MAX_VALUE);
    }
    
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidP() throws MathException {
        normalDistribution.inverseCumulativeProbability(1.5);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double result = normalDistribution.getDomainLowerBound(0.4);
        assertEquals(-Double.MAX_VALUE, result, 0.0);
        result = normalDistribution.getDomainLowerBound(0.6);
        assertEquals(0.0, result, 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double result = normalDistribution.getDomainUpperBound(0.4);
        assertEquals(0.0, result, 0.0);
        result = normalDistribution.getDomainUpperBound(0.6);
        assertEquals(Double.MAX_VALUE, result, 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        double result = normalDistribution.getInitialDomain(0.4);
        assertEquals(-1.0, result, 0.0);
        result = normalDistribution.getInitialDomain(0.6);
        assertEquals(1.0, result, 0.0);
        result = normalDistribution.getInitialDomain(0.5);
        assertEquals(0.0, result, 0.0);
    }
}
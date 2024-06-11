package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl distribution;
    
    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        distribution.setMean(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(3.5);
        assertEquals(3.5, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviationNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            distribution.setStandardDeviation(-3.0);
        });
        assertEquals("Standard deviation must be positive.", exception.getMessage());
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double probability = distribution.cumulativeProbability(0.0);
        assertEquals(0.5, probability, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        double value = distribution.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, value, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        double value = distribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, value, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double value = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, value, 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBoundLessThanHalf() {
        double bound = distribution.getDomainLowerBound(0.3);
        assertEquals(-Double.MAX_VALUE, bound, 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBoundGreaterThanHalf() {
        double bound = distribution.getDomainLowerBound(0.7);
        assertEquals(0.0, bound, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBoundLessThanHalf() {
        double bound = distribution.getDomainUpperBound(0.3);
        assertEquals(0.0, bound, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBoundGreaterThanHalf() {
        double bound = distribution.getDomainUpperBound(0.7);
        assertEquals(Double.MAX_VALUE, bound, 0.0001);
    }
    
    @Test
    public void testGetInitialDomainLessThanHalf() {
        double domain = distribution.getInitialDomain(0.3);
        assertEquals(-1.0, domain, 0.0001);
    }
    
    @Test
    public void testGetInitialDomainGreaterThanHalf() {
        double domain = distribution.getInitialDomain(0.7);
        assertEquals(1.0, domain, 0.0001);
    }
}
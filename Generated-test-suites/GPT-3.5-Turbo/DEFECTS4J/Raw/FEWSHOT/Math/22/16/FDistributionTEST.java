package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class FDistributionTest {
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeNumeratorDegreesOfFreedom() {
        new FDistribution(-1, 3);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDenominatorDegreesOfFreedom() {
        new FDistribution(2, -3);
    }
    
    @Test
    public void testConstructor() {
        FDistribution fd = new FDistribution(2, 3);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2, 3);
        assertEquals(0.0, fd.density(-1), 0.0);
        assertEquals(0.0, fd.density(0), 0.0);
        assertEquals(0.14433756729740642, fd.density(0.5), 10e-9);
        assertEquals(0.19947114020071635, fd.density(1), 10e-9);
        assertEquals(0.13788101035021478, fd.density(2.5), 10e-9);
        assertEquals(0.09245791049366499, fd.density(5), 10e-9);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2, 3);
        assertEquals(0.0, fd.cumulativeProbability(-1), 0.0);
        assertEquals(0.0, fd.cumulativeProbability(0), 0.0);
        assertEquals(0.8197185014396318, fd.cumulativeProbability(0.5), 10e-9);
        assertEquals(0.8942231690065832, fd.cumulativeProbability(1), 10e-9);
        assertEquals(0.9710345029926896, fd.cumulativeProbability(2.5), 10e-9);
        assertEquals(0.9943249277636337, fd.cumulativeProbability(5), 10e-9);
        assertEquals(1.0, fd.cumulativeProbability(10), 0.0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2, 3);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2, 3);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(2, 3);
        assertEquals(Double.NaN, fd.getNumericalMean(), 0.0);
        
        FDistribution fd2 = new FDistribution(5, 6);
        assertEquals(1.2857142857142858, fd2.getNumericalMean(), 0.0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(2, 3);
        assertTrue(Double.isNaN(fd.getNumericalVariance()));
        
        FDistribution fd2 = new FDistribution(5, 6);
        assertEquals(0.7936507936507936, fd2.getNumericalVariance(), 0.0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(2, 3);
        assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(2, 3);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(2, 3);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(2, 3);
        assertTrue(!fd.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(2, 3);
        assertTrue(fd.isSupportConnected());
    }
}
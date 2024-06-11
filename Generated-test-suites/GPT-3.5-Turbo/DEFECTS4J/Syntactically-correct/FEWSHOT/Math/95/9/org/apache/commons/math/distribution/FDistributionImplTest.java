import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class FDistributionImplTest {
    
    @Test
    public void testConstructor() {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 3.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        assertNotNull(fd);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = -1.0;
        double denominatorDegreesOfFreedom = 3.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = -3.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        double x = 1.5;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        double result = fd.cumulativeProbability(x);
        
        assertEquals(0.825457, result, 1e-6);
    }
    
    @Test
    public void testCumulativeProbability_XLessThanEqualsToZero() throws MathException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        double x = -1.5;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        double result = fd.cumulativeProbability(x);
        
        assertEquals(0.0, result, 0);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        double p = 0.5;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        double result = fd.inverseCumulativeProbability(p);
        
        assertEquals(0.9049, result, 1e-4);
    }
    
    @Test
    public void testInverseCumulativeProbability_PEqualsZero() throws MathException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        double p = 0.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        double result = fd.inverseCumulativeProbability(p);
        
        assertEquals(0.0, result, 0);
    }
    
    @Test
    public void testInverseCumulativeProbability_PEqualsOne() throws MathException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        double p = 1.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        double result = fd.inverseCumulativeProbability(p);
        
        assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double p = 0.5;
        
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        
        double result = fd.getDomainLowerBound(p);
        
        assertEquals(0.0, result, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double p = 0.5;
        
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        
        double result = fd.getDomainUpperBound(p);
        
        assertEquals(Double.MAX_VALUE, result, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        
        FDistributionImpl fd = new FDistributionImpl(3.0, 4.0);
        
        double result = fd.getInitialDomain(p);
        
        assertEquals(2.25, result, 0);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        fd.setNumeratorDegreesOfFreedom(5.0);
        
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_NegativeDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        fd.setNumeratorDegreesOfFreedom(-5.0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        double result = fd.getNumeratorDegreesOfFreedom();
        
        assertEquals(3.0, result, 0);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        fd.setDenominatorDegreesOfFreedom(5.0);
        
        assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_NegativeDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        fd.setDenominatorDegreesOfFreedom(-5.0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        
        FDistributionImpl fd = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        
        double result = fd.getDenominatorDegreesOfFreedom();
        
        assertEquals(4.0, result, 0);
    }
    
}
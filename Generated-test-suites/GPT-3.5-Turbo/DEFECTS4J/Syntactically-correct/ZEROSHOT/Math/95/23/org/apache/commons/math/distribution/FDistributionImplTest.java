import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.cumulativeProbability(2.0);
        assertEquals(0.6552975954, result, 0.000000001);
        
        result = distribution.cumulativeProbability(-1.0);
        assertEquals(0.0, result, 0.000000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.8072121363, result, 0.000000001);
        
        result = distribution.inverseCumulativeProbability(0.0);
        assertEquals(0.0, result, 0.000000001);
        
        result = distribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.000000001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.getInitialDomain(0.5);
        assertEquals(1.25, result, 0.0);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        distribution.setNumeratorDegreesOfFreedom(7);
        assertEquals(7.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithInvalidValue() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        distribution.setNumeratorDegreesOfFreedom(0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(5.0, result, 0.0);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        distribution.setDenominatorDegreesOfFreedom(15);
        assertEquals(15.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithInvalidValue() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        distribution.setDenominatorDegreesOfFreedom(0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(10.0, result, 0.0);
    }
}
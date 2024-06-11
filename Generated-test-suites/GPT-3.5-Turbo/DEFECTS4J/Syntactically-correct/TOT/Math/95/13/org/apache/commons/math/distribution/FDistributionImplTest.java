import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.cumulativeProbability(2.5);
        assertEquals(0.821, result, 0.001);
        
        result = fDistribution.cumulativeProbability(0);
        assertEquals(0, result, 0);
        
        result = fDistribution.cumulativeProbability(15);
        assertEquals(0.999, result, 0.001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.inverseCumulativeProbability(0.821);
        assertEquals(2.5, result, 0);
        
        result = fDistribution.inverseCumulativeProbability(0);
        assertEquals(0, result, 0);
        
        result = fDistribution.inverseCumulativeProbability(0.999);
        assertEquals(15, result, 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(1.25, result, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        fDistribution.setNumeratorDegreesOfFreedom(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        fDistribution.setDenominatorDegreesOfFreedom(-5);
    }

}
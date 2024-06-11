import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {
  
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 3);
        assertEquals(0.01617, fDistribution.cumulativeProbability(1.0), 0.00001);
        assertEquals(0.07528, fDistribution.cumulativeProbability(2.0), 0.00001);
        assertEquals(0.9380, fDistribution.cumulativeProbability(8.0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 3);
        assertEquals(1.04584, fDistribution.inverseCumulativeProbability(0.1), 0.00001);
        assertEquals(1.84273, fDistribution.inverseCumulativeProbability(0.5), 0.00001);
        assertEquals(4.04850, fDistribution.inverseCumulativeProbability(0.9), 0.00001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 3);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.1), 0.00001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.00001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.9), 0.00001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 3);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.1), 0.00001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.00001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.9), 0.00001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 3);
        assertEquals(1.5, fDistribution.getInitialDomain(0.1), 0.00001);
        assertEquals(1.5, fDistribution.getInitialDomain(0.5), 0.00001);
        assertEquals(1.5, fDistribution.getInitialDomain(0.9), 0.00001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 3);
        fDistribution.setNumeratorDegreesOfFreedom(0.0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 3);
        assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 3);
        fDistribution.setDenominatorDegreesOfFreedom(0.0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 3);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.00001);
    }

}
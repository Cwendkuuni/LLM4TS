import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        double result = fDistribution.cumulativeProbability(1.5);
        assertEquals(0.4068023, result, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.777778, result, 0.000001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        double result = fDistribution.getDomainLowerBound(0.2);
        assertEquals(0.0, result, 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        double result = fDistribution.getDomainUpperBound(0.2);
        assertEquals(Double.MAX_VALUE, result, 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        double result = fDistribution.getInitialDomain(0.2);
        assertEquals(3.333333, result, 0.000001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalid() {
        FDistributionImpl fDistribution = new FDistributionImpl(0.0, 3.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalid() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 0.0);
    }

}
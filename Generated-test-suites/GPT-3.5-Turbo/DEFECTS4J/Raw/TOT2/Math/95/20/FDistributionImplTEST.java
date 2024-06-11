import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.cumulativeProbability(2.0);
        assertEquals(0.264423394585595, result, 0.0000000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.inverseCumulativeProbability(0.264423394585595);
        assertEquals(2.0, result, 0.0000000001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(1.25, result, 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        fDistribution.setNumeratorDegreesOfFreedom(-2.0); // should throw IllegalArgumentException
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        fDistribution.setDenominatorDegreesOfFreedom(-5.0); // should throw IllegalArgumentException
    }
}
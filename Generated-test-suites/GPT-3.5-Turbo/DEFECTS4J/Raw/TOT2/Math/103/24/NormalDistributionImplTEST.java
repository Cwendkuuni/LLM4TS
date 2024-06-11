import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {
    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
    
    @Test
    public void testGetMean(){
        assertEquals(0, normalDistribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean(){
        normalDistribution.setMean(3);
        assertEquals(3, normalDistribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation(){
        assertEquals(1, normalDistribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation(){
        normalDistribution.setStandardDeviation(2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability(){
        try {
            double result = normalDistribution.cumulativeProbability(0.5);
            assertEquals(0.691462, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown.");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability(){
        try {
            double result = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(0, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown.");
        }
    }
    
    @Test
    public void testGetDomainLowerBound(){
        double result = normalDistribution.getDomainLowerBound(0.3);
        assertEquals(-Double.MAX_VALUE, result, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound(){
        double result = normalDistribution.getDomainUpperBound(0.7);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }
    
    @Test
    public void testGetInitialDomain(){
        double result = normalDistribution.getInitialDomain(0.5);
        assertEquals(1, result, 0.0001);
    }
}
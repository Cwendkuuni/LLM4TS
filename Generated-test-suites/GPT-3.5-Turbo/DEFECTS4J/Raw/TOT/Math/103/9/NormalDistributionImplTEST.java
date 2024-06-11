import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl dist;
    
    @Before
    public void setUp() {
        dist = new NormalDistributionImpl(0.0, 1.0);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, dist.getMean(), 0);
    }
    
    @Test
    public void testSetMean() {
        dist.setMean(2.5);
        assertEquals(2.5, dist.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, dist.getStandardDeviation(), 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        dist.setStandardDeviation(2.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0);
    }
    
    @Test
    public void testCumulativeProbability() {
        try {
            // Typical use case
            double result1 = dist.cumulativeProbability(0.5);
            assertEquals(0.691462, result1, 0.000001);
            
            // Edge cases
            double result2 = dist.cumulativeProbability(Double.NEGATIVE_INFINITY);
            assertEquals(0.0, result2, 0);
            
            double result3 = dist.cumulativeProbability(Double.POSITIVE_INFINITY);
            assertEquals(1.0, result3, 0);
            
            // Error scenario
            double result4 = dist.cumulativeProbability(Double.NaN);
            fail("MathException not thrown");
            
        } catch (MathException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        try {
            // Typical use case
            double result1 = dist.inverseCumulativeProbability(0.5);
            assertEquals(0.0, result1, 0);
            
            // Edge cases
            double result2 = dist.inverseCumulativeProbability(0);
            assertEquals(Double.NEGATIVE_INFINITY, result2, 0);
            
            double result3 = dist.inverseCumulativeProbability(1);
            assertEquals(Double.POSITIVE_INFINITY, result3, 0);
            
            // Error scenario
            double result4 = dist.inverseCumulativeProbability(-0.5);
            fail("MathException not thrown");
            
        } catch (MathException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double result1 = dist.getDomainLowerBound(0.25);
        assertEquals(-Double.MAX_VALUE, result1, 0);
        
        double result2 = dist.getDomainLowerBound(0.75);
        assertEquals(0.0, result2, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double result1 = dist.getDomainUpperBound(0.25);
        assertEquals(0.0, result1, 0);
        
        double result2 = dist.getDomainUpperBound(0.75);
        assertEquals(Double.MAX_VALUE, result2, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        double result1 = dist.getInitialDomain(0.25);
        assertEquals(-1.0, result1, 0);
        
        double result2 = dist.getInitialDomain(0.75);
        assertEquals(1.0, result2, 0);
    }
}
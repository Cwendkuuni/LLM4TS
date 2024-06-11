import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    // Create an instance of the NormalDistributionImpl class to test
    NormalDistributionImpl distribution = new NormalDistributionImpl();
    
    // Test getMean() method
    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.00001);
    }
    
    // Test setMean() method
    @Test
    public void testSetMean() {
        distribution.setMean(1.5);
        assertEquals(1.5, distribution.getMean(), 0.00001);
    }
    
    // Test getStandardDeviation() method
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.00001);
    }
    
    // Test setStandardDeviation() method
    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.00001);
    }
    
    // Test cumulativeProbability() method for a basic case
    @Test
    public void testCumulativeProbability() {
        try{
            double result = distribution.cumulativeProbability(1.0);
            assertEquals(0.8413447460, result, 0.00001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    // Test inverseCumulativeProbability() method for a basic case
    @Test
    public void testInverseCumulativeProbability() {
        try{
            double result = distribution.inverseCumulativeProbability(0.8413447460);
            assertEquals(1.0, result, 0.00001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    // Test getDomainLowerBound() method for p < 0.5
    @Test
    public void testGetDomainLowerBoundForP() {
        double result = distribution.getDomainLowerBound(0.2);
        assertEquals(-Double.MAX_VALUE, result, 0.00001);
    }
    
    // Test getDomainUpperBound() method for p < 0.5
    @Test
    public void testGetDomainUpperBoundForP() {
        double result = distribution.getDomainUpperBound(0.2);
        assertEquals(0.0, result, 0.00001);
    }
    
    // Test getInitialDomain() method for p < 0.5
    @Test
    public void testGetInitialDomainForP() {
        double result = distribution.getInitialDomain(0.2);
        assertEquals(-1.0, result, 0.00001);
    }
}
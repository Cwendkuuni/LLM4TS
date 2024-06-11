import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class NormalDistributionImplTest {

    // Test for getMean method
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }
    
    // Test for setMean method
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0001);
    }
    
    // Test for getStandardDeviation method
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(1.5, distribution.getStandardDeviation(), 0.0001);
    }
    
    // Test for setStandardDeviation method
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    // Test for cumulativeProbability method with valid input
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        double result = distribution.cumulativeProbability(4.0); // x = 4.0
        assertEquals(0.8413, result, 0.0001);
    }
    
    // Test for cumulativeProbability method with invalid input
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalidInput() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        distribution.cumulativeProbability(Double.NaN); // x = NaN
    }
    
    // Test for inverseCumulativeProbability method with valid input
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        double result = distribution.inverseCumulativeProbability(0.5); // p = 0.5
        assertEquals(2.5, result, 0.0001);
    }
    
    // Test for inverseCumulativeProbability method with invalid input
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        distribution.inverseCumulativeProbability(-0.1); // p = -0.1
    }
    
    // Test for getDomainLowerBound method with valid input
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        double result = distribution.getDomainLowerBound(0.4); // p = 0.4
        assertEquals(-Double.MAX_VALUE, result, 0.0001);
    }
    
    // Test for getDomainLowerBound method with edge case
    @Test
    public void testGetDomainLowerBoundEdgeCase() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        double result = distribution.getDomainLowerBound(0.5); // p = 0.5
        assertEquals(2.5, result, 0.0001);
    }
    
    // Test for getDomainUpperBound method with valid input
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        double result = distribution.getDomainUpperBound(0.6); // p = 0.6
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }
    
    // Test for getDomainUpperBound method with edge case
    @Test
    public void testGetDomainUpperBoundEdgeCase() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        double result = distribution.getDomainUpperBound(0.5); // p = 0.5
        assertEquals(2.5, result, 0.0001);
    }
    
    // Test for getInitialDomain method with valid input
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        double result = distribution.getInitialDomain(0.3); // p = 0.3
        assertEquals(1.0, result, 0.0001);
    }
    
    // Test for getInitialDomain method with edge cases
    @Test
    public void testGetInitialDomainEdgeCase() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        double result1 = distribution.getInitialDomain(0.4); // p = 0.4
        double result2 = distribution.getInitialDomain(0.6); // p = 0.6
        assertEquals(1.0, result1, 0.0001);
        assertEquals(4.0, result2, 0.0001);
    }
}
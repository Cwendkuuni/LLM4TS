import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl distribution;
    
    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0, distribution.getMean(), 0);
    }
    
    @Test
    public void testSetMean() {
        double mean = 5.4;
        distribution.setMean(mean);
        assertEquals(mean, distribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        double stdDev = 2.4;
        distribution.setStandardDeviation(stdDev);
        assertEquals(stdDev, distribution.getStandardDeviation(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationInvalid() {
        distribution.setStandardDeviation(-2.4);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(0.691462, result, 0.000001);
    }
    
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityError() throws MathException {
        distribution.cumulativeProbability(500);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0, result, 0);
    }
    
    @Test
    public void testInverseCumulativeProbabilityLowerBound() throws MathException {
        double result = distribution.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0);
    }
    
    @Test
    public void testInverseCumulativeProbabilityUpperBound() throws MathException {
        double result = distribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalid() throws MathException {
        distribution.inverseCumulativeProbability(1.5);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        double result1 = distribution.getDomainLowerBound(0.2);
        double result2 = distribution.getDomainLowerBound(0.5);
        assertEquals(-Double.MAX_VALUE, result1, 0);
        assertEquals(0, result2, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        double result1 = distribution.getDomainUpperBound(0.2);
        double result2 = distribution.getDomainUpperBound(0.5);
        assertEquals(0, result1, 0);
        assertEquals(Double.MAX_VALUE, result2, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        double result1 = distribution.getInitialDomain(0.2);
        double result2 = distribution.getInitialDomain(0.5);
        assertEquals(-1, result1, 0);
        assertEquals(1, result2, 0);
    }
}
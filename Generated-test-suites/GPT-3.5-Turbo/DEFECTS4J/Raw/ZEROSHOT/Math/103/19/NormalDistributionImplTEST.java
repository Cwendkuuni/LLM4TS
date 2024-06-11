import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl normalDistribution;
    
    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        normalDistribution.setMean(1.0);
        assertEquals(1.0, normalDistribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        try {
            double result = normalDistribution.cumulativeProbability(1.0);
            assertEquals(0.8413, result, 0.0001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        try {
            double result = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(0.0, result, 0.0001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        normalDistribution.inverseCumulativeProbability(1.5);
    }
    
    @Test
    public void testGetDomainLowerBoundLessThanHalf() {
        double result = normalDistribution.getDomainLowerBound(0.4);
        assertEquals(-Double.MAX_VALUE, result, 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBoundGreaterThanHalf() {
        double result = normalDistribution.getDomainLowerBound(0.6);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBoundLessThanHalf() {
        double result = normalDistribution.getDomainUpperBound(0.4);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBoundGreaterThanHalf() {
        double result = normalDistribution.getDomainUpperBound(0.6);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }
    
    @Test
    public void testGetInitialDomainLessThanHalf() {
        double result = normalDistribution.getInitialDomain(0.4);
        assertEquals(-1.0, result, 0.0001);
    }
    
    @Test
    public void testGetInitialDomainGreaterThanHalf() {
        double result = normalDistribution.getInitialDomain(0.6);
        assertEquals(1.0, result, 0.0001);
    }
}
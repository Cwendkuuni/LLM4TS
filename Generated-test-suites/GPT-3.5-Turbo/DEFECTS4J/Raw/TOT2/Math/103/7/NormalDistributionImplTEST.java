import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(10.0, 2.0);
        assertEquals(10.0, normalDist.getMean(), 0.0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setMean(5.0);
        assertEquals(5.0, normalDist.getMean(), 0.0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(10.0, 2.0);
        assertEquals(2.0, normalDist.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(3.0);
        assertEquals(3.0, normalDist.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(0.5, normalDist.cumulativeProbability(0.0), 0.0001);
        } catch (MathException e) {
            fail("Exception should not be thrown for this test case");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDist.inverseCumulativeProbability(0), 0.0);
            assertEquals(Double.POSITIVE_INFINITY, normalDist.inverseCumulativeProbability(1), 0.0);
        } catch (MathException e) {
            fail("Exception should not be thrown for this test case");
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, normalDist.getDomainLowerBound(0.2), 0.0);
        assertEquals(0.0, normalDist.getDomainLowerBound(0.8), 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.MAX_VALUE, normalDist.getDomainUpperBound(0.2), 0.0);
        assertEquals(0.0, normalDist.getDomainUpperBound(0.8), 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.0, normalDist.getInitialDomain(0.2), 0.0);
        assertEquals(1.0, normalDist.getInitialDomain(0.8), 0.0);
    }
}
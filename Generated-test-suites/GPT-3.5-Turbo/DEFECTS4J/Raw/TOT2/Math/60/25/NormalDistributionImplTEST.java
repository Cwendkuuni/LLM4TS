import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(2, 3);
        assertEquals(2, normalDist.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(2, 3);
        assertEquals(3, normalDist.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, normalDist.density(0), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, normalDist.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("Should not throw MathException");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0, normalDist.inverseCumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("Should not throw MathException");
        }
    }
    
    @Test
    public void testSample() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            normalDist.sample(); // Check no exceptions are thrown
        } catch (MathException e) {
            fail("Should not throw MathException");
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, normalDist.getDomainLowerBound(0.4), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, normalDist.getDomainUpperBound(0.6), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(-1, normalDist.getInitialDomain(0.4), 0.0001);
    }
}
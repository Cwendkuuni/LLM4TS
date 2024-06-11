import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(1, dist.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
            
        assertEquals(0.3989, dist.density(0), 0.0001);
        assertEquals(0.2419, dist.density(1), 0.0001);
        assertEquals(0.0539, dist.density(2), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
            
        assertEquals(0.5, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.8413, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.9772, dist.cumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
            
        assertEquals(0, dist.inverseCumulativeProbability(0.5), 0.0001);
        assertEquals(1, dist.inverseCumulativeProbability(0.8413), 0.0001);
        assertEquals(2, dist.inverseCumulativeProbability(0.9772), 0.0001);
    }
    
    @Test
    public void testSample() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
            
        double sample = dist.sample();
            
        assertTrue(sample >= -10 && sample <= 10);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
            
        assertEquals(Double.NEGATIVE_INFINITY, dist.getDomainLowerBound(0.3), 0.0001);
        assertEquals(0, dist.getDomainLowerBound(0.9), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
            
        assertEquals(0, dist.getDomainUpperBound(0.3), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, dist.getDomainUpperBound(0.9), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
            
        assertEquals(-1, dist.getInitialDomain(0.3), 0.0001);
        assertEquals(1, dist.getInitialDomain(0.9), 0.0001);
    }
}
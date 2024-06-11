import org.apache.commons.math.distribution.*;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, normal.getMean(), 0.01);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(1.0, normal.getStandardDeviation(), 0.01);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989, normal.density(-1), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0.8413, normal.cumulativeProbability(1), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0.8413, normal.inverseCumulativeProbability(0.8), 0.0001);
    }
    
    @Test
    public void testSample() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double sample = normal.sample();
        assertTrue(sample >= -Double.MAX_VALUE && sample <= Double.MAX_VALUE);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.2), 0.01);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.8), 0.01);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(-1.0, normal.getInitialDomain(0.3), 0.01);
    }
}
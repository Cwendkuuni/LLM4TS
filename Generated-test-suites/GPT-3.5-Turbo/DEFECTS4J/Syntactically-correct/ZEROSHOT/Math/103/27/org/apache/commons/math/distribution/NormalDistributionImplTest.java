import org.apache.commons.math.distribution.NormalDistribution;
import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    private static final double DELTA = 1e-15;
    
    @Test
    public void testGetMean() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), DELTA);
        
        distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getMean(), DELTA);
    }
    
    @Test
    public void testSetMean() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(7.0);
        assertEquals(7.0, distribution.getMean(), DELTA);
        
        distribution.setMean(-1.0);
        assertEquals(-1.0, distribution.getMean(), DELTA);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(1.0, distribution.getStandardDeviation(), DELTA);
        
        distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), DELTA);
        
        distribution.setStandardDeviation(0.5);
        assertEquals(0.5, distribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl();
        try {
            double result = distribution.cumulativeProbability(0.0);
            assertEquals(0.5, result, DELTA);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl();
        try {
            double result = distribution.inverseCumulativeProbability(0.5);
            assertEquals(0.0, result, DELTA);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}
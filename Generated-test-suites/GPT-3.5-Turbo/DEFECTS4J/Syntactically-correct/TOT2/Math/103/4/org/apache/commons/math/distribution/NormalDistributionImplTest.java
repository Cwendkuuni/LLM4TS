import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0);
    }
    
    @Test
    public void testSetMean() {
        normalDistribution.setMean(5.0);
        assertEquals(5.0, normalDistribution.getMean(), 0.0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0.0), 0.001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        double p = 0.0;
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(p), 0.0);
        } catch (MathException e) {
            fail("MathException thrown");
        }
        
        p = 1.0;
        try {
            assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(p), 0.0);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.25), 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.75), 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, normalDistribution.getInitialDomain(0.25), 0.0);
    }

}
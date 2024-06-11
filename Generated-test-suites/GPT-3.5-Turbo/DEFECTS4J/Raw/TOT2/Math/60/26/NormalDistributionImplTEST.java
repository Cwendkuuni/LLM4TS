import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(5.0, normalDistribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.24197, normalDistribution.density(0.5), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.84134, normalDistribution.cumulativeProbability(1), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(1.28155, normalDistribution.inverseCumulativeProbability(0.9), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testSample() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 3);
        try {
            double sample = normalDistribution.sample();
            assertTrue(sample >= 0 && sample <= 20);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.3), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.7), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, normalDistribution.getInitialDomain(0.3), 0.0001);
    }
}
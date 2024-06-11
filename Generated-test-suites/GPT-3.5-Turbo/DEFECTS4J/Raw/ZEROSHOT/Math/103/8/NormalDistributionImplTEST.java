import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(2.5, distribution.getMean(), 0.0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.7);
        assertEquals(3.7, distribution.getMean(), 0.0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(1.5, distribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.3);
        assertEquals(2.3, distribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double prob = distribution.cumulativeProbability(0.5);
        assertEquals(0.691462, prob, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double value = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0, value, 0.000001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double bound = distribution.getDomainLowerBound(0.3);
        assertEquals(-Double.MAX_VALUE, bound, 0.000001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double bound = distribution.getDomainUpperBound(0.7);
        assertEquals(Double.MAX_VALUE, bound, 0.000001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double domain = distribution.getInitialDomain(0.5);
        assertEquals(1, domain, 0.000001);
    }
}
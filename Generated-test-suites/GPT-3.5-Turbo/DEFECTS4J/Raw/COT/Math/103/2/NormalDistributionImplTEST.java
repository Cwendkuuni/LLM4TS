import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {
  
    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
        assertEquals(10, dist.getMean(), 0);
    }
  
    @Test
    public void testSetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(5);
        assertEquals(5, dist.getMean(), 0);
    }
  
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
        assertEquals(2, dist.getStandardDeviation(), 0);
    }
  
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(3);
        assertEquals(3, dist.getStandardDeviation(), 0);
    }
  
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
        assertEquals(0.5, dist.cumulativeProbability(10), 0);
    }
  
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
        assertEquals(10, dist.inverseCumulativeProbability(0.5), 0);
    }
  
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
        assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0.25), 0);
    }
  
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.75), 0);
    }
  
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl(10, 2);
        assertEquals(9.0, dist.getInitialDomain(0.4), 0);
    }
}
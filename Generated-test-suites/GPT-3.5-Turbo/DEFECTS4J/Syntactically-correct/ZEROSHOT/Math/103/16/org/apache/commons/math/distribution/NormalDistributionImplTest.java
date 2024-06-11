import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(0.5, distribution.cumulativeProbability(5.0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.inverseCumulativeProbability(0.5), 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.4), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.6), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(3.0, distribution.getInitialDomain(0.4), 0.0001);
    }
}
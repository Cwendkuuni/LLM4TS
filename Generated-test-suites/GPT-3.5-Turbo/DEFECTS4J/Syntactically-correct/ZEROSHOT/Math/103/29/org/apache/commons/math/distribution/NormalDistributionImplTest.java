import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.5);
        assertEquals(3.5, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(0.5, distribution.cumulativeProbability(2.5), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(2.5, distribution.inverseCumulativeProbability(0.5), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityForZero() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityForOne() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.4), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.6), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0, distribution.getInitialDomain(0.5), 0.0001);
    }
}
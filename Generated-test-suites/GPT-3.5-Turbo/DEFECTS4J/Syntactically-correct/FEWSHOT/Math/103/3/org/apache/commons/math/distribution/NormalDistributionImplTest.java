import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, dist.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(3.0);
        assertEquals(3.0, dist.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(4.0);
        assertEquals(4.0, dist.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.cumulativeProbability(-Double.MAX_VALUE), 0.0001);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(Double.MAX_VALUE), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1.0), 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0.25), 0.0001);
        assertEquals(0.0, dist.getDomainLowerBound(0.75), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getDomainUpperBound(0.25), 0.0001);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.75), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.0, dist.getInitialDomain(0.25), 0.0001);
        assertEquals(1.0, dist.getInitialDomain(0.75), 0.0001);
        assertEquals(0.0, dist.getInitialDomain(0.5), 0.0001);
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0, distribution.getMean(), 0);
        distribution.setMean(5);
        assertEquals(5, distribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(1, distribution.getStandardDeviation(), 0);
        distribution.setStandardDeviation(2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationInvalid() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.5, distribution.cumulativeProbability(0), 0);
        assertEquals(0, distribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0);
        assertEquals(1, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.4), 0);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.5), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.6), 0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(-1, distribution.getInitialDomain(0.4), 0);
        assertEquals(1, distribution.getInitialDomain(0.6), 0);
        assertEquals(0, distribution.getInitialDomain(0.5), 0);
    }
}
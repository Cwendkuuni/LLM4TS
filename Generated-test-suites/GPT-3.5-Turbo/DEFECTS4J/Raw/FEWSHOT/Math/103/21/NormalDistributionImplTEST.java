import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        distribution.setMean(2.0);
        assertEquals(2.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(0.3085, distribution.cumulativeProbability(0.0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(-0.6745, distribution.inverseCumulativeProbability(0.25), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.25), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.75), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(-0.5104, distribution.getInitialDomain(0.4), 0.0001);
    }
}
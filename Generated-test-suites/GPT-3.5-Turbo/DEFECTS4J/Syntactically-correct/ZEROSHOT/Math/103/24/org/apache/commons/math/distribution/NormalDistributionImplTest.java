import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(0.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setMean(5.0);
        Assert.assertEquals(5.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(2.0);
        Assert.assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.0001);
        Assert.assertEquals(0.8413, distribution.cumulativeProbability(1.0), 0.0001);
        Assert.assertEquals(0.1587, distribution.cumulativeProbability(-1.0), 0.0001);
        Assert.assertEquals(0.9772, distribution.cumulativeProbability(2.0), 0.0001);
        Assert.assertEquals(0.0227, distribution.cumulativeProbability(-2.0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0001);
        Assert.assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0001);
        Assert.assertEquals(-0.6744, distribution.inverseCumulativeProbability(0.25), 0.0001);
        Assert.assertEquals(0.6744, distribution.inverseCumulativeProbability(0.75), 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.0), 0.0001);
        Assert.assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.25), 0.0001);
        Assert.assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0001);
        Assert.assertEquals(0.0, distribution.getDomainLowerBound(0.75), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(0.0, distribution.getDomainUpperBound(0.0), 0.0001);
        Assert.assertEquals(0.0, distribution.getDomainUpperBound(0.25), 0.0001);
        Assert.assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.0001);
        Assert.assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.75), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(-1.0, distribution.getInitialDomain(0.0), 0.0001);
        Assert.assertEquals(-0.6744, distribution.getInitialDomain(0.25), 0.0001);
        Assert.assertEquals(0.0, distribution.getInitialDomain(0.5), 0.0001);
        Assert.assertEquals(0.6744, distribution.getInitialDomain(0.75), 0.0001);
        Assert.assertEquals(1.0, distribution.getInitialDomain(1.0), 0.0001);
    }
}
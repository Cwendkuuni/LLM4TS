
import org.apache.commons.math.exception.MathException;
import org.apache.commons.math.distribution.*;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        Assert.assertEquals(1, distribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        Assert.assertEquals(2, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        Assert.assertEquals(0.053991, distribution.density(3), 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        Assert.assertEquals(0.308538, distribution.cumulativeProbability(0.5), 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        Assert.assertEquals(-0.994457, distribution.inverseCumulativeProbability(0.1), 0.000001);
    }
    
    @Test
    public void testSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        Assert.assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.2), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        Assert.assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.2), 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        Assert.assertEquals(-1, distribution.getInitialDomain(0.2), 0);
    }
}

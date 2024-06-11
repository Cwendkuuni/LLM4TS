
import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 5);
        Assert.assertEquals(10, distribution.getMean(), 0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(20);
        Assert.assertEquals(20, distribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 5);
        Assert.assertEquals(5, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3);
        Assert.assertEquals(3, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 5);
        double result = distribution.cumulativeProbability(15);
        Assert.assertEquals(0.841344746, result, 0.000000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 5);
        double result = distribution.inverseCumulativeProbability(0.841344746);
        Assert.assertEquals(15, result, 0.000000001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 5);
        double result = distribution.getDomainLowerBound(0.5);
        Assert.assertEquals(10, result, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 5);
        double result = distribution.getDomainUpperBound(0.5);
        Assert.assertEquals(10, result, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 5);
        double result = distribution.getInitialDomain(0.5);
        Assert.assertEquals(10, result, 0);
    }
    
}
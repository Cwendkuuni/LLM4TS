import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double mean = distribution.getMean();
        Assert.assertEquals(0, mean, 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double stdDev = distribution.getStandardDeviation();
        Assert.assertEquals(1, stdDev, 0);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double density = distribution.density(0);
        Assert.assertEquals(0.3989422804014327, density, 0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double probability = distribution.cumulativeProbability(0);
        Assert.assertEquals(0.5, probability, 0);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double inverseProbability = distribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0, inverseProbability, 0);
    }
    
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        Assert.assertNotNull(sample);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double absoluteAccuracy = distribution.getSolverAbsoluteAccuracy();
        Assert.assertEquals(1e-9, absoluteAccuracy, 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double lowerBound = distribution.getDomainLowerBound(0.5);
        Assert.assertEquals(-Double.MAX_VALUE, lowerBound, 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double upperBound = distribution.getDomainUpperBound(0.5);
        Assert.assertEquals(Double.MAX_VALUE, upperBound, 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double initialDomain = distribution.getInitialDomain(0.5);
        Assert.assertEquals(1, initialDomain, 0);
    }
}
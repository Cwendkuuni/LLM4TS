import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(5, 2);
        Assert.assertEquals(5, normal.getMean(), 0.001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(5, 2);
        Assert.assertEquals(2, normal.getStandardDeviation(), 0.001);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.3989422804014327, normal.density(0), 0.001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.5, normal.cumulativeProbability(0), 0.001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0, normal.inverseCumulativeProbability(0.5), 0.001);
    }
    
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double value = normal.sample();
        Assert.assertTrue(value >= Double.NEGATIVE_INFINITY);
        Assert.assertTrue(value <= Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1e-9, normal.getSolverAbsoluteAccuracy(), 0.001);
    }
    
    @Test
    public void testGetDomainLowerBound_lessThan05() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.4), 0.001);
    }
    
    @Test
    public void testGetDomainLowerBound_greaterThan05() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0, normal.getDomainLowerBound(0.6), 0.001);
    }
    
    @Test
    public void testGetDomainUpperBound_lessThan05() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0, normal.getDomainUpperBound(0.4), 0.001);
    }
    
    @Test
    public void testGetDomainUpperBound_greaterThan05() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.6), 0.001);
    }
    
    @Test
    public void testGetInitialDomain_lessThan05() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(-1, normal.getInitialDomain(0.4), 0.001);
    }
    
    @Test
    public void testGetInitialDomain_greaterThan05() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1, normal.getInitialDomain(0.6), 0.001);
    }
}
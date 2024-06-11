import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private final double DELTA = 1e-9; // allowed delta for floating-point comparison
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0, distribution.getMean(), DELTA);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1, distribution.getStandardDeviation(), DELTA);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.3989422804014327, distribution.density(0), DELTA);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0), DELTA);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0, distribution.inverseCumulativeProbability(0), DELTA);
    }
    
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), DELTA);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.25), DELTA);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.75), DELTA);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(-1, distribution.getInitialDomain(0.25), DELTA);
    }

}
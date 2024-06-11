import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    private static final double DELTA = 1e-9;
    private static final double MEAN = 0;
    private static final double STANDARD_DEVIATION = 1;
    private static final double ACCURACY = 1e-9;
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION);
        Assert.assertEquals(MEAN, dist.getMean(), DELTA);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION);
        Assert.assertEquals(STANDARD_DEVIATION, dist.getStandardDeviation(), DELTA);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION);
        double result = dist.density(0);
        Assert.assertEquals(0.3989422804014327, result, DELTA);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION);
        double result = dist.cumulativeProbability(0);
        Assert.assertEquals(0.5, result, DELTA);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION, ACCURACY);
        double result = dist.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0, result, DELTA);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION);
        double result = dist.sample();
        Assert.assertTrue(-Double.MAX_VALUE <= result && result <= Double.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION);
        double result = dist.getDomainLowerBound(0.5);
        Assert.assertEquals(-Double.MAX_VALUE, result, DELTA);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION);
        double result = dist.getDomainUpperBound(0.5);
        Assert.assertEquals(Double.MAX_VALUE, result, DELTA);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION);
        double result = dist.getInitialDomain(0.5);
        Assert.assertEquals(0, result, DELTA);
    }
}
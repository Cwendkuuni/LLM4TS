import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(0.0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(2.5);
        Assert.assertEquals(2.5, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(3.5);
        Assert.assertEquals(3.5, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDistribution.cumulativeProbability(1.5);
        Assert.assertEquals(0.933192797, result, 0.000000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDistribution.inverseCumulativeProbability(0.8);
        Assert.assertEquals(0.841621233, result, 0.000000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDistribution.getDomainLowerBound(0.25);
        Assert.assertEquals(-1.7976931348623157E308, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDistribution.getDomainUpperBound(0.75);
        Assert.assertEquals(1.7976931348623157E308, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDistribution.getInitialDomain(0.5);
        Assert.assertEquals(0.0, result, 0.0001);
    }
}
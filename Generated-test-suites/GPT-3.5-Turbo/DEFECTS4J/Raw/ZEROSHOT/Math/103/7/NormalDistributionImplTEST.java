import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(2.5, normalDistribution.getMean(), 0.01);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(3.5);
        Assert.assertEquals(3.5, normalDistribution.getMean(), 0.01);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(1.5, normalDistribution.getStandardDeviation(), 0.01);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(2.5);
        Assert.assertEquals(2.5, normalDistribution.getStandardDeviation(), 0.01);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        double cumulativeProbability = normalDistribution.cumulativeProbability(1.5);
        Assert.assertEquals(0.266, cumulativeProbability, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals(2.5, inverseCumulativeProbability, 0.01);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        double domainLowerBound = normalDistribution.getDomainLowerBound(0.2);
        Assert.assertEquals(-Double.MAX_VALUE, domainLowerBound, 0.01);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        double domainUpperBound = normalDistribution.getDomainUpperBound(0.8);
        Assert.assertEquals(Double.MAX_VALUE, domainUpperBound, 0.01);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        double initialDomain = normalDistribution.getInitialDomain(0.6);
        Assert.assertEquals(4.0, initialDomain, 0.01);
    }
}
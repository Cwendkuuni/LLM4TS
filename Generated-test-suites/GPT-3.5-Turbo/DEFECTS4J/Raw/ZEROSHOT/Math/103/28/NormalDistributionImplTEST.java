import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2, 3);
        double mean = normalDistribution.getMean();
        Assert.assertEquals(2, mean, 0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2, 3);
        normalDistribution.setMean(3);
        double mean = normalDistribution.getMean();
        Assert.assertEquals(3, mean, 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2, 3);
        double standardDeviation = normalDistribution.getStandardDeviation();
        Assert.assertEquals(3, standardDeviation, 0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2, 3);
        normalDistribution.setStandardDeviation(4);
        double standardDeviation = normalDistribution.getStandardDeviation();
        Assert.assertEquals(4, standardDeviation, 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2, 3);
        double cumulativeProbability = normalDistribution.cumulativeProbability(4);
        Assert.assertEquals(0.7475074624530771, cumulativeProbability, 0.00001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2, 3);
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.7);
        Assert.assertEquals(2.6744897501960826, inverseCumulativeProbability, 0.00001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2, 3);
        double domainLowerBound = normalDistribution.getDomainLowerBound(0.7);
        Assert.assertEquals(2, domainLowerBound, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2, 3);
        double domainUpperBound = normalDistribution.getDomainUpperBound(0.7);
        Assert.assertEquals(Double.MAX_VALUE, domainUpperBound, 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2, 3);
        double initialDomain = normalDistribution.getInitialDomain(0.7);
        Assert.assertEquals(5, initialDomain, 0);
    }
}
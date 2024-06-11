import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    PoissonDistributionImpl poissonDistribution;

    @Test
    public void testGetMean() {
        poissonDistribution = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(5.0, poissonDistribution.getMean(), 0.0);
    }

    @Test(expected = org.apache.commons.math.MathRuntimeException.class)
    public void testNegativeMean() {
        poissonDistribution = new PoissonDistributionImpl(-5.0);
    }

    @Test
    public void testProbability() {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        Assert.assertEquals(0.12511003572113372, poissonDistribution.probability(5), 0.0);
    }

    @Test
    public void testProbabilityNegativeX() {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        Assert.assertEquals(0.0, poissonDistribution.probability(-5), 0.0);
    }

    @Test
    public void testProbabilityMaxValueX() {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        Assert.assertEquals(0.0, poissonDistribution.probability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws org.apache.commons.math.MathException {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        Assert.assertEquals(0.0078334192293563, poissonDistribution.cumulativeProbability(5), 0.0);
    }

    @Test
    public void testCumulativeProbabilityNegativeX() throws org.apache.commons.math.MathException {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        Assert.assertEquals(0.0, poissonDistribution.cumulativeProbability(-5), 0.0);
    }

    @Test
    public void testCumulativeProbabilityMaxValueX() throws org.apache.commons.math.MathException {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        Assert.assertEquals(1.0, poissonDistribution.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testNormalApproximateProbability() throws org.apache.commons.math.MathException {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        Assert.assertEquals(0.9937903346742238, poissonDistribution.normalApproximateProbability(5), 0.0);
    }

    @Test
    public void testSample() throws org.apache.commons.math.MathException {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        int sample = poissonDistribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        Assert.assertEquals(0, poissonDistribution.getDomainLowerBound(0.0));
    }

    @Test
    public void testGetDomainUpperBound() {
        poissonDistribution = new PoissonDistributionImpl(10.0);
        Assert.assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.0));
    }

}
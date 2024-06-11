import org.junit.Test;
import org.junit.Assert;

public class PoissonDistributionImplTest {

    PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(5.0);

    @Test
    public void testGetMean() {
        Assert.assertEquals(5.0, poissonDistribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.17547, poissonDistribution.probability(3), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        double cumulativeProbability = poissonDistribution.cumulativeProbability(3);
        Assert.assertEquals(0.2650259, cumulativeProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() {
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(3);
        Assert.assertEquals(0.77935, normalApproximateProbability, 0.0001);
    }

    @Test
    public void testSample() {
        int sample = poissonDistribution.sample();
        Assert.assertTrue(sample >= 0);
    }

    @Test
    public void testLowerBound() {
        int lowerBound = poissonDistribution.getDomainLowerBound(0.0);
        Assert.assertEquals(0, lowerBound);
    }

    @Test
    public void testUpperBound() {
        int upperBound = poissonDistribution.getDomainUpperBound(1.0);
        Assert.assertEquals(Integer.MAX_VALUE, upperBound);
    }
}
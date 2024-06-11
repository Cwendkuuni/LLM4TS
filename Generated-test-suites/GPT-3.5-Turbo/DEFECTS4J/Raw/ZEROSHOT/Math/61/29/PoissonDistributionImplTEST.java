import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(5, distribution.getMean(), 0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.067, distribution.probability(2), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.615, distribution.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.97, distribution.normalApproximateProbability(5), 0.01);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        int sample = distribution.sample();
        Assert.assertTrue(sample >= 0);
        Assert.assertTrue(sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0.5), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
    }
}
import org.junit.Assert;
import org.junit.Test;


public class PoissonDistributionImplTest {
    PoissonDistributionImpl poisson;

    @Test
    public void testGetMean() {
        poisson = new PoissonDistributionImpl(5);
        Assert.assertEquals(5, poisson.getMean(), 0.001);
    }

    @Test
    public void testProbability() {
        poisson = new PoissonDistributionImpl(3);
        Assert.assertEquals(0.16803, poisson.probability(1), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        poisson = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.9865, poisson.cumulativeProbability(9), 0.001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        poisson = new PoissonDistributionImpl(3);
        Assert.assertEquals(0.956, poisson.normalApproximateProbability(4), 0.001);
    }

    @Test
    public void testSample() throws MathException {
        poisson = new PoissonDistributionImpl(5);
        int sample = poisson.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        poisson = new PoissonDistributionImpl(5);
        Assert.assertEquals(0, poisson.getDomainLowerBound(0.5), 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        poisson = new PoissonDistributionImpl(5);
        Assert.assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5), 0.001);
    }
}
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.5);
        Assert.assertEquals(3.5, poisson.getMean(), 0.01);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.5);
        Assert.assertEquals(0.216, poisson.probability(2), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.5);
        Assert.assertEquals(0.981, poisson.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.5);
        Assert.assertEquals(0.642, poisson.normalApproximateProbability(2), 0.001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.5);
        int sample = poisson.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.5);
        Assert.assertEquals(0, poisson.getDomainLowerBound(0.1));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.5);
        Assert.assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.1));
    }
}
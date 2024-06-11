import org.apache.commons.math.distribution.PoissonDistributionImpl;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(0.17547, distribution.probability(3), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(0.44049, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(0.48492, distribution.normalApproximateProbability(3), 0.0001);
    }
  
    @Test
    public void testSample() throws Exception {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        int sample = distribution.sample();
        Assert.assertNotNull(sample);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0.1));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.1), 0.0001);
    }
}
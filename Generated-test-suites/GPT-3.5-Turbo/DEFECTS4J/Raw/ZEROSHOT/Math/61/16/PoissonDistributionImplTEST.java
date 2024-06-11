import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {
    private PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new PoissonDistributionImpl(2.5);
    }

    @Test
    public void testGetMean() {
        Assert.assertEquals(2.5, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.0820849986, distribution.probability(1), 0.0001);
        Assert.assertEquals(0.2052124964, distribution.probability(2), 0.0001);
        Assert.assertEquals(0.213909502, distribution.probability(3), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        Assert.assertEquals(0.066833183, distribution.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.2740686191, distribution.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.4889784864, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() {
        Assert.assertEquals(0.4998206451, distribution.normalApproximateProbability(1), 0.0001);
        Assert.assertEquals(0.8018190526, distribution.normalApproximateProbability(2), 0.0001);
        Assert.assertEquals(0.9581615696, distribution.normalApproximateProbability(3), 0.0001);
    }

    @Test
    public void testSample() {
        int sample = distribution.sample();
        Assert.assertTrue(sample >= 0);
        Assert.assertTrue(sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        Assert.assertEquals(0, distribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}
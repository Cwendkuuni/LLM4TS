import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.PoissonDistributionImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl poissonDistribution;

    @Before
    public void setUp() {
        poissonDistribution = new PoissonDistributionImpl(2.5);
    }

    @Test
    public void testGetMean() {
        Assert.assertEquals(2.5, poissonDistribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.0820849986, poissonDistribution.probability(2), 0.0000000001);
    }

    @Test
    public void testProbabilityWithNegativeX() {
        Assert.assertEquals(0.0, poissonDistribution.probability(-1), 0.0001);
    }

    @Test
    public void testProbabilityWithMaxX() {
        Assert.assertEquals(0.0, poissonDistribution.probability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        Assert.assertEquals(0.0668009422, poissonDistribution.cumulativeProbability(2), 0.0000000001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeX() throws MathException {
        Assert.assertEquals(0.0, poissonDistribution.cumulativeProbability(-1), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithMaxX() throws MathException {
        Assert.assertEquals(1.0, poissonDistribution.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        Assert.assertEquals(0.953641141, poissonDistribution.normalApproximateProbability(2), 0.000000001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = poissonDistribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        Assert.assertEquals(0, poissonDistribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        Assert.assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.5));
    }

}
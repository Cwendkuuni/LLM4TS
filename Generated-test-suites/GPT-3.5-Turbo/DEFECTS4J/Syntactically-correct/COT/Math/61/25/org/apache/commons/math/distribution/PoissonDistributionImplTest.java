import org.apache.commons.math.exception.MathException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, distribution.getMean(), EPSILON);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.06679497846086797, distribution.probability(1), EPSILON);
        Assert.assertEquals(0.20521249694973116, distribution.probability(2), EPSILON);
        Assert.assertEquals(0.21376651154080748, distribution.probability(3), EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.0820849986238988, distribution.cumulativeProbability(1), EPSILON);
        Assert.assertEquals(0.28729749557363, distribution.cumulativeProbability(2), EPSILON);
        Assert.assertEquals(0.5010630071143115, distribution.cumulativeProbability(3), EPSILON);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.32465246735834974, distribution.normalApproximateProbability(1), EPSILON);
        Assert.assertEquals(0.6503684795511601, distribution.normalApproximateProbability(2), EPSILON);
        Assert.assertEquals(0.831828789479086, distribution.normalApproximateProbability(3), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample1 = distribution.sample();
        int sample2 = distribution.sample();
        int sample3 = distribution.sample();
        Assert.assertTrue(sample1 >= 0 && sample1 <= Integer.MAX_VALUE);
        Assert.assertTrue(sample2 >= 0 && sample2 <= Integer.MAX_VALUE);
        Assert.assertTrue(sample3 >= 0 && sample3 <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0.5), EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), EPSILON);
    }
}
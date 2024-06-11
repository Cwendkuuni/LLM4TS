import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid argument.
     */
    @Test
    public void testConstructorValidArg() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, dist.getMean(), 0.001);
    }

    /**
     * Test constructor with invalid argument.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorInvalidArg() {
        new PoissonDistributionImpl(-2.5);
    }

    /**
     * Test probability method with valid arguments.
     */
    @Test
    public void testProbabilityValidArgs() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.21881242, dist.probability(0), 0.001);
        Assert.assertEquals(0.11273215, dist.probability(1), 0.001);
        Assert.assertEquals(0.13288306, dist.probability(2), 0.001);
        Assert.assertEquals(0.10136637, dist.probability(3), 0.001);
        Assert.assertEquals(0.05992578, dist.probability(4), 0.001);
        Assert.assertEquals(0.02986344, dist.probability(5), 0.001);
    }

    /**
     * Test probability method with negative argument.
     */
    @Test
    public void testProbabilityNegativeArg() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.0, dist.probability(-1), 0.0);
        Assert.assertEquals(0.0, dist.probability(Integer.MIN_VALUE), 0.0);
    }

    /**
     * Test probability method with large argument.
     */
    @Test
    public void testProbabilityLargeArg() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(StatUtils.ONE_MINUS_SMALL_PROBABILITY, dist.probability(Integer.MAX_VALUE), 0.001);
    }

    /**
     * Test cumulativeProbability method with valid arguments.
     */
    @Test
    public void testCumulativeProbabilityValidArgs() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.0, dist.cumulativeProbability(-1), 0.0);
        Assert.assertEquals(0.36787944, dist.cumulativeProbability(0), 0.001);
        Assert.assertEquals(0.53038252, dist.cumulativeProbability(1), 0.001);
        Assert.assertEquals(0.8145623, dist.cumulativeProbability(2), 0.001);
        Assert.assertEquals(0.97039613, dist.cumulativeProbability(3), 0.001);
        Assert.assertEquals(0.9993166, dist.cumulativeProbability(4), 0.001);
        Assert.assertEquals(1.0, dist.cumulativeProbability(5), 0.001);
    }

    /**
     * Test cumulativeProbability method with negative argument.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeArg() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        dist.cumulativeProbability(-1);
    }

    /**
     * Test normalApproximateProbability method with valid arguments.
     */
    @Test
    public void testNormalApproximateProbabilityValidArgs() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.36787944, dist.normalApproximateProbability(0), 0.001);
        Assert.assertEquals(0.53038252, dist.normalApproximateProbability(1), 0.001);
        Assert.assertEquals(0.8145623, dist.normalApproximateProbability(2), 0.001);
        Assert.assertEquals(0.97039613, dist.normalApproximateProbability(3), 0.001);
        Assert.assertEquals(1.0, dist.normalApproximateProbability(4), 0.001);
    }

    /**
     * Test normalApproximateProbability method with negative argument.
     */
    @Test
    public void testNormalApproximateProbabilityNegativeArg() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.0, dist.normalApproximateProbability(-1), 0.0);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        Assert.assertTrue(sample >= 0);
        Assert.assertTrue(sample <= 1000 * 2.5);
    }
}
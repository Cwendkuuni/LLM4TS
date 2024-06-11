import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImpl() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(5, distribution.getMean(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImplNegativeMean() {
        new PoissonDistributionImpl(-5);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.033689, distribution.probability(2), 1e-6);
    }

    @Test
    public void testProbabilityWithMaxValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.000001, distribution.probability(Integer.MAX_VALUE), 1e-6);
    }

    @Test
    public void testProbabilityWithNegativeValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0, distribution.probability(-2), 1e-6);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.616115, distribution.cumulativeProbability(8), 1e-6);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0, distribution.cumulativeProbability(-2), 1e-6);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.815939, distribution.normalApproximateProbability(8), 1e-6);
    }

    @Test
    public void testNormalApproximateProbabilityWithNegativeValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.00344377, distribution.normalApproximateProbability(-2), 1e-6);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        int sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
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
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        Assert.assertEquals(5, distribution.getMean(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMeanNegative() {
        new PoissonDistributionImpl(-5);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        Assert.assertEquals(0.049787, distribution.probability(1), 0.000001);
        Assert.assertEquals(0.002478752, distribution.probability(2), 0.000000001);
        Assert.assertEquals(0.00012341, distribution.probability(3), 0.00000001);
        Assert.assertEquals(0.000049354, distribution.probability(4), 0.000000001);
    }

    @Test
    public void testProbabilityNegative() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        Assert.assertEquals(0.0, distribution.probability(-2), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        Assert.assertEquals(0.199148267, distribution.cumulativeProbability(1), 0.000000001);
        Assert.assertEquals(0.199148267 + 0.049787068, distribution.cumulativeProbability(2), 0.000000001);
        Assert.assertEquals(0.50, distribution.cumulativeProbability(3), 0.000000001);
        Assert.assertEquals(0.50 + 0.150849837, distribution.cumulativeProbability(4), 0.000000001);
    }

    @Test
    public void testCumulativeProbabilityNegative() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-2), 0);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        Assert.assertEquals(0.778927, distribution.normalApproximateProbability(3), 0.000001);
        Assert.assertEquals(0.875127, distribution.normalApproximateProbability(4), 0.000001);
    }

    @Test
    public void testNormalApproximateProbabilityNegative() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        Assert.assertEquals(0.0, distribution.normalApproximateProbability(-2), 0);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        for (int i = 0; i < 1000; i++) {
            int sample = distribution.sample();
            Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0), 0);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0.5), 0);
        Assert.assertEquals(0, distribution.getDomainLowerBound(1), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0), 0);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1), 0);
    }

    @Test
    public void testInvalidCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        exception.expect(MathException.class);
        exception.expectMessage(LocalizedFormats.GAMMA_OVERFLOW_AFTER_2_ITERATIONS);
        distribution.cumulativeProbability(Integer.MAX_VALUE);
    }

}
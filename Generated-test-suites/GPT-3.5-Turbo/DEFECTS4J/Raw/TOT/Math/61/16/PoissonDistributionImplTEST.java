import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private final double DELTA = 1e-9;

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        Assert.assertEquals(0.2231, distribution.probability(1), DELTA);
        Assert.assertEquals(0.4059, distribution.probability(2), DELTA);
        Assert.assertEquals(0.2052, distribution.probability(3), DELTA);
        Assert.assertEquals(0.0466, distribution.probability(4), DELTA);
        Assert.assertEquals(0.0070, distribution.probability(5), DELTA);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        Assert.assertEquals(0.5580, distribution.cumulativeProbability(1), DELTA);
        Assert.assertEquals(0.9632, distribution.cumulativeProbability(2), DELTA);
        Assert.assertEquals(0.9953, distribution.cumulativeProbability(3), DELTA);
        Assert.assertEquals(0.9987, distribution.cumulativeProbability(4), DELTA);
        Assert.assertEquals(0.9998, distribution.cumulativeProbability(5), DELTA);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        Assert.assertEquals(0.5868, distribution.normalApproximateProbability(1), DELTA);
        Assert.assertEquals(0.9088, distribution.normalApproximateProbability(2), DELTA);
        Assert.assertEquals(0.9938, distribution.normalApproximateProbability(3), DELTA);
        Assert.assertEquals(0.9995, distribution.normalApproximateProbability(4), DELTA);
        Assert.assertEquals(0.9999, distribution.normalApproximateProbability(5), DELTA);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        for (int i = 0; i < 1000; i++) {
            int value = distribution.sample();
            Assert.assertTrue(value >= 0 && value <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0), DELTA);
        Assert.assertEquals(0, distribution.getDomainLowerBound(1), DELTA);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0), DELTA);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1), DELTA);
    }

}
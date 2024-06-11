import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    // Test case for probability method
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);

        Assert.assertEquals(0.27067, distribution.probability(2), 0.0001);
        Assert.assertEquals(0.13534, distribution.probability(1), 0.0001);
        Assert.assertEquals(0.27067, distribution.probability(3), 0.0001);
    }

    // Test case for cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);

        try {
            distribution.cumulativeProbability(-1);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof MathException);
        }

        Assert.assertEquals(0.593994, distribution.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-10), 0.0001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
    }

    // Test case for normalApproximateProbability method
    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);

        try {
            distribution.normalApproximateProbability(-1);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof MathException);
        }

        Assert.assertEquals(0.30854, distribution.normalApproximateProbability(2), 0.0001);
        Assert.assertEquals(0.98214, distribution.normalApproximateProbability(4), 0.0001);
        Assert.assertEquals(1.0, distribution.normalApproximateProbability(Integer.MAX_VALUE), 0.0001);
    }

    // Test case for sample method
    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);

        int sample = distribution.sample();
        Assert.assertTrue(sample >= 0);
        Assert.assertTrue(sample <= Integer.MAX_VALUE);
    }
}
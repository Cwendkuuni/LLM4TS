import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(5.0, poisson.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.0);
        Assert.assertEquals(0.049787, poisson.probability(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);
        try {
            Assert.assertEquals(0.238103, poisson.cumulativeProbability(1), 0.0001);
        } catch (MathException e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(4.0);
        try {
            Assert.assertEquals(0.593349, poisson.normalApproximateProbability(3), 0.0001);
        } catch (MathException e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(6.0);
        try {
            int sample = poisson.sample();
            Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        } catch (MathException e) {
            Assert.fail("Exception thrown");
        }
    }
}
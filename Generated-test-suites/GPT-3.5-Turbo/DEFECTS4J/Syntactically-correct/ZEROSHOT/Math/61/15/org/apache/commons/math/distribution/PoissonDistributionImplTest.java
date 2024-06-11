import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.03368973499542734, poisson.probability(3), 0.000000001);
        Assert.assertEquals(0.1754673697678507, poisson.probability(7), 0.000000001);
        Assert.assertEquals(0.03783327480207024, poisson.probability(4), 0.000000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.2650259152973616, poisson.cumulativeProbability(3), 0.000000001);
        Assert.assertEquals(0.9999385601695072, poisson.cumulativeProbability(10), 0.000000001);
        Assert.assertEquals(0.0951625819640404, poisson.cumulativeProbability(2), 0.000000001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5);
        Assert.assertEquals(0.7852614970923018, poisson.normalApproximateProbability(3), 0.000000001);
        Assert.assertEquals(0.9999451244622407, poisson.normalApproximateProbability(10), 0.000000001);
        Assert.assertEquals(0.8997130068577696, poisson.normalApproximateProbability(2), 0.000000001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5);
        int randomSample = poisson.sample();
        Assert.assertTrue(randomSample >= 0);
        Assert.assertTrue(randomSample <= Integer.MAX_VALUE);
    }

}
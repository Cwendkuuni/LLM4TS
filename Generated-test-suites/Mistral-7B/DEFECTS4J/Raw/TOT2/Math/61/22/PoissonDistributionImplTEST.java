import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, dist.getMean(), 0.01);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double probability = dist.probability(0);
        Assert.assertEquals(0.0738, probability, 0.001);
    }

    @Test
    public void testProbabilityTypical() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double probability = dist.probability(1);
        Assert.assertEquals(0.1361, probability, 0.001);
    }

    @Test
    public void testProbabilityMax() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double probability = dist.probability(Integer.MAX_VALUE);
        Assert.assertEquals(0.0, probability, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeMean() {
        new PoissonDistributionImpl(-2.5);
    }

    @Test
    public void testCumulativeProbabilityTypical() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = 0;
        for (int i = 0; i <= 5; i++) {
            cumulativeProbability += dist.probability(i);
        }
        double calculatedCumulativeProbability = dist.cumulativeProbability(5);
        Assert.assertEquals(cumulativeProbability, calculatedCumulativeProbability, 0.001);
    }

    @Test
    public void testCumulativeProbabilityEdgeCase() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        double calculatedCumulativeProbability = dist.cumulativeProbability(Integer.MAX_VALUE);
        Assert.assertEquals(1.0, calculatedCumulativeProbability, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityException() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(-1.5);
        dist.cumulativeProbability(-1);
    }

    @Test
    public void testSampleTypical() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        Assert.assertEquals(3, sample);
    }

    @Test
    public void testSampleEdgeCase() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1000.0);
        int sample = dist.sample();
        Assert.assertEquals(1000, sample);
    }

    @Test(expected = MathException.class)
    public void testSampleException() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(-1.0);
        dist.sample();
    }

    @Test
    public void testNormalApproximateProbabilityTypical() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = dist.normalApproximateProbability(1);
        Assert.assertEquals(0.1361, normalApproximateProbability, 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityEdgeCase() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(10.0);
        double normalApproximateProbability = dist.normalApproximateProbability(10);
        Assert.assertEquals(0.0788, normalApproximateProbability, 0.001);
    }

    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityException() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.0);
        dist.normalApproximateProbability(1);
    }
}
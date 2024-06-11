import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImplConstructorWithPositiveMean() {
        double mean = 2.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        Assert.assertEquals(mean, poissonDistribution.getMean());
    }

    @Test(expected = MathRuntimeException.class)
    public void testPoissonDistributionImplConstructorWithNegativeMean() {
        double mean = -1.0;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbabilityMethodZero() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double probability = poissonDistribution.probability(0);
        Assert.assertEquals(0.270660256000357, probability, 0.00001);
    }

    @Test
    public void testProbabilityMethodPositiveInteger() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double probability = poissonDistribution.probability(3);
        Assert.assertEquals(0.1821255941179391, probability, 0.00001);
    }

    @Test
    public void testProbabilityMethodLargeInteger() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100.0);
        double probability = poissonDistribution.probability(99);
        Assert.assertEquals(0.000129826072825, probability, 0.00001);
    }

    @Test(expected = MathException.class)
    public void testProbabilityMethodNegativeInteger() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.probability(-1);
    }

    @Test
    public void testCumulativeProbabilityMethodZero() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        Assert.assertEquals(0.3678794411512683, cumulativeProbability, 0.00001);
    }

    @Test
    public void testCumulativeProbabilityMethodOne() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        Assert.assertEquals(0.6821192295635211, cumulativeProbability, 0.00001);
    }

    @Test
    public void testCumulativeProbabilityMethodMaxValue() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
        Assert.assertEquals(1.0, cumulativeProbability, 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityMethodZero() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(0.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(0);
        Assert.assertEquals(0.398942280401433, normalApproximateProbability, 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityMethodPositiveInteger() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(0.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(3);
        double exactProbability = poissonDistribution.probability(3);
        Assert.assertTrue(Precision.abs(normalApproximateProbability - exactProbability) < 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityMethodLargeInteger() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(10.0);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(9);
        double exactProbability = poissonDistribution.probability(9);
        Assert.assertTrue(Precision.abs(normalApproximateProbability - exactProbability) < 0.05);
    }

    @Test
    public void testSampleMethodSmallMean() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        int sample = poissonDistribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= 10);
    }

    @Test
    public void testSampleMethodLargeMean() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100.0);
        int sample = poissonDistribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= 200);
    }

    @Test
    public void testSampleMethodMaxValue() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(Double.MAX_VALUE);
        int sample = poissonDistribution.sample();
        Assert.assertEquals(Integer.MAX_VALUE, sample);
    }
}
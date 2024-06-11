import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImpl_Constructor() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        Assert.assertEquals(1.0, poissonDistribution.getMean(), 0.0);
    }

    @Test
    public void testProbability_Zero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        double probability = poissonDistribution.probability(0);
        Assert.assertEquals(0.03678794411714423E-1, probability, 1e-9);
    }

    @Test
    public void testProbability_Positive() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(1);
        Assert.assertEquals(0.12040508053241538E-1, probability, 1e-9);
    }

    @Test
    public void testProbability_MaxValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(5.0);
        double probability = poissonDistribution.probability(Integer.MAX_VALUE);
        Assert.assertEquals(0.0, probability);
    }

    @Test
    public void testCumulativeProbability_Zero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        Assert.assertEquals(0.0, cumulativeProbability, 1e-11);
    }

    @Test
    public void testCumulativeProbability_SmallPositive() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        Assert.assertEquals(0.3678794411714423E-1, cumulativeProbability, 1e-9);
    }

    @Test
    public void testCumulativeProbability_LargePositive() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(10.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(5);
        Assert.assertEquals(0.4339033234532239E-1, cumulativeProbability, 1e-9);
    }

    @Test
    public void testCumulativeProbability_MaxValue() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(5.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
        Assert.assertEquals(1.0, cumulativeProbability, 1e-9);
    }

    @Test
    public void testNormalApproximateProbability_Zero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(0.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(0);
        Assert.assertEquals(0.03678794411714423E-1, normalApproximateProbability, 1e-9);
    }

    @Test
    public void testNormalApproximateProbability_SmallPositive() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        Assert.assertEquals(0.12040508053241538E-1, normalApproximateProbability, 1e-9);
    }

    @Test
    public void testNormalApproximateProbability_LargePositive() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(10.0);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(5);
        Assert.assertEquals(0.4339033234532239E-1, normalApproximateProbability, 1e-9);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_Negative() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(5.0);
        poissonDistribution.cumulativeProbability(-1);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        Assert.assertEquals(1, sample);
    }

    @Test
    public void testSample_LargeMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(10.0);
        int sample = poissonDistribution.sample();
        Assert.assertEquals(5, sample);
    }
}
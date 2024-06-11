import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0, dist.getMean(), 0);
        Assert.assertEquals(1, dist.getStandardDeviation(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.398942280401431, dist.density(0), 0.00001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.5, dist.cumulativeProbability(0), 0.00001);
        Assert.assertEquals(0.841344746, dist.cumulativeProbability(1), 0.00001);
        Assert.assertEquals(1, dist.cumulativeProbability(5), 0.00001);
        Assert.assertEquals(0, dist.cumulativeProbability(-5), 0.00001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(100);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1.280661, dist.inverseCumulativeProbability(0.8413447), 0.0001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(1.2);
    }

    @Test
    public void testSample() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Random rand = new Random();
        double sample = dist.sample(rand);
        Assert.assertEquals(0, sample, 0.01);
        sample = dist.sample(rand);
        Assert.assertEquals(1, sample, 0.01);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 0);
    }
}
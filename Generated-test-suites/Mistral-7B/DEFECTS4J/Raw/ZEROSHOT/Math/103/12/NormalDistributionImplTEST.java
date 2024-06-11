import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionCreation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(0.0, normalDistribution.getMean(), 0.01);
        Assert.assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.01);

        NormalDistribution normalDistributionWithMeanAndSD = new NormalDistributionImpl(1.2, 0.5);
        Assert.assertEquals(1.2, normalDistributionWithMeanAndSD.getMean(), 0.01);
        Assert.assertEquals(0.5, normalDistributionWithMeanAndSD.getStandardDeviation(), 0.01);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);

        Assert.assertEquals(0.5, normalDistribution.cumulativeProbability(0.0), 0.01);
        Assert.assertEquals(0.841345, normalDistribution.cumulativeProbability(0.5), 0.001);
        Assert.assertEquals(0.977245, normalDistribution.cumulativeProbability(1.0), 0.001);

        Assert.assertEquals(0.1587, normalDistribution.cumulativeProbability(-1.0), 0.001);
        Assert.assertEquals(0.0, normalDistribution.cumulativeProbability(-3.0), 0.001);
        Assert.assertEquals(1.0, normalDistribution.cumulativeProbability(3.0), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithLargeX() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double largeX = 100.0;
        normalDistribution.cumulativeProbability(largeX);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);

        Assert.assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), 0.0);
        Assert.assertEquals(0.0, normalDistribution.inverseCumulativeProbability(0.5), 0.001);
        Assert.assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1.0), 0.0);

        Assert.assertEquals(-0.842753, normalDistribution.inverseCumulativeProbability(0.1587), 0.001);
        Assert.assertEquals(-1.2, normalDistribution.inverseCumulativeProbability(0.0), 0.01);
        Assert.assertEquals(1.2, normalDistribution.inverseCumulativeProbability(0.977245), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double invalidProbability = 1.2;
        normalDistribution.inverseCumulativeProbability(invalidProbability);
    }
}
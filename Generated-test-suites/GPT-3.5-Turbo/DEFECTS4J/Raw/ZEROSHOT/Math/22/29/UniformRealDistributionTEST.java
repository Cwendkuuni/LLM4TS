import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertEquals(1.0, dist.density(0.5), 0.0001);
        Assert.assertEquals(0.0, dist.density(-0.5), 0.0001);
        Assert.assertEquals(0.0, dist.density(1.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.0, dist.cumulativeProbability(-0.5), 0.0001);
        Assert.assertEquals(0.5, dist.cumulativeProbability(0.5), 0.0001);
        Assert.assertEquals(1.0, dist.cumulativeProbability(1.5), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.08333333333333333, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.0, dist.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertEquals(1.0, dist.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        Assert.assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double sample = dist.sample();
        Assert.assertTrue(sample >= 0.0);
        Assert.assertTrue(sample < 1.0);
    }
}
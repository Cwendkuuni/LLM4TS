import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(1.0, distribution.density(0.5), 0.000001);
        Assert.assertEquals(0.0, distribution.density(2), 0.000001);
        Assert.assertEquals(0.0, distribution.density(-1), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.000001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(2), 0.000001);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-1), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.5, distribution.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.08333333333333333, distribution.getNumericalVariance(), 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), 0.000001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(1.0, distribution.getSupportUpperBound(), 0.000001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        Assert.assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= 1);
    }

}
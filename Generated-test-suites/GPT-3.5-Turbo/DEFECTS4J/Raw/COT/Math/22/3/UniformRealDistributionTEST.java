
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        double result = distribution.density(0.5);
        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        double result = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.5, result, 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        double result = distribution.getNumericalMean();
        Assert.assertEquals(5.0, result, 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        double result = distribution.getNumericalVariance();
        Assert.assertEquals(8.333333333333334, result, 0.000000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        double result = distribution.getSupportLowerBound();
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        double result = distribution.getSupportUpperBound();
        Assert.assertEquals(10.0, result, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        boolean result = distribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        boolean result = distribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        boolean result = distribution.isSupportConnected();
        Assert.assertTrue(result);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double result = distribution.sample();
        Assert.assertTrue(result >= 0 && result <= 1);
    }
}


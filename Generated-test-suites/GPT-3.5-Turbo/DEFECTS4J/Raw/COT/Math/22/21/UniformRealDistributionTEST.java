import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double density = distribution.density(0.5);
        Assert.assertEquals(1, density, 0.0000001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double cumulativeProbability = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.5, cumulativeProbability, 0.0000001);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double solverAbsoluteAccuracy = distribution.getSolverAbsoluteAccuracy();
        Assert.assertEquals(1e-9, solverAbsoluteAccuracy, 0.0000001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double numericalMean = distribution.getNumericalMean();
        Assert.assertEquals(0.5, numericalMean, 0.0000001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double numericalVariance = distribution.getNumericalVariance();
        Assert.assertEquals(1.0 / 12.0, numericalVariance, 0.0000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double supportLowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(0, supportLowerBound, 0.0000001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double supportUpperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(1, supportUpperBound, 0.0000001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean isSupportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean isSupportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean isSupportConnected = distribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample < 1);
    }
}
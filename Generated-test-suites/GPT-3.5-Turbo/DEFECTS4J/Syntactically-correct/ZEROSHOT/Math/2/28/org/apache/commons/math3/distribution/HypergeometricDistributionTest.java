import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(10, dist.getPopulationSize());
        Assert.assertEquals(5, dist.getNumberOfSuccesses());
        Assert.assertEquals(3, dist.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroPopulationSize() {
        new HypergeometricDistribution(0, 5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeSampleSize() {
        new HypergeometricDistribution(10, 5, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 12, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.44999999999, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.23809523809, dist.probability(0), 0.0001);
        Assert.assertEquals(0.50264550264, dist.probability(1), 0.0001);
        Assert.assertEquals(0.23809523809, dist.probability(2), 0.0001);
        Assert.assertEquals(0.02164502164, dist.probability(3), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.23809523809, dist.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.74074074074, dist.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.97883597883, dist.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.76190476190, dist.upperCumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.23809523809, dist.upperCumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.02116402116, dist.upperCumulativeProbability(3), 0.0001);
    }
}
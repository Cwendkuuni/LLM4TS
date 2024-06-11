
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructors() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(10, dist.getPopulationSize());
        Assert.assertEquals(5, dist.getNumberOfSuccesses());
        Assert.assertEquals(3, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.24242424242424243, dist.cumulativeProbability(1), 0.00001);
        Assert.assertEquals(0.6060606060606061, dist.cumulativeProbability(2), 0.00001);
        Assert.assertEquals(0.8484848484848485, dist.cumulativeProbability(3), 0.00001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.24242424242424243, dist.probability(1), 0.00001);
        Assert.assertEquals(0.36363636363636365, dist.probability(2), 0.00001);
        Assert.assertEquals(0.24242424242424243, dist.probability(3), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.7575757575757576, dist.upperCumulativeProbability(1), 0.00001);
        Assert.assertEquals(0.3939393939393939, dist.upperCumulativeProbability(2), 0.00001);
        Assert.assertEquals(0.15151515151515152, dist.upperCumulativeProbability(3), 0.00001);
    }

    @Test
    public void testMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.5, dist.getNumericalMean(), 0.00001);
    }

    @Test
    public void testVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.375, dist.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(2, dist.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertTrue(dist.isSupportConnected());
    }
}

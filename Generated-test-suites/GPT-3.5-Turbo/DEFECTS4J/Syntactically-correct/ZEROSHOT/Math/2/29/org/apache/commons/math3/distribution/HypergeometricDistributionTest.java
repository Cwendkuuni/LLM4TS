import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        double cumulativeProbability = distribution.cumulativeProbability(2);
        Assert.assertEquals(0.842105263, cumulativeProbability, 0.000001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        double probability = distribution.probability(2);
        Assert.assertEquals(0.0857142857, probability, 0.000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        double upperCumulativeProbability = distribution.upperCumulativeProbability(2);
        Assert.assertEquals(0.157894736, upperCumulativeProbability, 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        double numericalMean = distribution.getNumericalMean();
        Assert.assertEquals(1.5, numericalMean, 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        double numericalVariance = distribution.getNumericalVariance();
        Assert.assertEquals(0.607142857, numericalVariance, 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        int supportLowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(0, supportLowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        int supportUpperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(3, supportUpperBound);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        boolean isSupportConnected = distribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}
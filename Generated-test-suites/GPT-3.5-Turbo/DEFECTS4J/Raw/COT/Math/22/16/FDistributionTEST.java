import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(1, 1);
        double density = distribution.density(2);
        Assert.assertEquals(1.0 / 9, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(1, 1);
        double cumulativeProbability = distribution.cumulativeProbability(2);
        Assert.assertEquals(2.0 / 3, cumulativeProbability, 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3, 4);
        double numeratorDegreesOfFreedom = distribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(3, numeratorDegreesOfFreedom, 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3, 4);
        double denominatorDegreesOfFreedom = distribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(4, denominatorDegreesOfFreedom, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(3, 4);
        double numericalMean = distribution.getNumericalMean();
        Assert.assertEquals(4, numericalMean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(3, 4);
        double numericalVariance = distribution.getNumericalVariance();
        Assert.assertEquals(800.0, numericalVariance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(3, 4);
        double supportLowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(0, supportLowerBound, 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(3, 4);
        double supportUpperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3, 4);
        boolean isSupportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3, 4);
        boolean isSupportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3, 4);
        boolean isSupportConnected = distribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}
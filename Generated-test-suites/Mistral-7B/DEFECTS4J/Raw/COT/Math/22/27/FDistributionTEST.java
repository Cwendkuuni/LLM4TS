import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistributionConstructor() throws NotStrictlyPositiveException {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        Assert.assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double density = fDistribution.density(1.5);
        Assert.assertNotNull(density);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorNegativeDegreesOfFreedom()
        throws NotStrictlyPositiveException {
        FDistribution fDistribution = new FDistribution(-3.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorZeroDegreesOfFreedom()
        throws NotStrictlyPositiveException {
        FDistribution fDistribution = new FDistribution(0.0, 5.0);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double cumulativeProbability = fDistribution.cumulativeProbability(1.5);
        Assert.assertNotNull(cumulativeProbability);
    }

    @Test
    public void testFDistributionGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double numericalMean = fDistribution.getNumericalMean();
        Assert.assertEquals(3.5, numericalMean, 0.0);
    }

    @Test
    public void testFDistributionGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double numericalVariance = fDistribution.getNumericalVariance();
        Assert.assertEquals(1.795875527223691, numericalVariance, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorNegativeNumericalVarianceIsCalculated()
        throws NotStrictlyPositiveException {
        FDistribution fDistribution = new FDistribution(new Well19937c(), -3.0, 5.0);
    }

    @Test
    public void testFDistributionGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double lowerBound = fDistribution.getSupportLowerBound();
        Assert.assertEquals(0.0, lowerBound, 0.0);
    }

    @Test
    public void testFDistributionGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double upperBound = fDistribution.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, upperBound);
    }

    @Test
    public void testFDistributionIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);
    }

    @Test
    public void testFDistributionIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportUpperBoundInclusive);
    }

    @Test
    public void testFDistributionIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        boolean isSupportConnected = fDistribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}
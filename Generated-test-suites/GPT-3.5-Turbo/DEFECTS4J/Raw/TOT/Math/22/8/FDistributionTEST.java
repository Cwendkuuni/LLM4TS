import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.distribution.FDistribution;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(0.5, fDistribution.density(1), 0.0001);

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(0.061178, fDistribution.density(1), 0.0001);

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(0.082756, fDistribution.density(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(1, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(0.758036, fDistribution.cumulativeProbability(1), 0.0001);

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(0.652462, fDistribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(1, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(2, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(5, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(1, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(4, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException1() {
        new FDistribution(0, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException2() {
        new FDistribution(1, -5);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(Double.NaN, fDistribution.getNumericalMean(), 0.0001);

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(4, fDistribution.getNumericalMean(), 0.0001);

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(10, fDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(Double.NaN, fDistribution.getNumericalVariance(), 0.0001);

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(32, fDistribution.getNumericalVariance(), 0.0001);

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(48, fDistribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(0, fDistribution.getSupportLowerBound(), 0.0001);

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(0, fDistribution.getSupportLowerBound(), 0.0001);

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(0, fDistribution.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0001);

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0001);

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(true, fDistribution.isSupportLowerBoundInclusive());

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(true, fDistribution.isSupportLowerBoundInclusive());

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(true, fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(false, fDistribution.isSupportUpperBoundInclusive());

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(false, fDistribution.isSupportUpperBoundInclusive());

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(false, fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(1, 1);
        Assert.assertEquals(true, fDistribution.isSupportConnected());

        fDistribution = new FDistribution(2, 4);
        Assert.assertEquals(true, fDistribution.isSupportConnected());

        fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(true, fDistribution.isSupportConnected());
    }
}
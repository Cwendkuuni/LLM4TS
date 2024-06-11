
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertEquals(0.0, fDistribution.density(-1.0), 0.0001);
        Assert.assertEquals(0.1363, fDistribution.density(1.0), 0.0001);
        Assert.assertEquals(0.0602, fDistribution.density(2.0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 0.0001);
        Assert.assertEquals(0.0674, fDistribution.cumulativeProbability(1.0), 0.0001);
        Assert.assertEquals(0.1266, fDistribution.cumulativeProbability(2.0), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertEquals(10, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertEquals(20, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertEquals(1.53846153846, fDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertEquals(16.875, fDistribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(10, 20);
        Assert.assertTrue(fDistribution.isSupportConnected());
    }
}

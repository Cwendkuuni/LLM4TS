import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(0.05077588003225688, fDistribution.density(0.5), 1e-9);
        Assert.assertEquals(0.11203565029177942, fDistribution.density(1.0), 1e-9);
        Assert.assertEquals(0.14098364152929254, fDistribution.density(1.5), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(0.07097553924377514, fDistribution.cumulativeProbability(0.5), 1e-9);
        Assert.assertEquals(0.206700646641168, fDistribution.cumulativeProbability(1.0), 1e-9);
        Assert.assertEquals(0.32124372135939045, fDistribution.cumulativeProbability(1.5), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(5, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(1.25, fDistribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(2.7777777777777777, fDistribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(0, fDistribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        Assert.assertTrue(fDistribution.isSupportConnected());
    }
}
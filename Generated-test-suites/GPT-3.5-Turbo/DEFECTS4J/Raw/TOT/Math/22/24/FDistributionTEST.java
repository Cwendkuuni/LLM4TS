import org.apache.commons.math3.distribution.FDistribution;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(2, 3);

        // Typical use case
        Assert.assertEquals(0.048572, fDistribution.density(1.0), 0.000001);

        // Edge case
        Assert.assertEquals(0.0, fDistribution.density(0.0), 0.000001);

        // Error scenario
        Assert.assertEquals(0.0, fDistribution.density(-1.0), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(2, 3);

        // Typical use case
        Assert.assertEquals(0.493654, fDistribution.cumulativeProbability(1.0), 0.000001);

        // Edge case
        Assert.assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.000001);

        // Error scenario
        Assert.assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 0.000001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2, 3);

        Assert.assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.000001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2, 3);

        Assert.assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(2, 3);

        // Typical use case
        Assert.assertEquals(3.0, fDistribution.getNumericalMean(), 0.000001);

        // Edge case
        FDistribution edgeCaseDistribution = new FDistribution(1, 2);
        Assert.assertEquals(Double.NaN, edgeCaseDistribution.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(2, 3);

        // Typical use case
        Assert.assertEquals(18.0, fDistribution.getNumericalVariance(), 0.000001);

        // Edge case
        FDistribution edgeCaseDistribution = new FDistribution(1, 2);
        Assert.assertEquals(Double.NaN, edgeCaseDistribution.getNumericalVariance(), 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(2, 3);

        Assert.assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.000001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(2, 3);

        Assert.assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.000001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2, 3);

        Assert.assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2, 3);

        Assert.assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(2, 3);

        Assert.assertTrue(fDistribution.isSupportConnected());
    }

}
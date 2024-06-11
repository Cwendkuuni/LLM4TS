import org.apache.commons.math3.distribution.FDistribution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {

    private FDistribution fDistribution;

    @Before
    public void setup() {
        fDistribution = new FDistribution(10, 20);
    }

    @Test
    public void testDensity() {
        double x = 1.0;
        double density = fDistribution.density(x);
        Assert.assertEquals(0.17669237188349704, density, 0.000000000000001);

        x = 0.5;
        density = fDistribution.density(x);
        Assert.assertEquals(0.30870645363917957, density, 0.000000000000001);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 1.0;
        double cumulativeProbability = fDistribution.cumulativeProbability(x);
        Assert.assertEquals(0.4518627618776062, cumulativeProbability, 0.000000000000001);

        x = 0.5;
        cumulativeProbability = fDistribution.cumulativeProbability(x);
        Assert.assertEquals(0.33078156477773915, cumulativeProbability, 0.000000000000001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(10, numeratorDegreesOfFreedom, 0.000000000000001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(20, denominatorDegreesOfFreedom, 0.000000000000001);
    }

    @Test
    public void testGetNumericalMean() {
        double numericalMean = fDistribution.getNumericalMean();
        Assert.assertEquals(1.25, numericalMean, 0.000000000000001);
    }

    @Test
    public void testGetNumericalVariance() {
        double numericalVariance = fDistribution.getNumericalVariance();
        Assert.assertEquals(0.4320987654320988, numericalVariance, 0.000000000000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        double supportLowerBound = fDistribution.getSupportLowerBound();
        Assert.assertEquals(0, supportLowerBound, 0.000000000000001);
    }

    @Test
    public void testGetSupportUpperBound() {
        double supportUpperBound = fDistribution.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0.000000000000001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        boolean isSupportConnected = fDistribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}
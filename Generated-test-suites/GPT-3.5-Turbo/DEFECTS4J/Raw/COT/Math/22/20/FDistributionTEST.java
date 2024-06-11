
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {
    private FDistribution fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistribution(3, 5);
    }

    @Test
    public void testDensity() {
        double x = 2;
        double expectedDensity = FastMath.exp(0.6996);
        double calculatedDensity = fDistribution.density(x);
        Assert.assertEquals(expectedDensity, calculatedDensity, 1e-4);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 3;
        double expectedCumulativeProbability = 0.3883;
        double calculatedCumulativeProbability = fDistribution.cumulativeProbability(x);
        Assert.assertEquals(expectedCumulativeProbability, calculatedCumulativeProbability, 1e-4);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double expectedDegreesOfFreedom = 3;
        double calculatedDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(expectedDegreesOfFreedom, calculatedDegreesOfFreedom, 1e-4);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double expectedDegreesOfFreedom = 5;
        double calculatedDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(expectedDegreesOfFreedom, calculatedDegreesOfFreedom, 1e-4);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        double expectedAccuracy = 1e-9;
        double calculatedAccuracy = fDistribution.getSolverAbsoluteAccuracy();
        Assert.assertEquals(expectedAccuracy, calculatedAccuracy, 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        double expectedMean = 1.25;
        double calculatedMean = fDistribution.getNumericalMean();
        Assert.assertEquals(expectedMean, calculatedMean, 1e-2);
    }

    @Test
    public void testGetNumericalVariance() {
        double expectedVariance = 3.4722222;
        double calculatedVariance = fDistribution.getNumericalVariance();
        Assert.assertEquals(expectedVariance, calculatedVariance, 1e-4);
    }

    @Test
    public void testGetSupportLowerBound() {
        double expectedLowerBound = 0;
        double calculatedLowerBound = fDistribution.getSupportLowerBound();
        Assert.assertEquals(expectedLowerBound, calculatedLowerBound, 1e-4);
    }

    @Test
    public void testGetSupportUpperBound() {
        double expectedUpperBound = Double.POSITIVE_INFINITY;
        double calculatedUpperBound = fDistribution.getSupportUpperBound();
        Assert.assertEquals(expectedUpperBound, calculatedUpperBound, 1e-4);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean isLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean isUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        boolean isSupportConnected = fDistribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}

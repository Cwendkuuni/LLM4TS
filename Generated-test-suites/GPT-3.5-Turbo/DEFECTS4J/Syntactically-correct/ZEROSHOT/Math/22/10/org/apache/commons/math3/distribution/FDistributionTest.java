import org.apache.commons.math3.distribution.FDistribution;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    private static final double EPSILON = 1e-9;

    private FDistribution fdistribution = new FDistribution(5, 10);

    @Test
    public void testDensity() {
        double x = 3.5;
        double expectedDensity = 0.016411858716104674;
        double actualDensity = fdistribution.density(x);
        Assert.assertEquals(expectedDensity, actualDensity, EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 3.5;
        double expectedCumulativeProbability = 0.9292018038440126;
        double actualCumulativeProbability = fdistribution.cumulativeProbability(x);
        Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, EPSILON);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double expectedNumeratorDegreesOfFreedom = 5;
        double actualNumeratorDegreesOfFreedom = fdistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(expectedNumeratorDegreesOfFreedom, actualNumeratorDegreesOfFreedom, EPSILON);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double expectedDenominatorDegreesOfFreedom = 10;
        double actualDenominatorDegreesOfFreedom = fdistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(expectedDenominatorDegreesOfFreedom, actualDenominatorDegreesOfFreedom, EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        double expectedNumericalMean = 1.25;
        double actualNumericalMean = fdistribution.getNumericalMean();
        Assert.assertEquals(expectedNumericalMean, actualNumericalMean, EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        double expectedNumericalVariance = 1.875;
        double actualNumericalVariance = fdistribution.getNumericalVariance();
        Assert.assertEquals(expectedNumericalVariance, actualNumericalVariance, EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        double expectedSupportLowerBound = 0;
        double actualSupportLowerBound = fdistribution.getSupportLowerBound();
        Assert.assertEquals(expectedSupportLowerBound, actualSupportLowerBound, EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        double expectedSupportUpperBound = Double.POSITIVE_INFINITY;
        double actualSupportUpperBound = fdistribution.getSupportUpperBound();
        Assert.assertEquals(expectedSupportUpperBound, actualSupportUpperBound, EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean expectedIsSupportLowerBoundInclusive = true;
        boolean actualIsSupportLowerBoundInclusive = fdistribution.isSupportLowerBoundInclusive();
        Assert.assertEquals(expectedIsSupportLowerBoundInclusive, actualIsSupportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean expectedIsSupportUpperBoundInclusive = false;
        boolean actualIsSupportUpperBoundInclusive = fdistribution.isSupportUpperBoundInclusive();
        Assert.assertEquals(expectedIsSupportUpperBoundInclusive, actualIsSupportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        boolean expectedIsSupportConnected = true;
        boolean actualIsSupportConnected = fdistribution.isSupportConnected();
        Assert.assertEquals(expectedIsSupportConnected, actualIsSupportConnected);
    }
}
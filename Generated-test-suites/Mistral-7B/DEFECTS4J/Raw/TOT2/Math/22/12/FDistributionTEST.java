import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {

    private static final double EPSILON = 1e-9;
    private FDistribution fDist;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        fDist = new FDistribution(3.0, 4.0);
    }

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        FDistribution fDist1 = new FDistribution(3.0, 4.0);
        Assert.assertEquals(3.0, fDist1.getNumeratorDegreesOfFreedom(), EPSILON);
        Assert.assertEquals(4.0, fDist1.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testDensity() {
        double x = 1.5;
        double density = fDist.density(x);
        double expectedDensity = FastMath.exp(3.0 * FastMath.log(x) - FastMath.log(2.0) * 3.0 +
                             FastMath.log(2.0) * 4.0 - FastMath.log(16.0 * x + 9.0) -
                             FastMath.log(16.0 * x + 9.0) - Beta.logBeta(3.0, 4.0));

        Assert.assertEquals(expectedDensity, density, EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 1.5;
        double cumProb = fDist.cumulativeProbability(x);
        double expectedCumProb = Beta.regularizedBeta((3.0 * x) / (4.0 + 3.0 * x), 1.5, 1.5);

        Assert.assertEquals(expectedCumProb, cumProb, EPSILON);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        Assert.assertEquals(3.0, fDist.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        Assert.assertEquals(4.0, fDist.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        Assert.assertEquals(EPSILON, fDist.getSolverAbsoluteAccuracy(), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(4.0 / 3.0, fDist.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(2.25, fDist.getNumericalVariance(), EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeNumeratorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-3.0, 4.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDenominatorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(3.0, -4.0);
    }

    @Test
    public void testDistributionFunctionAtZero() {
        double cumProb = fDist.cumulativeProbability(0);
        double expectedCumProb = Beta.regularizedBeta(0, 1.5, 1.5);

        Assert.assertEquals(expectedCumProb, cumProb, EPSILON);
    }

    @Test
    public void testDistributionFunctionAtInfinity() {
        double cumProb = fDist.cumulativeProbability(Double.POSITIVE_INFINITY);
        double expectedCumProb = 1.0;

        Assert.assertEquals(expectedCumProb, cumProb, EPSILON);
    }

    @Test
    public void testDistributionFunctionAtLargeValue() {
        double x = 100.0;
        double cumProb = fDist.cumulativeProbability(x);

        double expectedCumProb = Beta.regularizedBeta((3.0 * x) / (4.0 + 3.0 * x), 1.5, 1.5);

        Assert.assertEquals(expectedCumProb, cumProb, EPSILON);
    }

    @Test
    public void testDensityAtZero() {
        double density = fDist.density(0);
        double expectedDensity = Beta.logBeta(1.5, 1.5);

        Assert.assertEquals(expectedDensity, density, EPSILON);
    }

    @Test
    public void testDensityAtLargeValue() {
        double x = 100.0;
        double density = fDist.density(x);

        double expectedDensity = FastMath.exp(3.0 * FastMath.log(x) - FastMath.log(2.0) * 3.0 +
                             FastMath.log(2.0) * 4.0 - FastMath.log(16.0 * x + 9.0) -
                             FastMath.log(16.0 * x + 9.0) - Beta.logBeta(3.0, 4.0));

        Assert.assertEquals(expectedDensity, density, EPSILON);
    }
}
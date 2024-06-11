import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution fDist = new FDistribution(2.0, 4.0);
            Assert.assertNotNull(fDist);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Creation of FDistribution with valid parameters should not throw exception.");
        }

        try {
            FDistribution fDist = new FDistribution(1.0, 4.0);
            Assert.fail("Creation of FDistribution with invalid parameter should throw NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            FDistribution fDist = new FDistribution(-1.0, 4.0);
            Assert.fail("Creation of FDistribution with invalid parameter should throw NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            FDistribution fDist = new FDistribution(2.0, 1.0);
            Assert.fail("Creation of FDistribution with invalid parameter should throw NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            FDistribution fDist = new FDistribution(new Well19937c(), 2.0, 4.0);
            Assert.assertNotNull(fDist);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Creation of FDistribution with valid parameters should not throw exception.");
        }

        try {
            FDistribution fDist = new FDistribution(new Well19937c(), 1.0, 4.0);
            Assert.fail("Creation of FDistribution with invalid parameter should throw NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            FDistribution fDist = new FDistribution(new Well19937c(), -1.0, 4.0);
            Assert.fail("Creation of FDistribution with invalid parameter should throw NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            FDistribution fDist = new FDistribution(new Well19937c(), 2.0, 1.0);
            Assert.fail("Creation of FDistribution with invalid parameter should throw NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double density = fDist.density(1.5);
        double expected = FastMath.exp(3.0 * FastMath.log(3.0) + 3.0 * FastMath.log(1.5) -
                                       FastMath.log(1.5) + 2.0 * FastMath.log(2.0) -
                                       3.0 * FastMath.log(1.5 + 3.0 * 1.5) - 2.0 * FastMath.log(1.5 + 3.0 * 1.5) -
                                       Beta.logBeta(3.0, 2.0));
        Assert.assertEquals(expected, density, 1e-9);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double cumulativeProbability = fDist.cumulativeProbability(1.5);
        double expected = Beta.regularizedBeta(3.0 * 1.5 / (4.0 + 3.0 * 1.5), 1.5, 1.5);
        Assert.assertEquals(expected, cumulativeProbability, 1e-9);
    }

    @Test
    public void testFDistributionGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double numeratorDegreesOfFreedom = fDist.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(3.0, numeratorDegreesOfFreedom, 1e-9);
    }

    @Test
    public void testFDistributionGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double denominatorDegreesOfFreedom = fDist.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(4.0, denominatorDegreesOfFreedom, 1e-9);
    }

    @Test
    public void testFDistributionGetSolverAbsoluteAccuracy() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double solverAbsoluteAccuracy = fDist.getSolverAbsoluteAccuracy();
        Assert.assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, solverAbsoluteAccuracy, 1e-9);
    }

    @Test
    public void testFDistributionNumericalMean() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double numericalMean = fDist.getNumericalMean();
        Assert.assertEquals(4.0 / 2.0, numericalMean, 1e-9);
    }

    @Test
    public void testFDistributionNumericalVariance() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double numericalVariance = fDist.getNumericalVariance();
        double expected = (2.0 * Math.pow(4.0, 2.0) * (3.0 + 4.0)) /
                         ((3.0 * (4.0 - 2.0) * (4.0 - 2.0) * (4.0 - 4.0)));
        Assert.assertEquals(expected, numericalVariance, 1e-9);
    }

    @Test
    public void testFDistributionGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double lowerBound = fDist.getSupportLowerBound();
        Assert.assertEquals(0.0, lowerBound, 1e-9);
    }

    @Test
    public void testFDistributionGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        double upperBound = fDist.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-9);
    }

    @Test
    public void testFDistributionIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        boolean inclusive = fDist.isSupportLowerBoundInclusive();
        Assert.assertTrue(inclusive);
    }

    @Test
    public void testFDistributionIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        boolean inclusive = fDist.isSupportUpperBoundInclusive();
        Assert.assertFalse(inclusive);
    }

    @Test
    public void testFDistributionIsSupportConnected() {
        FDistribution fDist = new FDistribution(3.0, 4.0);
        boolean connected = fDist.isSupportConnected();
        Assert.assertTrue(connected);
    }
}
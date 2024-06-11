import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {

    // Test constructor with valid inputs
    @Test
    public void testConstructor_validInputs() {
        try {
            FDistribution fDistribution = new FDistribution(3.0, 6.0);
            Assert.assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
            Assert.assertEquals(6.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
            Assert.assertEquals(1e-9, fDistribution.getSolverAbsoluteAccuracy(), 0.0001);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Exception thrown for valid inputs");
        }
    }

    // Test constructor with invalid inputs
    @Test
    public void testConstructor_invalidInputs() {
        try {
            FDistribution fDistribution = new FDistribution(0.0, 6.0);
            Assert.fail("No exception thrown for zero numerator degrees of freedom");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception thrown
        }

        try {
            FDistribution fDistribution = new FDistribution(3.0, 0.0);
            Assert.fail("No exception thrown for zero denominator degrees of freedom");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception thrown
        }
    }

    // Test density method
    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);

        // Test typical use case
        Assert.assertEquals(0.127483, fDistribution.density(1.0), 0.000001);

        // Test edge cases
        Assert.assertEquals(Double.POSITIVE_INFINITY, fDistribution.density(0.0), 0.000001);
        Assert.assertEquals(0.0, fDistribution.density(Double.POSITIVE_INFINITY), 0.000001);

        // Test error scenario
        try {
            fDistribution.density(Double.NaN);
            Assert.fail("No exception thrown for NaN input");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);

        // Test typical use case
        Assert.assertEquals(0.373238, fDistribution.cumulativeProbability(1.0), 0.000001);

        // Test edge case
        Assert.assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.000001);

        // Test error scenario
        try {
            fDistribution.cumulativeProbability(Double.NaN);
            Assert.fail("No exception thrown for NaN input");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    // Test getNumeratorDegreesOfFreedom method
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(3.0, 6.0);
        Assert.assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    // Test getDenominatorDegreesOfFreedom method
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(3.0, 6.0);
        Assert.assertEquals(6.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    // Test getSolverAbsoluteAccuracy method
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fDistribution = new FDistribution(3.0, 6.0);
        Assert.assertEquals(1e-9, fDistribution.getSolverAbsoluteAccuracy(), 0.0001);
    }

    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution1 = new FDistribution(3.0, 6.0);
        Assert.assertEquals(6.0, fDistribution1.getNumericalMean(), 0.0001);

        FDistribution fDistribution2 = new FDistribution(1.0, 6.0);
        Assert.assertEquals(Double.NaN, fDistribution2.getNumericalMean(), 0.0001);
    }

    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution1 = new FDistribution(5.0, 10.0);
        Assert.assertEquals(100.0, fDistribution1.getNumericalVariance(), 0.0001);

        FDistribution fDistribution2 = new FDistribution(1.0, 6.0);
        Assert.assertEquals(Double.NaN, fDistribution2.getNumericalVariance(), 0.0001);
    }

    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(3.0, 6.0);
        Assert.assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0001);
    }

    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(3.0, 6.0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0001);
    }

    // Test isSupportLowerBoundInclusive method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 6.0);
        Assert.assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    // Test isSupportUpperBoundInclusive method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 6.0);
        Assert.assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(3.0, 6.0);
        Assert.assertTrue(fDistribution.isSupportConnected());
    }
}
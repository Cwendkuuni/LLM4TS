import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;

public class FDistributionTest {

    // Test case for the constructor with positive degrees of freedom
    @Test
    public void testConstructorWithPositiveDegreesOfFreedom() {
        try {
            FDistribution fDistribution = new FDistribution(2, 3);
            Assert.assertEquals(2, fDistribution.getNumeratorDegreesOfFreedom(), 0);
            Assert.assertEquals(3, fDistribution.getDenominatorDegreesOfFreedom(), 0);
        } catch (Exception e) {
            Assert.fail("Constructor should not throw an exception for positive degrees of freedom");
        }
    }

    // Test case for the constructor with zero degrees of freedom
    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testConstructorWithZeroDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(0, 0);
    }

    // Test case for the constructor with negative degrees of freedom
    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(-1, -2);
    }

    // Test case for the density method with positive input
    @Test
    public void testDensityWithPositiveInput() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double density = fDistribution.density(1.5);
        Assert.assertEquals(0.32163, density, 0.00001);
    }

    // Test case for the density method with zero input
    @Test
    public void testDensityWithZeroInput() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double density = fDistribution.density(0);
        Assert.assertEquals(0, density, 0);
    }

    // Test case for the density method with negative input
    @Test
    public void testDensityWithNegativeInput() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double density = fDistribution.density(-1.5);
        Assert.assertEquals(0, density, 0);
    }

    // Test case for the cumulativeProbability method with positive input
    @Test
    public void testCumulativeProbabilityWithPositiveInput() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double cumulativeProbability = fDistribution.cumulativeProbability(1.5);
        Assert.assertEquals(0.77140, cumulativeProbability, 0.00001);
    }

    // Test case for the cumulativeProbability method with zero input
    @Test
    public void testCumulativeProbabilityWithZeroInput() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double cumulativeProbability = fDistribution.cumulativeProbability(0);
        Assert.assertEquals(0, cumulativeProbability, 0);
    }

    // Test case for the cumulativeProbability method with negative input
    @Test
    public void testCumulativeProbabilityWithNegativeInput() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double cumulativeProbability = fDistribution.cumulativeProbability(-1.5);
        Assert.assertEquals(0, cumulativeProbability, 0);
    }

    // Test case for the getNumeratorDegreesOfFreedom method
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(2, numeratorDegreesOfFreedom, 0);
    }

    // Test case for the getDenominatorDegreesOfFreedom method
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(3, denominatorDegreesOfFreedom, 0);
    }

    // Test case for the getNumericalMean method with denominator degrees of freedom greater than 2
    @Test
    public void testGetNumericalMeanWithDenominatorDegreesOfFreedomGreaterThan2() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double numericalMean = fDistribution.getNumericalMean();
        Assert.assertEquals(Double.NaN, numericalMean, 0);
    }

    // Test case for the getNumericalMean method with denominator degrees of freedom equal to 2
    @Test
    public void testGetNumericalMeanWithDenominatorDegreesOfFreedomEqualTo2() {
        FDistribution fDistribution = new FDistribution(2, 2);
        double numericalMean = fDistribution.getNumericalMean();
        Assert.assertEquals(Double.NaN, numericalMean, 0);
    }

    // Test case for the getNumericalVariance method with denominator degrees of freedom greater than 4
    @Test
    public void testGetNumericalVarianceWithDenominatorDegreesOfFreedomGreaterThan4() {
        FDistribution fDistribution = new FDistribution(2, 5);
        double numericalVariance = fDistribution.getNumericalVariance();
        Assert.assertEquals(1.33333, numericalVariance, 0.00001);
    }

    // Test case for the getNumericalVariance method with denominator degrees of freedom equal to 4
    @Test
    public void testGetNumericalVarianceWithDenominatorDegreesOfFreedomEqualTo4() {
        FDistribution fDistribution = new FDistribution(2, 2);
        double numericalVariance = fDistribution.getNumericalVariance();
        Assert.assertEquals(Double.NaN, numericalVariance, 0);
    }

    // Test case for the getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double supportLowerBound = fDistribution.getSupportLowerBound();
        Assert.assertEquals(0, supportLowerBound, 0);
    }

    // Test case for the getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double supportUpperBound = fDistribution.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0);
    }

    // Test case for the isSupportLowerBoundInclusive method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2, 3);
        boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);
    }

    // Test case for the isSupportUpperBoundInclusive method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2, 3);
        boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportUpperBoundInclusive);
    }

    // Test case for the isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(2, 3);
        boolean isSupportConnected = fDistribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}
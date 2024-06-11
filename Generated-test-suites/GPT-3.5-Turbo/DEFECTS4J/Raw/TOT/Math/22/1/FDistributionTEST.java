import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {
  
    // Test invalid degrees of freedom (not strictly positive)
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidDegreesOfFreedom() {
        new FDistribution(0, 5);
    }
  
    // Test density function
    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double x = 1.5;
        double expectedDensity = FastMath.exp(2.270895051293373) * 0.192450089729874;
        double density = fDistribution.density(x);
        Assert.assertEquals(expectedDensity, density, 1e-9);
    }
  
    // Test cumulative probability function
    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double x = 2.0;
        double expectedProbability = 0.8531932711374147;
        double probability = fDistribution.cumulativeProbability(x);
        Assert.assertEquals(expectedProbability, probability, 1e-9);
    }
  
    // Test getting numerator degrees of freedom
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double expectedDegreesOfFreedom = 5;
        double degreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(expectedDegreesOfFreedom, degreesOfFreedom, 0);
    }
  
    // Test getting denominator degrees of freedom
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double expectedDegreesOfFreedom = 10;
        double degreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(expectedDegreesOfFreedom, degreesOfFreedom, 0);
    }
  
    // Test getting numerical mean
    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double expectedMean = 10.0 / 8.0;
        double mean = fDistribution.getNumericalMean();
        Assert.assertEquals(expectedMean, mean, 0);
    }
  
    // Test getting numerical variance
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double expectedVariance = (2 * 10 * 10 * (5 + 10 - 2)) / (5 * (10 - 2) * (10 - 4));
        double variance = fDistribution.getNumericalVariance();
        Assert.assertEquals(expectedVariance, variance, 1e-9);
    }
  
    // Test getting support lower bound
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double expectedLowerBound = 0;
        double lowerBound = fDistribution.getSupportLowerBound();
        Assert.assertEquals(expectedLowerBound, lowerBound, 0);
    }
  
    // Test getting support upper bound
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double expectedUpperBound = Double.POSITIVE_INFINITY;
        double upperBound = fDistribution.getSupportUpperBound();
        Assert.assertEquals(expectedUpperBound, upperBound, 0);
    }
  
    // Test checking if support lower bound is inclusive
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        boolean isLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isLowerBoundInclusive);
    }
  
    // Test checking if support upper bound is inclusive
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        boolean isUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isUpperBoundInclusive);
    }
  
    // Test checking if support is connected
    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        boolean isSupportConnected = fDistribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}
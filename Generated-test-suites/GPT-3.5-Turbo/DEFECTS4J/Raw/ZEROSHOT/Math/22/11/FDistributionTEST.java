import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {
    
    @Test
    public void testDensity() {
        FDistribution fdistribution = new FDistribution(10, 20);
        double x = 1.5;
        double expectedDensity = 0.33505532376693885;
        double actualDensity = fdistribution.density(x);
        Assert.assertEquals(expectedDensity, actualDensity, 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fdistribution = new FDistribution(10, 20);
        double x = 1.5;
        double expectedCumulativeProbability = 0.6262835229632781;
        double actualCumulativeProbability = fdistribution.cumulativeProbability(x);
        Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.000001);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fdistribution = new FDistribution(10, 20);
        double expectedDegreesOfFreedom = 10;
        double actualDegreesOfFreedom = fdistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(expectedDegreesOfFreedom, actualDegreesOfFreedom, 0.000001);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fdistribution = new FDistribution(10, 20);
        double expectedDegreesOfFreedom = 20;
        double actualDegreesOfFreedom = fdistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(expectedDegreesOfFreedom, actualDegreesOfFreedom, 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution fdistribution = new FDistribution(10, 20);
        double expectedNumericalMean = 1.3333333333333333;
        double actualNumericalMean = fdistribution.getNumericalMean();
        Assert.assertEquals(expectedNumericalMean, actualNumericalMean, 0.000001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution fdistribution = new FDistribution(10, 20);
        double expectedNumericalVariance = 7.840000000000018;
        double actualNumericalVariance = fdistribution.getNumericalVariance();
        Assert.assertEquals(expectedNumericalVariance, actualNumericalVariance, 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fdistribution = new FDistribution(10, 20);
        double expectedLowerBound = 0;
        double actualLowerBound = fdistribution.getSupportLowerBound();
        Assert.assertEquals(expectedLowerBound, actualLowerBound, 0.000001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fdistribution = new FDistribution(10, 20);
        double expectedUpperBound = Double.POSITIVE_INFINITY;
        double actualUpperBound = fdistribution.getSupportUpperBound();
        Assert.assertEquals(expectedUpperBound, actualUpperBound, 0.000001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fdistribution = new FDistribution(10, 20);
        boolean expectedLowerBoundInclusive = true;
        boolean actualLowerBoundInclusive = fdistribution.isSupportLowerBoundInclusive();
        Assert.assertEquals(expectedLowerBoundInclusive, actualLowerBoundInclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fdistribution = new FDistribution(10, 20);
        boolean expectedUpperBoundInclusive = false;
        boolean actualUpperBoundInclusive = fdistribution.isSupportUpperBoundInclusive();
        Assert.assertEquals(expectedUpperBoundInclusive, actualUpperBoundInclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fdistribution = new FDistribution(10, 20);
        boolean expectedSupportConnected = true;
        boolean actualSupportConnected = fdistribution.isSupportConnected();
        Assert.assertEquals(expectedSupportConnected, actualSupportConnected);
    }
}
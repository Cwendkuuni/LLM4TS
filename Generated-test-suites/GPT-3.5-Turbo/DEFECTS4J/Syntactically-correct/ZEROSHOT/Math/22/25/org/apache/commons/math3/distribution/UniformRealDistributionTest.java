import org.junit.Test;
import org.junit.Assert;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test point inside the range
        double x1 = 0.5;
        double expectedDensity1 = 1.0;
        double actualDensity1 = distribution.density(x1);
        Assert.assertEquals(expectedDensity1, actualDensity1, 0.0001);
        
        // Test point outside the range
        double x2 = 2.0;
        double expectedDensity2 = 0.0;
        double actualDensity2 = distribution.density(x2);
        Assert.assertEquals(expectedDensity2, actualDensity2, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test point below the range
        double x1 = -1.0;
        double expectedCumulativeProb1 = 0.0;
        double actualCumulativeProb1 = distribution.cumulativeProbability(x1);
        Assert.assertEquals(expectedCumulativeProb1, actualCumulativeProb1, 0.0001);
        
        // Test point above the range
        double x2 = 2.0;
        double expectedCumulativeProb2 = 1.0;
        double actualCumulativeProb2 = distribution.cumulativeProbability(x2);
        Assert.assertEquals(expectedCumulativeProb2, actualCumulativeProb2, 0.0001);
        
        // Test point inside the range
        double x3 = 0.5;
        double expectedCumulativeProb3 = 0.5;
        double actualCumulativeProb3 = distribution.cumulativeProbability(x3);
        Assert.assertEquals(expectedCumulativeProb3, actualCumulativeProb3, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        
        double expectedMean = 3.5;
        double actualMean = distribution.getNumericalMean();
        Assert.assertEquals(expectedMean, actualMean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        
        double expectedVariance = 0.75;
        double actualVariance = distribution.getNumericalVariance();
        Assert.assertEquals(expectedVariance, actualVariance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double expectedLowerBound = 0.0;
        double actualLowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(expectedLowerBound, actualLowerBound, 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double expectedUpperBound = 1.0;
        double actualUpperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(expectedUpperBound, actualUpperBound, 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        boolean expectedIsInclusive = true;
        boolean actualIsInclusive = distribution.isSupportLowerBoundInclusive();
        Assert.assertEquals(expectedIsInclusive, actualIsInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        boolean expectedIsInclusive = false;
        boolean actualIsInclusive = distribution.isSupportUpperBoundInclusive();
        Assert.assertEquals(expectedIsInclusive, actualIsInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        boolean expectedIsConnected = true;
        boolean actualIsConnected = distribution.isSupportConnected();
        Assert.assertEquals(expectedIsConnected, actualIsConnected);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0.0 && sample < 1.0);
    }
}
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    // Test constructor 1
    @Test
    public void testConstructor1() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        Assert.assertNotNull(dist);
    }
    
    // Test constructor 2
    @Test
    public void testConstructor2() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 2);
        Assert.assertNotNull(dist);
    }
    
    // Test cumulativeProbability
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        double result = dist.cumulativeProbability(1);
        Assert.assertEquals(0.5454545454545454, result, 0.0001);
    }
    
    // Test probability
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        double result = dist.probability(1);
        Assert.assertEquals(0.45454545454545453, result, 0.0001);
    }
    
    // Test getNumericalMean
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        double result = dist.getNumericalMean();
        Assert.assertEquals(0.6, result, 0.0001);
    }
    
    // Test getNumericalVariance
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        double result = dist.getNumericalVariance();
        Assert.assertEquals(0.15, result, 0.0001);
    }
    
    // Test getSupportLowerBound
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        int result = dist.getSupportLowerBound();
        Assert.assertEquals(0, result);
    }
    
    // Test getSupportUpperBound
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        int result = dist.getSupportUpperBound();
        Assert.assertEquals(2, result);
    }
    
    // Test isSupportConnected
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        boolean result = dist.isSupportConnected();
        Assert.assertTrue(result);
    }
}
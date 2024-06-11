import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    private HypergeometricDistribution distribution;

    @Before
    public void setup() {
        // Initialize the distribution with sample parameters
        distribution = new HypergeometricDistribution(10, 4, 3);
    }

    @Test
    public void testCumulativeProbability() {
        // Test typical use case
        Assert.assertEquals(0.19047619047619047, distribution.cumulativeProbability(2), 0.0001);

        // Test edge cases
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(6), 0.0001);
        
        // Test error scenarios
        try {
            distribution.cumulativeProbability(10);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, test passed
        }
    }

    @Test
    public void testProbability() {
        // Test typical use case
        Assert.assertEquals(0.2857142857142857, distribution.probability(2), 0.0001);

        // Test edge cases
        Assert.assertEquals(0.0, distribution.probability(-1), 0.0001);
        Assert.assertEquals(0.0, distribution.probability(6), 0.0001);
        
        // Test error scenarios
        try {
            distribution.probability(10);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, test passed
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Test typical use case
        Assert.assertEquals(0.9047619047619048, distribution.upperCumulativeProbability(2), 0.0001);

        // Test edge cases
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0001);
        
        // Test error scenarios
        try {
            distribution.upperCumulativeProbability(10);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, test passed
        }
    }

    @Test
    public void testGetNumericalMean() {
        // Test typical use case
        Assert.assertEquals(1.2, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        // Test typical use case
        Assert.assertEquals(0.34285714285714286, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        // Test typical use case
        Assert.assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        // Test typical use case
        Assert.assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        // Test typical use case
        Assert.assertTrue(distribution.isSupportConnected());
    }
}
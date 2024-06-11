import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    // Test the density() method
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical use case
        Assert.assertEquals(0.1, distribution.density(5), 0.0001);

        // Edge cases
        Assert.assertEquals(0.0, distribution.density(11), 0.0001);
        Assert.assertEquals(0.0, distribution.density(-1), 0.0001);

        // Error scenario
        Assert.assertNotEquals(0.1, distribution.density(50), 0.0001);
    }

    // Test the cumulativeProbability() method
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical use case
        Assert.assertEquals(0.5, distribution.cumulativeProbability(5), 0.0001);

        // Edge cases
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(11), 0.0001);

        // Error scenario
        Assert.assertNotEquals(0.5, distribution.cumulativeProbability(50), 0.0001);
    }

    // Test the getNumericalMean() method
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        
        // Typical use case
        Assert.assertEquals(5, distribution.getNumericalMean(), 0.0001);
        
        // Edge cases
        UniformRealDistribution distribution2 = new UniformRealDistribution(-10, 10);
        Assert.assertEquals(0, distribution2.getNumericalMean(), 0.0001);
        UniformRealDistribution distribution3 = new UniformRealDistribution(-10, -5);
        Assert.assertEquals(-7.5, distribution3.getNumericalMean(), 0.0001);
        
        // Error scenario
        UniformRealDistribution distribution4 = new UniformRealDistribution(0, 0);
        Assert.assertNotEquals(5, distribution4.getNumericalMean(), 0.0001);
    }

    // Test the getNumericalVariance() method
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        
        // Typical use case
        Assert.assertEquals(8.3333, distribution.getNumericalVariance(), 0.0001);
        
        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(-15, -5);
        Assert.assertEquals(8.3333, distribution2.getNumericalVariance(), 0.0001);

        // Error scenario
        UniformRealDistribution distribution3 = new UniformRealDistribution(0, 10);
        Assert.assertNotEquals(5, distribution3.getNumericalVariance(), 0.0001);
    }

    // Test the getSupportLowerBound() method
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        
        // Typical use case
        Assert.assertEquals(0, distribution.getSupportLowerBound(), 0.0001);
        
        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(-15, -5);
        Assert.assertEquals(-15, distribution2.getSupportLowerBound(), 0.0001);
    }

    // Test the getSupportUpperBound() method
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        
        // Typical use case
        Assert.assertEquals(10, distribution.getSupportUpperBound(), 0.0001);
        
        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(-15, -5);
        Assert.assertEquals(-5, distribution2.getSupportUpperBound(), 0.0001);
    }

    // Test the isSupportLowerBoundInclusive() method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        
        // Typical use case
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    // Test the isSupportUpperBoundInclusive() method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        
        // Typical use case
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    // Test the isSupportConnected() method
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        
        // Typical use case
        Assert.assertTrue(distribution.isSupportConnected());
    }

    // Test the sample() method
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        
        // Typical use case
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample < 10);
    }
}
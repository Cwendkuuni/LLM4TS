import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class UniformRealDistributionTest {
    
    //Method: UniformRealDistribution()
    @Test
    public void testUniformRealDistribution() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertNotNull(distribution);
    }
    
    //Method: UniformRealDistribution(double lower, double upper)
    @Test
    public void testUniformRealDistributionWithBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertNotNull(distribution);
    }
    
    //Method: UniformRealDistribution(double lower, double upper, double inverseCumAccuracy)
    @Test
    public void testUniformRealDistributionWithBoundsAndAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1, 0.001);
        assertNotNull(distribution);
    }
    
    //Method: UniformRealDistribution(RandomGenerator rng, double lower, double upper, double inverseCumAccuracy)
    @Test
    public void testUniformRealDistributionWithRngBoundsAndAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(new Well19937c(), 0, 1, 0.001);
        assertNotNull(distribution);
    }
    
    //Method: double density(double x)
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.5, distribution.density(0.5), 1e-9);
    }
    
    //Method: double cumulativeProbability(double x)
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 1e-9);
    }
    
    //Method: double getNumericalMean()
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.5, distribution.getNumericalMean(), 1e-9);
    }
    
    //Method: double getNumericalVariance()
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(1.0 / 12, distribution.getNumericalVariance(), 1e-9);
    }
    
    //Method: double getSupportLowerBound()
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getSupportLowerBound(), 1e-9);
    }
    
    //Method: double getSupportUpperBound()
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(1.0, distribution.getSupportUpperBound(), 1e-9);
    }
    
    //Method: boolean isSupportLowerBoundInclusive()
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    //Method: boolean isSupportUpperBoundInclusive()
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    //Method: boolean isSupportConnected()
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertTrue(distribution.isSupportConnected());
    }
    
    //Method: double sample()
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        double sample = distribution.sample();
        assertTrue(sample >= 0.0 && sample < 1.0);
    }
    
    //Method: UniformRealDistribution(double lower, double upper) throws NumberIsTooLargeException
    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionWithInvalidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 0);
    }
    
    //Method: UniformRealDistribution(double lower, double upper, double inverseCumAccuracy) throws NumberIsTooLargeException
    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionWithInvalidBoundsAndAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 0, 0.001);
    }

}
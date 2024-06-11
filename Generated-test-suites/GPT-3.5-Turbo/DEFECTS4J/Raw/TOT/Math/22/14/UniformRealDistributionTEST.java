import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        // Typical use case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0.1, distribution.density(5), 0.0001);
        
        // Edge case
        distribution = new UniformRealDistribution(0, 100);
        assertEquals(0.01, distribution.density(50), 0.0001);
        
        // Error scenario
        distribution = new UniformRealDistribution(10, 0);
        assertEquals(0.0, distribution.density(5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0.5, distribution.cumulativeProbability(5), 0.0001);
        
        // Edge case
        distribution = new UniformRealDistribution(0, 100);
        assertEquals(0.6, distribution.cumulativeProbability(60), 0.0001);
        
        // Error scenario
        distribution = new UniformRealDistribution(10, 0);
        assertEquals(0.0, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        // Typical use case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(5.0, distribution.getNumericalMean(), 0.0001);
        
        // Edge case
        distribution = new UniformRealDistribution(0, 100);
        assertEquals(50.0, distribution.getNumericalMean(), 0.0001);
        
        // Error scenario
        distribution = new UniformRealDistribution(10, 0);
        assertEquals(Double.NaN, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        // Typical use case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(8.3333, distribution.getNumericalVariance(), 0.0001);
        
        // Edge case
        distribution = new UniformRealDistribution(0, 100);
        assertEquals(833.3333, distribution.getNumericalVariance(), 0.0001);
        
        // Error scenario
        distribution = new UniformRealDistribution(10, 0);
        assertEquals(Double.NaN, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        // Typical use case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0001);
        
        // Edge case
        distribution = new UniformRealDistribution(-10, 10);
        assertEquals(-10.0, distribution.getSupportLowerBound(), 0.0001);
        
        // Error scenario
        distribution = new UniformRealDistribution(10, 0);
        assertEquals(Double.NaN, distribution.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        // Typical use case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(10.0, distribution.getSupportUpperBound(), 0.0001);
        
        // Edge case
        distribution = new UniformRealDistribution(-10, 10);
        assertEquals(10.0, distribution.getSupportUpperBound(), 0.0001);
        
        // Error scenario
        distribution = new UniformRealDistribution(10, 0);
        assertEquals(Double.NaN, distribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        // Typical use case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        
        // Edge case
        distribution = new UniformRealDistribution(-10, 10);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        
        // Error scenario
        distribution = new UniformRealDistribution(10, 0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        // Typical use case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertFalse(distribution.isSupportUpperBoundInclusive());
        
        // Edge case
        distribution = new UniformRealDistribution(-10, 10);
        assertFalse(distribution.isSupportUpperBoundInclusive());
        
        // Error scenario
        distribution = new UniformRealDistribution(10, 0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        // Typical use case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertTrue(distribution.isSupportConnected());
        
        // Edge case
        distribution = new UniformRealDistribution(-10, 10);
        assertTrue(distribution.isSupportConnected());
        
        // Error scenario
        distribution = new UniformRealDistribution(10, 0);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        // No deterministic test can be performed on sample method
        // Edge case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        double sample = distribution.sample();
        assertTrue(sample >= 0.0 && sample <= 10.0);
    }
}
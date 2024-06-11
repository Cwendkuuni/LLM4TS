import static org.junit.Assert.*;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        // Basic test case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0, distribution.density(0.5), 0.001);
        
        // Test when x is outside the range
        assertEquals(0.0, distribution.density(2.0), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        // Basic test case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.001);
        
        // Test when x is outside the range
        assertEquals(1.0, distribution.cumulativeProbability(2.0), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        // Basic test case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0.001);
        
        // Test with negative range
        distribution = new UniformRealDistribution(-1, 1);
        assertEquals(0.0, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        // Basic test case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0/12.0, distribution.getNumericalVariance(), 0.001);
        
        // Test with negative range
        distribution = new UniformRealDistribution(-1, 1);
        assertEquals(2.0/12.0, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        // Basic test case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.001);
        
        // Test with negative lower bound
        distribution = new UniformRealDistribution(-1, 1);
        assertEquals(-1.0, distribution.getSupportLowerBound(), 0.001);
    }

    @Test
    public void testGetSupportUpperBound() {
        // Basic test case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.001);
        
        // Test with negative upper bound
        distribution = new UniformRealDistribution(-1, 1);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        // Basic test case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        // Basic test case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        // Basic test case
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        // Test sample range
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0.0 && sample <= 1.0);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorException() {
        // Test when lower bound is greater than or equal to upper bound
        UniformRealDistribution distribution = new UniformRealDistribution(2, 1);
    }
}
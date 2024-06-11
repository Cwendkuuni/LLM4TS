import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test for within range value
        double densityWithinRange = distribution.density(0.5);
        assertEquals(1.0, densityWithinRange, 0.00001);
        
        // Test for below lower bound value
        double densityBelowRange = distribution.density(-1);
        assertEquals(0.0, densityBelowRange, 0.00001);
        
        // Test for above upper bound value
        double densityAboveRange = distribution.density(2);
        assertEquals(0.0, densityAboveRange, 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        // Test for within range value
        double cumulativeWithinRange = distribution.cumulativeProbability(0.5);
        assertEquals(0.5, cumulativeWithinRange, 0.00001);
        
        // Test for below lower bound value
        double cumulativeBelowRange = distribution.cumulativeProbability(-1);
        assertEquals(0.0, cumulativeBelowRange, 0.00001);
        
        // Test for above upper bound value
        double cumulativeAboveRange = distribution.cumulativeProbability(2);
        assertEquals(1.0, cumulativeAboveRange, 0.00001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double mean = distribution.getNumericalMean();
        assertEquals(0.5, mean, 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double variance = distribution.getNumericalVariance();
        assertEquals(0.083333333, variance, 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double lowerBound = distribution.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.00001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double upperBound = distribution.getSupportUpperBound();
        assertEquals(1.0, upperBound, 0.00001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        boolean inclusive = distribution.isSupportLowerBoundInclusive();
        assertTrue(inclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        boolean inclusive = distribution.isSupportUpperBoundInclusive();
        assertFalse(inclusive);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        boolean connected = distribution.isSupportConnected();
        assertTrue(connected);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 1);
    }
    
}
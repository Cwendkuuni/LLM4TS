import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;


public class UniformRealDistributionTest {
    
    private UniformRealDistribution distribution;
    private RandomGenerator rng = new Well19937c();
    
    @Test
    public void testDefaultConstructor() {
        distribution = new UniformRealDistribution();
        
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.00001);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.00001);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testConstructor() {
        distribution = new UniformRealDistribution(3.0, 10.0);
        
        assertEquals(3.0, distribution.getSupportLowerBound(), 0.00001);
        assertEquals(10.0, distribution.getSupportUpperBound(), 0.00001);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        distribution = new UniformRealDistribution(10.0, 3.0);
    }
    
    @Test
    public void testDensity() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        assertEquals(1.0, distribution.density(0.5), 0.00001);
        assertEquals(0.0, distribution.density(1.5), 0.00001);
        assertEquals(0.0, distribution.density(-0.5), 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        assertEquals(0.0, distribution.cumulativeProbability(-0.5), 0.00001);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(1.5), 0.00001);
    }
    
    @Test
    public void testGetNumericalMean() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        assertEquals(0.5, distribution.getNumericalMean(), 0.00001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        assertEquals(0.083333, distribution.getNumericalVariance(), 0.00001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.00001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.00001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        distribution = new UniformRealDistribution(0.0, 1.0);
        
        double sample = distribution.sample();
        assertTrue(sample >= 0.0);
        assertTrue(sample <= 1.0);
    }
}
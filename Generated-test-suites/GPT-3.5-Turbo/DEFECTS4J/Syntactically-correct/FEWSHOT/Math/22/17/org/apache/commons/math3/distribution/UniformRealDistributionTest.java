import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution = new UniformRealDistribution();
    
    @Test
    public void testDensity() {
        double result = distribution.density(0.5);
        assertEquals(1.0, result, 0.0001);
        
        result = distribution.density(-1.0);
        assertEquals(0.0, result, 0.0001);
        
        result = distribution.density(2.0);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() {
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(0.5, result, 0.0001);
        
        result = distribution.cumulativeProbability(-1.0);
        assertEquals(0.0, result, 0.0001);
        
        result = distribution.cumulativeProbability(2.0);
        assertEquals(1.0, result, 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        double result = distribution.getNumericalMean();
        double expected = 0.5;
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        double result = distribution.getNumericalVariance();
        double expected = 0.08333;
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        double result = distribution.getSupportLowerBound();
        double expected = 0.0;
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        double result = distribution.getSupportUpperBound();
        double expected = 1.0;
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean result = distribution.isSupportLowerBoundInclusive();
        assertTrue(result);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean result = distribution.isSupportUpperBoundInclusive();
        assertFalse(result);
    }
    
    @Test
    public void testIsSupportConnected() {
        boolean result = distribution.isSupportConnected();
        assertTrue(result);
    }
    
    @Test
    public void testSample() {
        double result = distribution.sample();
        assertTrue(result >= 0.0 && result < 1.0);
    }
}
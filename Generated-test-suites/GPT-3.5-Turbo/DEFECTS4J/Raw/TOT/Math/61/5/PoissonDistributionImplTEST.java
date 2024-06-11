import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {
    
    private PoissonDistributionImpl distribution;
    
    @Before
    public void setup() {
        distribution = new PoissonDistributionImpl(5);
    }
    
    @Test
    public void testGetMean() {
        double expected = 5;
        double actual = distribution.getMean();
        assertEquals(expected, actual, 0);
    }
    
    @Test
    public void testProbability() {
        double expected = 0;
        double actual = distribution.probability(-1);
        assertEquals(expected, actual, 0);
        
        expected = Math.exp(-5);
        actual = distribution.probability(0);
        assertEquals(expected, actual, 0);
        
        expected = Math.exp(-5.118);
        actual = distribution.probability(3);
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        double expected = 0;
        double actual = distribution.cumulativeProbability(-1);
        assertEquals(expected, actual, 0);
        
        expected = 1;
        actual = distribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(expected, actual, 0);
        
        expected = 0.857;
        actual = distribution.cumulativeProbability(3);
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        double expected = 0.968;
        double actual = distribution.normalApproximateProbability(3);
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testSample() throws MathException {
        int value = distribution.sample();
        assertTrue(value >= 0 && value <= Integer.MAX_VALUE);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        int expected = 0;
        int actual = distribution.getDomainLowerBound(0.5);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        int expected = Integer.MAX_VALUE;
        int actual = distribution.getDomainUpperBound(0.5);
        assertEquals(expected, actual);
    }
}
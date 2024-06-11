import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    private HypergeometricDistribution hyperDist;
    
    @Before
    public void setUp() {
        hyperDist = new HypergeometricDistribution(10, 5, 3);
    }
    
    @Test
    public void testConstructor() {
        try {
            new HypergeometricDistribution(-5, 5, 3);
            fail("Expected a NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
        
        try {
            new HypergeometricDistribution(10, -5, 3);
            fail("Expected a NotPositiveException to be thrown");
        } catch (NotPositiveException e) {
            assertTrue(true);
        }
        
        try {
            new HypergeometricDistribution(10, 5, -3);
            fail("Expected a NotPositiveException to be thrown");
        } catch (NotPositiveException e) {
            assertTrue(true);
        }
        
        try {
            new HypergeometricDistribution(10, 15, 10);
            fail("Expected a NumberIsTooLargeException to be thrown");
        } catch (NumberIsTooLargeException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        assertEquals(1.0, hyperDist.cumulativeProbability(3), 0);
        assertEquals(0.0, hyperDist.cumulativeProbability(0), 0);
        assertEquals(0.5, hyperDist.cumulativeProbability(2), 0);
        assertEquals(0.25, hyperDist.cumulativeProbability(1), 0);
        assertEquals(1.0, hyperDist.cumulativeProbability(5), 0);
    }
    
    @Test
    public void testProbability() {
        assertEquals(1.0, hyperDist.probability(3), 0);
        assertEquals(0.0, hyperDist.probability(0), 0);
        assertEquals(0.5, hyperDist.probability(2), 0);
        assertEquals(0.25, hyperDist.probability(1), 0);
        assertEquals(0.21428571428571427, hyperDist.probability(5), 0);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.0, hyperDist.upperCumulativeProbability(9), 0);
        assertEquals(1.0, hyperDist.upperCumulativeProbability(3), 0);
        assertEquals(1.0, hyperDist.upperCumulativeProbability(0), 0);
        assertEquals(0.5, hyperDist.upperCumulativeProbability(2), 0);
        assertEquals(0.75, hyperDist.upperCumulativeProbability(1), 0);
        assertEquals(0.0, hyperDist.upperCumulativeProbability(5), 0);
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, hyperDist.getNumericalMean(), 0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.5357142857142857, hyperDist.getNumericalVariance(), 0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, hyperDist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, hyperDist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(hyperDist.isSupportConnected());
    }
}
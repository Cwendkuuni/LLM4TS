import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.random.*;
import org.apache.commons.math3.distribution.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(-1, 2, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(1, -2, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(1, 2, -3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(5, 4, 6);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 6);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        assertNotNull(distribution);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        double result = distribution.cumulativeProbability(1);
        assertEquals(0.5, result, 0.0001);
        
        result = distribution.cumulativeProbability(2);
        assertEquals(1.0, result, 0.0001);
        
        result = distribution.cumulativeProbability(0);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        int result = distribution.getNumberOfSuccesses();
        assertEquals(2, result);
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        int result = distribution.getPopulationSize();
        assertEquals(5, result);
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        int result = distribution.getSampleSize();
        assertEquals(3, result);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        double result = distribution.probability(0);
        assertEquals(0.1667, result, 0.0001);
        
        result = distribution.probability(2);
        assertEquals(0.3333, result, 0.0001);
        
        result = distribution.probability(3);
        assertEquals(0.0, result, 0.0001);
        
        result = distribution.probability(1);
        assertEquals(0.5, result, 0.0001);
        
        result = distribution.probability(4);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        double result = distribution.upperCumulativeProbability(0);
        assertEquals(1.0, result, 0.0001);
        
        result = distribution.upperCumulativeProbability(2);
        assertEquals(0.5, result, 0.0001);
        
        result = distribution.upperCumulativeProbability(3);
        assertEquals(0.0, result, 0.0001);
        
        result = distribution.upperCumulativeProbability(1);
        assertEquals(0.5, result, 0.0001);
        
        result = distribution.upperCumulativeProbability(4);
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        double result = distribution.getNumericalMean();
        assertEquals(1.2, result, 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        double result = distribution.getNumericalVariance();
        assertEquals(0.48, result, 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        int result = distribution.getSupportLowerBound();
        assertEquals(0, result);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        int result = distribution.getSupportUpperBound();
        assertEquals(2, result);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        
        boolean result = distribution.isSupportConnected();
        assertEquals(true, result);
    }
}
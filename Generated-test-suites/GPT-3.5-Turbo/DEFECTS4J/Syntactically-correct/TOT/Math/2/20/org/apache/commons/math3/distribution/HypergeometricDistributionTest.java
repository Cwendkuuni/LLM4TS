import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    private HypergeometricDistribution distribution;
    private RandomGenerator rng;
    
    @Before
    public void setUp() {
        rng = mock(RandomGenerator.class);
        distribution = new HypergeometricDistribution(rng, 20, 10, 5);
    }
    
    @Test
    public void testConstructor() {
        try {
            new HypergeometricDistribution(0, 10, 5);
            fail("Expecting NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(LocalizedFormats.POPULATION_SIZE.getMessage(0), e.getMessage());
        }
        
        try {
            new HypergeometricDistribution(20, -1, 5);
            fail("Expecting NotPositiveException");
        } catch (NotPositiveException e) {
            assertEquals(LocalizedFormats.NUMBER_OF_SUCCESSES.getMessage(-1), e.getMessage());
        }
        
        try {
            new HypergeometricDistribution(20, 10, -1);
            fail("Expecting NotPositiveException");
        } catch (NotPositiveException e) {
            assertEquals(LocalizedFormats.NUMBER_OF_SAMPLES.getMessage(-1), e.getMessage());
        }
        
        try {
            new HypergeometricDistribution(20, 25, 5);
            fail("Expecting NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertEquals(LocalizedFormats.NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE.getMessage(25, 20), e.getMessage());
        }
        
        try {
            new HypergeometricDistribution(20, 10, 30);
            fail("Expecting NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertEquals(LocalizedFormats.SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE.getMessage(30, 20), e.getMessage());
        }
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(10, distribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(20, distribution.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        assertEquals(5, distribution.getSampleSize());
    }
    
    @Test
    public void testCumulativeProbability() {
        double epsilon = 1e-9;
        
        // Typical case
        assertEquals(0.369047619, distribution.cumulativeProbability(2), epsilon);
        
        // Edge cases
        assertEquals(0.0, distribution.cumulativeProbability(-1), epsilon);
        assertEquals(1.0, distribution.cumulativeProbability(7), epsilon);
        
        // Error scenarios
        try {
            distribution.cumulativeProbability(10); // x > domain[1]
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Number out of range: 10"));
        }
    }
    
    @Test
    public void testProbability() {
        double epsilon = 1e-9;
        
        // Typical case
        assertEquals(0.161815755, distribution.probability(3), epsilon);
        
        // Edge cases
        assertEquals(0.0, distribution.probability(-1), epsilon);
        assertEquals(0.0, distribution.probability(8), epsilon);
        
        // Error scenarios
        try {
            distribution.probability(10); // x < domain[0] or x > domain[1]
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Number out of range: 10"));
        }
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        double epsilon = 1e-9;
        
        // Typical case
        assertEquals(0.630952381, distribution.upperCumulativeProbability(2), epsilon);
        
        // Edge cases
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), epsilon);
        assertEquals(0.0, distribution.upperCumulativeProbability(7), epsilon);
        
        // Error scenarios
        try {
            distribution.upperCumulativeProbability(10); // x <= domain[0]
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Number out of range: 10"));
        }
    }
    
    @Test
    public void testGetNumericalMean() {
        double epsilon = 1e-9;
        
        assertEquals(2.5, distribution.getNumericalMean(), epsilon);
    }
    
    @Test
    public void testGetNumericalVariance() {
        double epsilon = 1e-9;
        
        assertEquals(0.797101449, distribution.getNumericalVariance(), epsilon);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(5, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(5, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}
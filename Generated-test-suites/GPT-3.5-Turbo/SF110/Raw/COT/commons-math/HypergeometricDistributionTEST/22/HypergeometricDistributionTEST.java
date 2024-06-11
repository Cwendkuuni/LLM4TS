import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor1() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(5, hd.getNumberOfSuccesses());
            assertEquals(10, hd.getPopulationSize());
            assertEquals(3, hd.getSampleSize());
            assertEquals(0, hd.getSupportLowerBound());
            assertEquals(3, hd.getSupportUpperBound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructor2() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 20, 10, 5);
            assertEquals(10, hd.getNumberOfSuccesses());
            assertEquals(20, hd.getPopulationSize());
            assertEquals(5, hd.getSampleSize());
            assertEquals(5, hd.getSupportLowerBound());
            assertEquals(10, hd.getSupportUpperBound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructor_InvalidPopulationSize() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(0, 5, 3);
            fail("InvalidPopulationSize exception not thrown");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Population size must be positive", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testConstructor_InvalidNumberOfSuccesses() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, -1, 3);
            fail("InvalidNumberOfSuccesses exception not thrown");
        } catch (NotPositiveException e) {
            assertEquals("Number of successes must be non-negative", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testConstructor_InvalidSampleSize() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, -1);
            fail("InvalidSampleSize exception not thrown");
        } catch (NotPositiveException e) {
            assertEquals("Number of samples must be non-negative", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testConstructor_NumberOfSuccessesLargerThanPopulationSize() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 15, 3);
            fail("NumberIsTooLargeException exception not thrown");
        } catch (NumberIsTooLargeException e) {
            assertEquals("Number of successes must be less than or equal to population size", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testConstructor_SampleSizeLargerThanPopulationSize() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 15);
            fail("NumberIsTooLargeException exception not thrown");
        } catch (NumberIsTooLargeException e) {
            assertEquals("Sample size must be less than or equal to population size", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testCumulativeProbabilityBelowLowerBound() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.0, hd.cumulativeProbability(0), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testCumulativeProbabilityAboveUpperBound() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(1.0, hd.cumulativeProbability(4), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testCumulativeProbabilityInRange() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.3333, hd.cumulativeProbability(1), 0.0001);
            assertEquals(0.4444, hd.cumulativeProbability(2), 0.0001);
            assertEquals(0.8889, hd.cumulativeProbability(3), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testProbabilityBelowLowerBound() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.0, hd.probability(0), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testProbabilityAboveUpperBound() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.0, hd.probability(4), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testProbabilityInRange() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.1414, hd.probability(1), 0.0001);
            assertEquals(0.4242, hd.probability(2), 0.0001);
            assertEquals(0.2828, hd.probability(3), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testUpperCumulativeProbabilityBelowLowerBound() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(1.0, hd.upperCumulativeProbability(0), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testUpperCumulativeProbabilityAboveUpperBound() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.0, hd.upperCumulativeProbability(4), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testUpperCumulativeProbabilityInRange() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.6667, hd.upperCumulativeProbability(1), 0.0001);
            assertEquals(0.5556, hd.upperCumulativeProbability(2), 0.0001);
            assertEquals(0.1111, hd.upperCumulativeProbability(3), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetNumericalMean() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(1.5, hd.getNumericalMean(), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetNumericalVariance() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.9375, hd.getNumericalVariance(), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetSupportLowerBound() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0, hd.getSupportLowerBound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetSupportUpperBound() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertEquals(3, hd.getSupportUpperBound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testIsSupportConnected() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
            assertTrue(hd.isSupportConnected());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
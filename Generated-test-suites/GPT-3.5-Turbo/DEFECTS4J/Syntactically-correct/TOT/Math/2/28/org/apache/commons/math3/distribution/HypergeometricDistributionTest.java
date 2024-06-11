import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    HypergeometricDistribution distribution;
    
    @Before
    public void setup() {
        distribution = new HypergeometricDistribution(100, 50, 20);
    }
    
    @Test
    public void testConstructor_ValidParameters_CreatesDistribution() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
            assertEquals(100, distribution.getPopulationSize());
            assertEquals(50, distribution.getNumberOfSuccesses());
            assertEquals(20, distribution.getSampleSize());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_PopulationSizeZero_ThrowsNotStrictlyPositiveException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(0, 50, 20);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_PopulationSizeNegative_ThrowsNotStrictlyPositiveException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-100, 50, 20);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_NumberOfSuccessesNegative_ThrowsNotPositiveException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, -50, 20);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_SampleSizeNegative_ThrowsNotPositiveException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, -20);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize_ThrowsNumberIsTooLargeException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 101, 20);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize_ThrowsNumberIsTooLargeException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 101);
    }
    
    @Test
    public void testCumulativeProbability_ValidParameters_ReturnsCorrectProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.1327, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(10), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(50), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(100), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability_InvalidParameter_ReturnsZero() {
        assertEquals(0.0, distribution.cumulativeProbability(-10), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(101), 0.0001);
    }
    
    @Test
    public void testProbability_ValidParameters_ReturnsCorrectProbability() {
        assertEquals(0.1327, distribution.probability(1), 0.0001);
        assertEquals(0.0, distribution.probability(10), 0.0001);
        assertEquals(0.0, distribution.probability(-10), 0.0001);
        assertEquals(0.0, distribution.probability(101), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability_ValidParameters_ReturnsCorrectProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.8673, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(10), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(50), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(100), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability_InvalidParameter_ReturnsOne() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-10), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(101), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean_ValidParameters_ReturnsCorrectMean() {
        assertEquals(10.0, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance_ValidParameters_ReturnsCorrectVariance() {
        assertEquals(4.0404, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound_ValidParameters_ReturnsCorrectLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound_ValidParameters_ReturnsCorrectUpperBound() {
        assertEquals(20, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected_AnyParameters_ReturnsTrue() {
        assertTrue(distribution.isSupportConnected());
    }
}
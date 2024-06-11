import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_ValidInput() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            assertNotNull(distribution);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(0, 5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 15);
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distribution.getPopulationSize());
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSampleSize());
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testProbability_ValidInput() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.161, distribution.probability(1), 0.001);
    }
    
    @Test
    public void testProbability_OutOfRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.probability(10), 0.001);
    }
    
    @Test
    public void testCumulativeProbability_ValidInput() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.78, distribution.cumulativeProbability(1), 0.001);
    }
    
    @Test
    public void testCumulativeProbability_OutOfRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, distribution.cumulativeProbability(10), 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability_ValidInput() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.22, distribution.upperCumulativeProbability(1), 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability_OutOfRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.upperCumulativeProbability(10), 0.001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.428, distribution.getNumericalVariance(), 0.001);
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_PositivePopulationSize_ThrowsNotPositiveException() {
        try {
            new HypergeometricDistribution(0, 1, 1);
            fail("Exception not thrown");
        } catch (NotPositiveException e) {
            // Exception thrown, test passed
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testConstructor_NegativeNumberOfSuccesses_ThrowsNotPositiveException() {
        try {
            new HypergeometricDistribution(10, -1, 1);
            fail("Exception not thrown");
        } catch (NotPositiveException e) {
            // Exception thrown, test passed
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testConstructor_NegativeSampleSize_ThrowsNotPositiveException() {
        try {
            new HypergeometricDistribution(10, 1, -1);
            fail("Exception not thrown");
        } catch (NotPositiveException e) {
            // Exception thrown, test passed
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize_ThrowsNumberIsTooLargeException() {
        try {
            new HypergeometricDistribution(10, 20, 1);
            fail("Exception not thrown");
        } catch (NumberIsTooLargeException e) {
            // Exception thrown, test passed
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testConstructor_SampleSizeGreaterThanPopulationSize_ThrowsNumberIsTooLargeException() {
        try {
            new HypergeometricDistribution(10, 1, 20);
            fail("Exception not thrown");
        } catch (NumberIsTooLargeException e) {
            // Exception thrown, test passed
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testCumulativeProbability_LowerThanLowerDomain_ReturnsZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 1, 1);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability_GreaterThanOrEqualToUpperDomain_ReturnsOne() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 1, 1);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability_InsideDomain_ReturnsCorrectProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.1905, distribution.cumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testGetNumberOfSuccesses_ReturnsCorrectValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(2, distribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize_ReturnsCorrectValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(10, distribution.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize_ReturnsCorrectValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(4, distribution.getSampleSize());
    }
    
    @Test
    public void testProbability_OutsideDomain_ReturnsZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.0, distribution.probability(6), 0.0001);
    }
    
    @Test
    public void testProbability_InsideDomain_ReturnsCorrectProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.5714, distribution.probability(2), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability_LessThanOrEqualToLowerDomain_ReturnsOne() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability_GreaterThanUpperDomain_ReturnsZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability_InsideDomain_ReturnsCorrectProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.5714, distribution.upperCumulativeProbability(2), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean_ReturnsCorrectValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.8, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance_ReturnsCorrectValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0.6333, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound_ReturnsCorrectValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound_ReturnsCorrectValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertEquals(2, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected_ReturnsTrue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 4);
        assertTrue(distribution.isSupportConnected());
    }
}
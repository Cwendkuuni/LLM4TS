
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

public class HypergeometricDistributionTest {
    @Test
    public void testConstructor() {
        // Test valid input
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        } catch (Exception e) {
            // Should not throw any exception
            fail("Error: " + e.getMessage());
        }
        
        // Test NotPositiveException for populationSize
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 2);
            fail("Should throw a NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Error: " + e.getMessage());
        }
        
        // Test NotPositiveException for numberOfSuccesses
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 2);
            fail("Should throw a NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Error: " + e.getMessage());
        }
        
        // Test NotPositiveException for sampleSize
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -2);
            fail("Should throw a NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Error: " + e.getMessage());
        }
        
        // Test NumberIsTooLargeException for numberOfSuccesses > populationSize
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(5, 10, 2);
            fail("Should throw a NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Error: " + e.getMessage());
        }
        
        // Test NumberIsTooLargeException for sampleSize > populationSize
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 10);
            fail("Should throw a NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Error: " + e.getMessage());
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        // Test x < domain[0]
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        
        // Test x > domain[1]
        assertEquals(1.0, dist.cumulativeProbability(10), 0.0001);
        
        // Test innerCumulativeProbability
        assertEquals(0.3636, dist.cumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int[] domain = dist.getDomain(10, 5, 2);
        assertEquals(0, domain[0]);
        assertEquals(2, domain[1]);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int lowerDomain = dist.getLowerDomain(10, 5, 2);
        assertEquals(3, lowerDomain);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int numberOfSuccesses = dist.getNumberOfSuccesses();
        assertEquals(5, numberOfSuccesses);
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int populationSize = dist.getPopulationSize();
        assertEquals(10, populationSize);
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int sampleSize = dist.getSampleSize();
        assertEquals(2, sampleSize);
    }
    
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int upperDomain = dist.getUpperDomain(5, 2);
        assertEquals(2, upperDomain);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);

        // Test x < domain[0] or x > domain[1]
        assertEquals(0.0, dist.probability(-1), 0.0001);
        assertEquals(0.0, dist.probability(10), 0.0001);
        
        // Test valid x
        assertEquals(0.2727, dist.probability(2), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        // Test x <= domain[0]
        assertEquals(1.0, dist.upperCumulativeProbability(2), 0.0001);
        
        // Test x > domain[1]
        assertEquals(0.0, dist.upperCumulativeProbability(10), 0.0001);
        
        // Test innerCumulativeProbability
        assertEquals(0.6363, dist.upperCumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        double innerCumulativeProbability = dist.innerCumulativeProbability(4, 2, -1);
        assertEquals(0.1818, innerCumulativeProbability, 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        double numericalMean = dist.getNumericalMean();
        assertEquals(1.0, numericalMean, 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(0.1818, numericalVariance, 0.0001);
    }
    
    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        double calculatedNumericalVariance = dist.calculateNumericalVariance();
        assertEquals(0.1818, calculatedNumericalVariance, 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int supportLowerBound = dist.getSupportLowerBound();
        assertEquals(5, supportLowerBound);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        int supportUpperBound = dist.getSupportUpperBound();
        assertEquals(2, supportUpperBound);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        
        boolean isSupportConnected = dist.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}

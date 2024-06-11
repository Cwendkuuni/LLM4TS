import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructorWithValidValues() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            assertNotNull(distribution);
        } catch (Exception e) {
            fail("Failed to create HypergeometricDistribution with valid values");
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-10, 5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 15);
    }
    
    @Test
    public void testCumulativeProbabilityWithValidValuesWithinDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.08333, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.23333, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.61666, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testCumulativeProbabilityWithValidValuesOutsideDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testProbabilityWithValidValuesInsideDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.35714, distribution.probability(0), 0.0001);
        assertEquals(0.42857, distribution.probability(1), 0.0001);
        assertEquals(0.21428, distribution.probability(2), 0.0001);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbabilityWithValidValuesWithinDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.77083, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.38461, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbabilityWithValidValuesOutsideDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.375, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, distribution.getSupportLowerBound());
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
}
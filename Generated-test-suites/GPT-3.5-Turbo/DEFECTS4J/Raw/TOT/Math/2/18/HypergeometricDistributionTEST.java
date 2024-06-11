import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.*;

public class HypergeometricDistributionTest {

    // Test constructor with valid input
    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        assertNotNull(dist);
    }
    
    // Test constructor with invalid input (negative numberOfSuccesses)
    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, -50, 20);
    }
    
    // Test constructor with invalid input (zero populationSize)
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(0, 50, 20);
    }
    
    // Test constructor with invalid input (negative sampleSize)
    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, -20);
    }
    
    // Test constructor with invalid input (numberOfSuccesses > populationSize)
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 150, 20);
    }
    
    // Test constructor with invalid input (sampleSize > populationSize)
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 120);
    }
    
    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double cumProb = dist.cumulativeProbability(10);
        assertTrue(cumProb >= 0.0 && cumProb <= 1.0);
    }
    
    // Test probability method
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double prob = dist.probability(10);
        assertTrue(prob >= 0.0 && prob <= 1.0);
    }
    
    // Test upperCumulativeProbability method
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double cumProb = dist.upperCumulativeProbability(10);
        assertTrue(cumProb >= 0.0 && cumProb <= 1.0);
    }
    
    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double mean = dist.getNumericalMean();
        assertTrue(mean >= 0.0);
    }
    
    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double variance = dist.getNumericalVariance();
        assertTrue(variance >= 0.0);
    }
    
    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        int lowerBound = dist.getSupportLowerBound();
        assertTrue(lowerBound >= 0);
    }
    
    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        int upperBound = dist.getSupportUpperBound();
        assertTrue(upperBound >= 0);
    }
    
    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        boolean isSupportConnected = dist.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}
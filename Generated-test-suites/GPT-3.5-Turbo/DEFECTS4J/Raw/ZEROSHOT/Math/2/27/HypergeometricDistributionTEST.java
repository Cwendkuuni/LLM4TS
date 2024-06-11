import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
    // Test for constructor with valid arguments
    @Test
    public void testConstructorWithValidArguments() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(dist);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }
    
    // Test for constructor with invalid population size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(0, 5, 3);
    }
    
    // Test for constructor with negative number of successes
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
    }
    
    // Test for constructor with negative sample size
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
    }
    
    // Test for constructor with number of successes larger than population size
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 20, 3);
    }
    
    // Test for constructor with sample size larger than population size
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 30);
    }
    
    // Test for cumulativeProbability() method
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6, dist.cumulativeProbability(0), 0.00001);
    }
    
    // Test for probability() method
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.28571, dist.probability(2), 0.00001);
    }
    
    // Test for upperCumulativeProbability() method
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.4, dist.upperCumulativeProbability(1), 0.00001);
    }
    
    // Test for getNumericalMean() method
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.00001);
    }
    
    // Test for getNumericalVariance() method
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.42857, dist.getNumericalVariance(), 0.00001);
    }
    
    // Test for getSupportLowerBound() method
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, dist.getSupportLowerBound());
    }
    
    // Test for getSupportUpperBound() method
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportUpperBound());
    }
    
    // Test for isSupportConnected() method
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
}
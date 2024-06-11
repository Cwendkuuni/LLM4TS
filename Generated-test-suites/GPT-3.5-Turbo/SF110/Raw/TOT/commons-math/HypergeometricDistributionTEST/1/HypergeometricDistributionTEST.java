import org.junit.*;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    // Test case for constructor with valid arguments
    @Test
    public void testConstructorValidArguments() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            assertNotNull(distribution);
            assertEquals(10, distribution.getPopulationSize());
            assertEquals(5, distribution.getNumberOfSuccesses());
            assertEquals(3, distribution.getSampleSize());
        } catch (Exception e) {
            fail("Exception thrown for valid arguments");
        }
    }

    // Test case for constructor with populationSize <= 0
    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructorInvalidPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-10, 5, 3);
    }

    // Test case for constructor with numberOfSuccesses < 0
    @Test(expected=NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 3);
    }

    // Test case for constructor with sampleSize < 0
    @Test(expected=NotPositiveException.class)
    public void testConstructorInvalidSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -3);
    }

    // Test case for constructor with numberOfSuccesses > populationSize
    @Test(expected=NumberIsTooLargeException.class)
    public void testConstructorInvalidNumberOfSuccessesLarge() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
    }

    // Test case for constructor with sampleSize > populationSize
    @Test(expected=NumberIsTooLargeException.class)
    public void testConstructorInvalidSampleSizeLarge() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 30);
    }

    // Test case for cumulativeProbability() with x < domain[0]
    @Test
    public void testCumulativeProbabilityLessThanLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(-1);
        assertEquals(0.0, result, 0.0001);
    }

    // Test case for cumulativeProbability() with x = domain[0]
    @Test
    public void testCumulativeProbabilityEqualToLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(0);
        assertEquals(0.0, result, 0.0001);
    }

    // Test case for cumulativeProbability() with x > domain[1]
    @Test
    public void testCumulativeProbabilityGreaterThanUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(11);
        assertEquals(1.0, result, 0.0001);
    }

    // Test case for cumulativeProbability() with x = domain[1]
    @Test
    public void testCumulativeProbabilityEqualToUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(10);
        assertEquals(1.0, result, 0.0001);
    }

    // Test case for cumulativeProbability() with x within domain[0] and domain[1]
    @Test
    public void testCumulativeProbabilityWithinDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(5);
        assertEquals(0.6190, result, 0.0001);
    }

    // Test case for getNumericalMean()
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.getNumericalMean();
        assertEquals(1.5, result, 0.0001);
    }

    // Test case for getNumericalVariance()
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.getNumericalVariance();
        assertEquals(0.5625, result, 0.0001);
    }

    // Test case for getSupportLowerBound()
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int result = distribution.getSupportLowerBound();
        assertEquals(3, result);
    }

    // Test case for getSupportUpperBound()
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int result = distribution.getSupportUpperBound();
        assertEquals(5, result);
    }

    // Test case for isSupportConnected()
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        boolean result = distribution.isSupportConnected();
        assertTrue(result);
    }

}
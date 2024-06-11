
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    // Test constructor with valid inputs
    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        } catch (Exception e) {
            fail("Unexpected Exception thrown: " + e.getMessage());
        }
    }

    // Test constructor with negative population size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_negativePopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 3);
    }

    // Test constructor with negative number of successes
    @Test(expected = NotPositiveException.class)
    public void testConstructor_negativeNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
    }

    // Test constructor with negative sample size
    @Test(expected = NotPositiveException.class)
    public void testConstructor_negativeSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
    }

    // Test constructor with number of successes greater than population size
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_numberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 15, 3);
    }

    // Test constructor with sample size greater than population size
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_sampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 15);
    }

    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.cumulativeProbability(2);
        assertEquals(0.05714285714285714, result, 0.00001);
    }

    // Test probability method
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.probability(2);
        assertEquals(0.34285714285714286, result, 0.00001);
    }

    // Test upperCumulativeProbability method
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.upperCumulativeProbability(2);
        assertEquals(0.9571428571428571, result, 0.00001);
    }

    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.getNumericalMean();
        assertEquals(1.5, result, 0.00001);
    }

    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.getNumericalVariance();
        assertEquals(0.42857142857142855, result, 0.00001);
    }

    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int result = dist.getSupportLowerBound();
        assertEquals(3, result);
    }

    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int result = dist.getSupportUpperBound();
        assertEquals(5, result);
    }

    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        boolean result = dist.isSupportConnected();
        assertTrue(result);
    }
}


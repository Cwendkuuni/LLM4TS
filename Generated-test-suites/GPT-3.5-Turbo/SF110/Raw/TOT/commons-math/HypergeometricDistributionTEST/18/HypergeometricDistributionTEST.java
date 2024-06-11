import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_withValidParameters_shouldCreateInstance() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            assertNotNull(distribution);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_withNegativePopulationSize_shouldThrowNotPositiveException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-10, 5, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withZeroPopulationSize_shouldThrowNotStrictlyPositiveException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(0, 5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_withNegativeNumberOfSuccesses_shouldThrowNotPositiveException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_withNegativeSampleSize_shouldThrowNotPositiveException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_withNumberOfSuccessesGreaterThanPopulationSize_shouldThrowNumberIsTooLargeException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_withSampleSizeGreaterThanPopulationSize_shouldThrowNumberIsTooLargeException() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 15);
    }

    @Test
    public void testCumulativeProbability_withValueLessThanLowerDomain_shouldReturnZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(1);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testCumulativeProbability_withValueEqualToLowerDomain_shouldReturnNonZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(3);
        assertNotEquals(0.0, result, 0.0001);
    }

    @Test
    public void testCumulativeProbability_withValueGreaterThanUpperDomain_shouldReturnOne() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(13);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testCumulativeProbability_withValueEqualToUpperDomain_shouldReturnNonOne() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(10);
        assertNotEquals(1.0, result, 0.0001);
    }

    @Test
    public void testProbability_withValueLessThanLowerDomain_shouldReturnZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.probability(1);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testProbability_withValueGreaterThanUpperDomain_shouldReturnZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.probability(13);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testProbability_withValueInsideDomain_shouldReturnNonZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.probability(6);
        assertNotEquals(0.0, result, 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability_withValueLessThanOrEqualToLowerDomain_shouldReturnOne() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.upperCumulativeProbability(1);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability_withValueGreaterThanUpperDomain_shouldReturnZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.upperCumulativeProbability(13);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability_withValueEqualToUpperDomain_shouldReturnNonZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.upperCumulativeProbability(10);
        assertNotEquals(0.0, result, 0.0001);
    }

    // Add more test cases for other methods...

}
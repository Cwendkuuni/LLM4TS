import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    // Test case for constructor with valid inputs
    @Test
    public void testConstructor_ValidInputs_Success() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
            assertNotNull(dist);
        } catch (Exception e) {
            fail("Exception thrown for valid inputs");
        }
    }

    // Test case for constructor with populationSize <= 0
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_PopulationSizeZero_ExceptionThrown() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(0, 50, 20);
    }

    // Test case for constructor with numberOfSuccesses < 0
    @Test(expected = NotPositiveException.class)
    public void testConstructor_NumberOfSuccessesNegative_ExceptionThrown() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, -1, 20);
    }

    // Test case for constructor with sampleSize < 0
    @Test(expected = NotPositiveException.class)
    public void testConstructor_SampleSizeNegative_ExceptionThrown() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, -1);
    }

    // Test case for constructor with numberOfSuccesses > populationSize
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize_ExceptionThrown() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 200, 20);
    }

    // Test case for constructor with sampleSize > populationSize
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize_ExceptionThrown() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 200);
    }

    // Test case for cumulativeProbability with x within domain
    @Test
    public void testCumulativeProbability_WithinDomain_ValidResult() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.cumulativeProbability(10);
        assertEquals(0.055, result, 0.001);
    }
    
    // Test case for cumulativeProbability with x less than lower domain
    @Test
    public void testCumulativeProbability_LowerThanDomain_ReturnsZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.cumulativeProbability(-10);
        assertEquals(0.0, result, 0.001);
    }

    // Test case for cumulativeProbability with x greater than upper domain
    @Test
    public void testCumulativeProbability_GreaterThanDomain_ReturnsOne() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.cumulativeProbability(30);
        assertEquals(1.0, result, 0.001);
    }

    // Test case for getLowerDomain
    @Test
    public void testGetLowerDomain_ValidInputs_ValidResult() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        int result = dist.getLowerDomain(100, 50, 20);
        assertEquals(30, result);
    }

    // Test case for getUpperDomain
    @Test
    public void testGetUpperDomain_ValidInputs_ValidResult() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        int result = dist.getUpperDomain(50, 20);
        assertEquals(20, result);
    }

    // Test case for probability with x within domain
    @Test
    public void testProbability_WithinDomain_ValidResult() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.probability(10);
        assertEquals(0.076, result, 0.001);
    }

    // Test case for probability with x less than lower domain
    @Test
    public void testProbability_LowerThanDomain_ReturnsZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.probability(-10);
        assertEquals(0.0, result, 0.001);
    }

    // Test case for probability with x greater than upper domain
    @Test
    public void testProbability_GreaterThanDomain_ReturnsZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.probability(30);
        assertEquals(0.0, result, 0.001);
    }

    // Test case for upperCumulativeProbability with x within domain
    @Test
    public void testUpperCumulativeProbability_WithinDomain_ValidResult() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.upperCumulativeProbability(10);
        assertEquals(0.901, result, 0.001);
    }

    // Test case for upperCumulativeProbability with x less than or equal to lower domain
    @Test
    public void testUpperCumulativeProbability_LowerOrEqualToDomain_ReturnsOne() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.upperCumulativeProbability(30);
        assertEquals(1.0, result, 0.001);
    }

    // Test case for upperCumulativeProbability with x greater than upper domain
    @Test
    public void testUpperCumulativeProbability_GreaterThanDomain_ReturnsZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.upperCumulativeProbability(-10);
        assertEquals(0.0, result, 0.001);
    }

    // Test case for getNumericalMean
    @Test
    public void testGetNumericalMean_ValidInputs_ValidResult() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.getNumericalMean();
        assertEquals(10.0, result, 0.001);
    }

    // Test case for getNumericalVariance
    @Test
    public void testGetNumericalVariance_ValidInputs_ValidResult() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        double result = dist.getNumericalVariance();
        assertEquals(4.418, result, 0.001);
    }

    // Test case for getSupportLowerBound
    @Test
    public void testGetSupportLowerBound_ValidInputs_ValidResult() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        int result = dist.getSupportLowerBound();
        assertEquals(30, result);
    }

    // Test case for getSupportUpperBound
    @Test
    public void testGetSupportUpperBound_ValidInputs_ValidResult() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        int result = dist.getSupportUpperBound();
        assertEquals(20, result);
    }

    // Test case for isSupportConnected
    @Test
    public void testIsSupportConnected_ValidInputs_ReturnsTrue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 20);
        boolean result = dist.isSupportConnected();
        assertTrue(result);
    }
}
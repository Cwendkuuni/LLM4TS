import org.junit.*;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution hypergeometric;

    @Before
    public void setUp() {
        hypergeometric = new HypergeometricDistribution(10, 5, 3);
    }

    // Test for constructor with valid parameters
    @Test
    public void testConstructorValidParameters() {
        assertNotNull(hypergeometric);
    }

    // Test for constructor with populationSize = 0
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorPopulationSizeZero() {
        new HypergeometricDistribution(0, 5, 3);
    }

    // Test for constructor with populationSize < 0
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorPopulationSizeNegative() {
        new HypergeometricDistribution(-10, 5, 3);
    }

    // Test for constructor with numberOfSuccesses < 0
    @Test(expected = NotPositiveException.class)
    public void testConstructorNumberOfSuccessesNegative() {
        new HypergeometricDistribution(10, -5, 3);
    }

    // Test for constructor with sampleSize < 0
    @Test(expected = NotPositiveException.class)
    public void testConstructorSampleSizeNegative() {
        new HypergeometricDistribution(10, 5, -3);
    }

    // Test for constructor with numberOfSuccesses > populationSize
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 15, 3);
    }

    // Test for constructor with sampleSize > populationSize
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 10);
    }

    // Test for cumulativeProbability within domain
    @Test
    public void testCumulativeProbabilityWithinDomain() {
        assertEquals(0.6, hypergeometric.cumulativeProbability(1), 0.001);
    }

    // Test for cumulativeProbability below domain
    @Test
    public void testCumulativeProbabilityBelowDomain() {
        assertEquals(0.0, hypergeometric.cumulativeProbability(0), 0.001);
    }

    // Test for cumulativeProbability above domain
    @Test
    public void testCumulativeProbabilityAboveDomain() {
        assertEquals(1.0, hypergeometric.cumulativeProbability(5), 0.001);
    }

    // Test for getNumberOfSuccesses
    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(5, hypergeometric.getNumberOfSuccesses());
    }

    // Test for getPopulationSize
    @Test
    public void testGetPopulationSize() {
        assertEquals(10, hypergeometric.getPopulationSize());
    }

    // Test for getSampleSize
    @Test
    public void testGetSampleSize() {
        assertEquals(3, hypergeometric.getSampleSize());
    }

    // Test for probability within domain
    @Test
    public void testProbabilityWithinDomain() {
        assertEquals(0.4285714285, hypergeometric.probability(2), 0.001);
    }

    // Test for probability below domain
    @Test
    public void testProbabilityBelowDomain() {
        assertEquals(0.0, hypergeometric.probability(0), 0.001);
    }

    // Test for probability above domain
    @Test
    public void testProbabilityAboveDomain() {
        assertEquals(0.0, hypergeometric.probability(6), 0.001);
    }

    // Test for upperCumulativeProbability within domain
    @Test
    public void testUpperCumulativeProbabilityWithinDomain() {
        assertEquals(0.4, hypergeometric.upperCumulativeProbability(1), 0.001);
    }

    // Test for upperCumulativeProbability below domain
    @Test
    public void testUpperCumulativeProbabilityBelowDomain() {
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(0), 0.001);
    }

    // Test for upperCumulativeProbability above domain
    @Test
    public void testUpperCumulativeProbabilityAboveDomain() {
        assertEquals(0.0, hypergeometric.upperCumulativeProbability(5), 0.001);
    }

    // Test for getNumericalMean
    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, hypergeometric.getNumericalMean(), 0.001);
    }

    // Test for getNumericalVariance
    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.375, hypergeometric.getNumericalVariance(), 0.001);
    }

    // Test for getSupportLowerBound
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, hypergeometric.getSupportLowerBound());
    }

    // Test for getSupportUpperBound
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, hypergeometric.getSupportUpperBound());
    }

    // Test for isSupportConnected
    @Test
    public void testIsSupportConnected() {
        assertTrue(hypergeometric.isSupportConnected());
    }

}
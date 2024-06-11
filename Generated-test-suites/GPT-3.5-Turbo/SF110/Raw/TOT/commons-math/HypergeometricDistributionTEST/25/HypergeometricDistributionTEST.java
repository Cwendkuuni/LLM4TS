import org.junit.*;
import org.apache.commons.math3.distribution.*;

public class HypergeometricDistributionTest {
    
    private HypergeometricDistribution distribution;

    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(100, 50, 20);
    }

    @Test
    public void testConstructor_validParameters_success() {
        try {
            new HypergeometricDistribution(200, 100, 50);
        } catch (Exception e) {
            Assert.fail("Constructor threw an exception for valid parameters.");
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_negativePopulationSize_exceptionThrown() {
        new HypergeometricDistribution(-100, 50, 20);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_negativeNumberOfSuccesses_exceptionThrown() {
        new HypergeometricDistribution(100, -50, 20);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_negativeSampleSize_exceptionThrown() {
        new HypergeometricDistribution(100, 50, -20);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_numberOfSuccessesLargerThanPopulationSize_exceptionThrown() {
        new HypergeometricDistribution(100, 150, 20);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_sampleSizeLargerThanPopulationSize_exceptionThrown() {
        new HypergeometricDistribution(100, 50, 120);
    }

    @Test
    public void testGetNumericalMean_validParameters_success() {
        Assert.assertEquals(10.0, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance_validParameters_success() {
        Assert.assertEquals(2.2222, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound_validParameters_success() {
        Assert.assertEquals(30, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound_validParameters_success() {
        Assert.assertEquals(20, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected_validParameters_success() {
        Assert.assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testProbability_validInput_success() {
        Assert.assertEquals(0.0909, distribution.probability(15), 0.0001);
    }

    @Test
    public void testProbability_inputLessThanLowerBound_returnsZero() {
        Assert.assertEquals(0.0, distribution.probability(10), 0.0001);
    }

    @Test
    public void testProbability_inputGreaterThanUpperBound_returnsZero() {
        Assert.assertEquals(0.0, distribution.probability(25), 0.0001);
    }

    @Test
    public void testCumulativeProbability_validInput_success() {
        Assert.assertEquals(0.9958, distribution.cumulativeProbability(15), 0.0001);
    }

    @Test
    public void testCumulativeProbability_inputLessThanLowerBound_returnsZero() {
        Assert.assertEquals(0.0, distribution.cumulativeProbability(10), 0.0001);
    }

    @Test
    public void testCumulativeProbability_inputGreaterThanUpperBound_returnsOne() {
        Assert.assertEquals(1.0, distribution.cumulativeProbability(25), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability_validInput_success() {
        Assert.assertEquals(0.0041, distribution.upperCumulativeProbability(15), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability_inputLessThanLowerBound_returnsOne() {
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(10), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability_inputGreaterThanUpperBound_returnsZero() {
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(25), 0.0001);
    }

    @Test
    public void testGetSampleSize_validParameters_success() {
        Assert.assertEquals(20, distribution.getSampleSize());
    }

    @Test
    public void testGetPopulationSize_validParameters_success() {
        Assert.assertEquals(100, distribution.getPopulationSize());
    }

    @Test
    public void testGetNumberOfSuccesses_validParameters_success() {
        Assert.assertEquals(50, distribution.getNumberOfSuccesses());
    }
}
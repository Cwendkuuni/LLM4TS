import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {
    private static final double DELTA = 1e-9;

    @Test
    public void testConstructorValidParameters() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            Assert.assertEquals(10, distribution.getPopulationSize());
            Assert.assertEquals(5, distribution.getNumberOfSuccesses());
            Assert.assertEquals(3, distribution.getSampleSize());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorZeroPopulationSize() {
        try {
            new HypergeometricDistribution(0, 5, 3);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotPositiveException e) {
            Assert.assertEquals(LocalizedFormats.POPULATION_SIZE, e.getSpecificPattern());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorNegativeNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(10, -5, 3);
            Assert.fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            Assert.assertEquals(LocalizedFormats.NUMBER_OF_SUCCESSES, e.getSpecificPattern());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorNegativeSampleSize() {
        try {
            new HypergeometricDistribution(10, 5, -3);
            Assert.fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            Assert.assertEquals(LocalizedFormats.NUMBER_OF_SAMPLES, e.getSpecificPattern());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorNumberOfSuccessesGreaterThanPopulationSize() {
        try {
            new HypergeometricDistribution(10, 15, 3);
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE, e.getSpecificPattern());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorSampleSizeGreaterThanPopulationSize() {
        try {
            new HypergeometricDistribution(10, 5, 15);
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE, e.getSpecificPattern());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCumulativeProbabilityLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-1), DELTA);
    }

    @Test
    public void testCumulativeProbabilityUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(20), DELTA);
    }

    @Test
    public void testCumulativeProbabilityInnerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.974358974, distribution.cumulativeProbability(7), DELTA);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, distribution.getSampleSize());
    }

    @Test
    public void testProbabilityOutOfBounds() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.probability(-1), DELTA);
        Assert.assertEquals(0.0, distribution.probability(20), DELTA);
    }

    @Test
    public void testProbabilityInnerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0769230769, distribution.probability(3), DELTA);
    }

    @Test
    public void testUpperCumulativeProbabilityLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(-1), DELTA);
    }

    @Test
    public void testUpperCumulativeProbabilityUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(20), DELTA);
    }

    @Test
    public void testUpperCumulativeProbabilityInnerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0256410256, distribution.upperCumulativeProbability(7), DELTA);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.5, distribution.getNumericalMean(), DELTA);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.3571428571, distribution.getNumericalVariance(), DELTA);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertTrue(distribution.isSupportConnected());
    }
}
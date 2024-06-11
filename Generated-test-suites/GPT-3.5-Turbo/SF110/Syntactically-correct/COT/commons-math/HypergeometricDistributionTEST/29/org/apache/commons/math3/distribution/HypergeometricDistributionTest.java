import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructorWithValidArguments() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            Assert.assertNotNull(distribution);
        } catch (Exception e) {
            Assert.fail("Failed to create HypergeometricDistribution with valid arguments");
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() throws Exception {
        new HypergeometricDistribution(0, 5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() throws Exception {
        new HypergeometricDistribution(10, -1, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize() throws Exception {
        new HypergeometricDistribution(10, 5, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() throws Exception {
        new HypergeometricDistribution(10, 15, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() throws Exception {
        new HypergeometricDistribution(10, 5, 15);
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
    public void testProbabilityWithXLessThanLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.probability(0), 0.0001);
    }

    @Test
    public void testProbabilityWithXGreaterThanUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.probability(10), 0.0001);
    }

    @Test
    public void testProbabilityWithXInRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.08571, distribution.probability(3), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithXLessThanLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithXGreaterThanUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(10), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithXInRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.08571, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbabilityWithXLessThanOrEqualToLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbabilityWithXGreaterThanUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(10), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbabilityWithXInRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.91429, distribution.upperCumulativeProbability(3), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.875, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(2, distribution.getSupportLowerBound());
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

    @Test
    public void testAllPublicMethods() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

            Assert.assertNotNull(distribution);
            Assert.assertEquals(5, distribution.getNumberOfSuccesses());
            Assert.assertEquals(10, distribution.getPopulationSize());
            Assert.assertEquals(3, distribution.getSampleSize());
            Assert.assertEquals(0.08571, distribution.probability(3), 0.0001);
            Assert.assertEquals(0.08571, distribution.cumulativeProbability(3), 0.0001);
            Assert.assertEquals(0.91429, distribution.upperCumulativeProbability(3), 0.0001);
            Assert.assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
            Assert.assertEquals(0.875, distribution.getNumericalVariance(), 0.0001);
            Assert.assertEquals(2, distribution.getSupportLowerBound());
            Assert.assertEquals(3, distribution.getSupportUpperBound());
            Assert.assertTrue(distribution.isSupportConnected());

        } catch (Exception e) {
            Assert.fail("Failed to test all public methods of HypergeometricDistribution");
        }
    }
}
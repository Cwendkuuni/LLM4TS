import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution hypergeo = new HypergeometricDistribution(-10, 5, 2);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals(LocalizedFormats.POPULATION_SIZE, e.getSpecifier());
        }

        try {
            HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, -5, 2);
            Assert.fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            Assert.assertEquals(LocalizedFormats.NUMBER_OF_SUCCESSES, e.getSpecifier());
        }

        try {
            HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, -2);
            Assert.fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            Assert.assertEquals(LocalizedFormats.NUMBER_OF_SAMPLES, e.getSpecifier());
        }

        try {
            HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 15, 20);
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE, e.getSpecifier());
        }

        try {
            HypergeometricDistribution hypergeo = new HypergeometricDistribution(20, 10, 15);
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            Assert.assertEquals(LocalizedFormats.SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE, e.getSpecifier());
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(0.273, hypergeo.cumulativeProbability(1), 0.001);
        Assert.assertEquals(0.636, hypergeo.cumulativeProbability(2), 0.001);
        Assert.assertEquals(1.000, hypergeo.cumulativeProbability(3), 0.001);
        Assert.assertEquals(0.000, hypergeo.cumulativeProbability(0), 0.001);
        Assert.assertEquals(0.000, hypergeo.cumulativeProbability(4), 0.001);
        Assert.assertEquals(1.000, hypergeo.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(0.273, hypergeo.probability(1), 0.001);
        Assert.assertEquals(0.364, hypergeo.probability(2), 0.001);
        Assert.assertEquals(0.182, hypergeo.probability(3), 0.001);
        Assert.assertEquals(0.182, hypergeo.probability(0), 0.001);
        Assert.assertEquals(0.000, hypergeo.probability(4), 0.001);
        Assert.assertEquals(0.000, hypergeo.probability(5), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(0.727, hypergeo.upperCumulativeProbability(1), 0.001);
        Assert.assertEquals(0.364, hypergeo.upperCumulativeProbability(2), 0.001);
        Assert.assertEquals(0.000, hypergeo.upperCumulativeProbability(3), 0.001);
        Assert.assertEquals(1.000, hypergeo.upperCumulativeProbability(0), 0.001);
        Assert.assertEquals(1.000, hypergeo.upperCumulativeProbability(4), 0.001);
        Assert.assertEquals(0.000, hypergeo.upperCumulativeProbability(5), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(1.0, hypergeo.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(0.333, hypergeo.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(0, hypergeo.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(2, hypergeo.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 2);
        Assert.assertTrue(hypergeo.isSupportConnected());
    }
}
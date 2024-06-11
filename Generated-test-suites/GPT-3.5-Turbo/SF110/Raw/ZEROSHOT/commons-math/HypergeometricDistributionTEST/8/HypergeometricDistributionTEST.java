import org.junit.*;
import org.apache.commons.math3.exception.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_PositiveValues() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            Assert.assertEquals(10, dist.getPopulationSize());
            Assert.assertEquals(5, dist.getNumberOfSuccesses());
            Assert.assertEquals(3, dist.getSampleSize());
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativePopulationSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 3);
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
            Assert.assertEquals("Population size must be positive.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeNumberOfSuccesses() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
        } catch (NotPositiveException e) {
            // Exception expected
            Assert.assertEquals("Number of successes must be non-negative.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeSampleSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
        } catch (NotPositiveException e) {
            // Exception expected
            Assert.assertEquals("Sample size must be non-negative.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 12, 3);
        } catch (NumberIsTooLargeException e) {
            // Exception expected
            Assert.assertEquals("Number of successes must be less than or equal to the population size.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 13);
        } catch (NumberIsTooLargeException e) {
            // Exception expected
            Assert.assertEquals("Sample size must be less than or equal to the population size.", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        Assert.assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.0204081632, dist.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.1836734694, dist.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.6326530612, dist.cumulativeProbability(3), 0.0001);
        Assert.assertEquals(1.0, dist.cumulativeProbability(4), 0.0001);
        Assert.assertEquals(1.0, dist.cumulativeProbability(5), 0.0001);
        Assert.assertEquals(1.0, dist.cumulativeProbability(6), 0.0001);
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int[] domain = dist.getDomain(10, 5, 3);
        Assert.assertEquals(2, domain.length);
        Assert.assertEquals(2, domain[0]);
        Assert.assertEquals(5, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(2, dist.getLowerDomain(10, 5, 3));
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(5, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, dist.getSampleSize());
    }

    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, dist.getUpperDomain(5, 3));
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, dist.probability(-1), 0.0001);
        Assert.assertEquals(0.0, dist.probability(0), 0.0001);
        Assert.assertEquals(0.1142857143, dist.probability(1), 0.0001);
        Assert.assertEquals(0.4, dist.probability(2), 0.0001);
        Assert.assertEquals(0.4571428571, dist.probability(3), 0.0001);
        Assert.assertEquals(0.0285714286, dist.probability(4), 0.0001);
        Assert.assertEquals(0.0, dist.probability(5), 0.0001);
        Assert.assertEquals(0.0, dist.probability(6), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.0001);
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.9795918367, dist.upperCumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.8163265306, dist.upperCumulativeProbability(3), 0.0001);
        Assert.assertEquals(0.1836734694, dist.upperCumulativeProbability(4), 0.0001);
        Assert.assertEquals(0.0, dist.upperCumulativeProbability(5), 0.0001);
        Assert.assertEquals(0.0, dist.upperCumulativeProbability(6), 0.0001);
    }

    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.1142857143, dist.innerCumulativeProbability(1, 2, 1), 0.0001);
        Assert.assertEquals(0.5142857143, dist.innerCumulativeProbability(2, 3, 1), 0.0001);
        Assert.assertEquals(0.4571428571, dist.innerCumulativeProbability(3, 3, 1), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.7142857143, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.8571428571, dist.calculateNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertTrue(dist.isSupportConnected());
    }
}
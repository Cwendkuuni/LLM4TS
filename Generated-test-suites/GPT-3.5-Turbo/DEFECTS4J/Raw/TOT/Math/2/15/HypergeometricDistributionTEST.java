import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor_PositiveInputs() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
            Assert.assertNotNull(distribution);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativePopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-10, 5, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_TooManySuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 20, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_TooManySamples() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 20);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);

        // Typical use case
        Assert.assertEquals(0.2857142857143, distribution.probability(1), 0.0000000000001);

        // Edge case: x = 0
        Assert.assertEquals(0.1428571428571, distribution.probability(0), 0.0000000000001);

        // Edge case: x = k
        Assert.assertEquals(0.1428571428571, distribution.probability(2), 0.0000000000001);

        // Error scenario: x > k
        Assert.assertEquals(0.0, distribution.probability(3), 0.0000000000001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);

        // Typical use case
        Assert.assertEquals(0.4285714285714, distribution.cumulativeProbability(1), 0.0000000000001);

        // Edge case: x = 0
        Assert.assertEquals(0.2857142857143, distribution.cumulativeProbability(0), 0.0000000000001);

        // Edge case: x = k
        Assert.assertEquals(1.0, distribution.cumulativeProbability(2), 0.0000000000001);

        // Error scenario: x < 0
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0000000000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);

        // Typical use case
        Assert.assertEquals(0.5714285714286, distribution.upperCumulativeProbability(1), 0.0000000000001);

        // Edge case: x = k
        Assert.assertEquals(0.4285714285714, distribution.upperCumulativeProbability(2), 0.0000000000001);

        // Edge case: x < 0
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0000000000001);

        // Error scenario: x > k
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0000000000001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);

        // Typical use case
        Assert.assertEquals(1.0, distribution.getNumericalMean(), 0.0000000000001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);

        // Typical use case
        Assert.assertEquals(0.057142857142857, distribution.getNumericalVariance(), 0.0000000000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);

        // Typical use case
        Assert.assertEquals(2, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);

        // Typical use case
        Assert.assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);

        // Typical use case
        Assert.assertTrue(distribution.isSupportConnected());
    }
}
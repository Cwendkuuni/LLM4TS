import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    // Test HypergeometricDistribution constructor with valid inputs
    @Test
    public void testConstructor_ValidInputs() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(500, 40, 30);
            Assert.assertNotNull(dist);
        } catch (Exception e) {
            Assert.fail("Constructor threw an exception");
        }
    }

    // Test HypergeometricDistribution constructor with negative population size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativePopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(-100, 40, 30);
    }

    // Test HypergeometricDistribution constructor with zero population size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(0, 40, 30);
    }

    // Test HypergeometricDistribution constructor with negative number of successes
    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, -10, 30);
    }

    // Test HypergeometricDistribution constructor with negative sample size
    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 40, -20);
    }

    // Test HypergeometricDistribution constructor with number of successes greater than population size
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 150, 50);
    }

    // Test HypergeometricDistribution constructor with sample size greater than population size
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 30, 110);
    }

    // Test cumulativeProbability method with valid input within range
    @Test
    public void testCumulativeProbability_ValidInputWithinRange() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(500, 40, 30);
            double result = dist.cumulativeProbability(20);
            Assert.assertEquals(0.93543861, result, 1e-8);
        } catch (Exception e) {
            Assert.fail("Exception was thrown");
        }
    }

    // Test cumulativeProbability method with invalid input below lower bound
    @Test
    public void testCumulativeProbability_InvalidInputBelowLowerBound() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(500, 40, 30);
            double result = dist.cumulativeProbability(5);
            Assert.assertEquals(0.0, result, 1e-8);
        } catch (Exception e) {
            Assert.fail("Exception was thrown");
        }
    }

    // Test cumulativeProbability method with invalid input above upper bound
    @Test
    public void testCumulativeProbability_InvalidInputAboveUpperBound() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(500, 40, 30);
            double result = dist.cumulativeProbability(50);
            Assert.assertEquals(1.0, result, 1e-8);
        } catch (Exception e) {
            Assert.fail("Exception was thrown");
        }
    }

    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(500, 40, 30);
        double result = dist.getNumericalMean();
        Assert.assertEquals(24.0, result, 1e-8);
    }

    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(500, 40, 30);
        double result = dist.getNumericalVariance();
        Assert.assertEquals(5.10734644, result, 1e-8);
    }

    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(500, 40, 30);
        int result = dist.getSupportLowerBound();
        Assert.assertEquals(10, result);
    }

    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(500, 40, 30);
        int result = dist.getSupportUpperBound();
        Assert.assertEquals(30, result);
    }

    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(500, 40, 30);
        boolean result = dist.isSupportConnected();
        Assert.assertTrue(result);
    }

}
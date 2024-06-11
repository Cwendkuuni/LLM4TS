import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distr.getPopulationSize());
        assertEquals(5, distr.getNumberOfSuccesses());
        assertEquals(3, distr.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        HypergeometricDistribution distr = new HypergeometricDistribution(-10, 5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 15, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 15);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5 , 3);
        // Test x less than lower bound
        assertEquals(0.0, distr.cumulativeProbability(1), 0.0001);
        // Test x greater than upper bound
        assertEquals(1.0, distr.cumulativeProbability(7), 0.0001);
        // Test x within bounds
        assertEquals(0.571428, distr.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5 , 3);
        // Test x less than lower bound
        assertEquals(0.0, distr.probability(1), 0.0001);
        // Test x greater than upper bound
        assertEquals(0.0, distr.probability(7), 0.0001);
        // Test x within bounds
        assertEquals(0.285714, distr.probability(4), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5 , 3);
        // Test x less than or equal to lower bound
        assertEquals(1.0, distr.upperCumulativeProbability(1), 0.0001);
        // Test x greater than upper bound
        assertEquals(0.0, distr.upperCumulativeProbability(7), 0.0001);
        // Test x within bounds
        assertEquals(0.428571, distr.upperCumulativeProbability(4), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5 , 3);
        assertEquals(1.5, distr.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5 , 3);
        assertEquals(0.428571, distr.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5 , 3);
        assertEquals(2, distr.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5 , 3);
        assertEquals(3, distr.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5 , 3);
        assertEquals(true, distr.isSupportConnected());
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6428, hypergeometricDistribution.calculateNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSize() {
        new HypergeometricDistribution(0, 5, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithLargeNumberOfSuccesses() {
        new HypergeometricDistribution(10, 15, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithLargeSampleSize() {
        new HypergeometricDistribution(10, 5, 10);
    }

}
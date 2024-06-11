import org.junit.*;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(2, distribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-10, 5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 15);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.009090909, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.054545454, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.272727273, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.8, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.036363636, distribution.probability(1), 0.0001);
        assertEquals(0.327272727, distribution.probability(2), 0.0001);
        assertEquals(0.545454545, distribution.probability(3), 0.0001);
        assertEquals(0.218181818, distribution.probability(4), 0.0001);
        assertEquals(0.0, distribution.probability(5), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.99090909, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.936363636, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.727272727, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.2, distribution.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.233333333, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(2, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertTrue(distribution.isSupportConnected());
    }
}
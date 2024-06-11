import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setup() {
        distribution = new HypergeometricDistribution(10, 5, 2);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(2, distribution.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() {
        new HypergeometricDistribution(-10, 5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, -2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 15, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 15);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(2, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(2, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testProbability() {
        assertEquals(0.363636, distribution.probability(0), 0.000001);
        assertEquals(0.272727, distribution.probability(1), 0.000001);
        assertEquals(0.090909, distribution.probability(2), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.363636, distribution.cumulativeProbability(0), 0.000001);
        assertEquals(0.636363, distribution.cumulativeProbability(1), 0.000001);
        assertEquals(0.727273, distribution.cumulativeProbability(2), 0.000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.636364, distribution.upperCumulativeProbability(0), 0.000001);
        assertEquals(0.454545, distribution.upperCumulativeProbability(1), 0.000001);
        assertEquals(0.090909, distribution.upperCumulativeProbability(2), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.0, distribution.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.333333, distribution.getNumericalVariance(), 0.000001);
    }
}
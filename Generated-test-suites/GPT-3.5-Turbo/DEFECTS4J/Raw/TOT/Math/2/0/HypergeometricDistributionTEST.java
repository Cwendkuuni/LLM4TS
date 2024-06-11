import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() throws Exception {
        distribution = new HypergeometricDistribution(20, 10, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructPopulationSizeNegative() {
        distribution = new HypergeometricDistribution(-20, 10, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructNumberOfSuccessesNegative() {
        distribution = new HypergeometricDistribution(20, -10, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructSampleSizeNegative() {
        distribution = new HypergeometricDistribution(20, 10, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructNumberOfSuccessesLargerThanPopulationSize() {
        distribution = new HypergeometricDistribution(20, 25, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructSampleSizeLargerThanPopulationSize() {
        distribution = new HypergeometricDistribution(20, 10, 25);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(10, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(20, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        assertEquals(5, distribution.getSampleSize());
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(2.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.78125, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.6142857142857143, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.1, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.5428571428571428, distribution.probability(2), 0.0001);
        assertEquals(0.0, distribution.probability(6), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(0.9, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.38571428571428575, distribution.upperCumulativeProbability(5), 0.0001);
    }

    @Test
    public void testSampling() {
        RandomDataGenerator generator = new RandomDataGenerator();
        int sample = distribution.sample(generator);
        assertTrue(sample >= 0 && sample <= 5);
    }

}
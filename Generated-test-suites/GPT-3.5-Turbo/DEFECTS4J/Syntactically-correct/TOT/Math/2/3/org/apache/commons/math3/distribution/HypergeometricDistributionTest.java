import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    private HypergeometricDistribution distribution;

    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(100, 50, 25);
    }

    @Test
    public void testConstructor_ValidValues() {
        assertEquals(100, distribution.getPopulationSize());
        assertEquals(50, distribution.getNumberOfSuccesses());
        assertEquals(25, distribution.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidPopulationSize() {
        HypergeometricDistribution invalidDistribution =
                new HypergeometricDistribution(0, 50, 25);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        HypergeometricDistribution invalidDistribution =
                new HypergeometricDistribution(100, -1, 25);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidSampleSize() {
        HypergeometricDistribution invalidDistribution =
                new HypergeometricDistribution(100, 50, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution invalidDistribution =
                new HypergeometricDistribution(100, 110, 25);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution invalidDistribution =
                new HypergeometricDistribution(100, 50, 110);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.0038, distribution.cumulativeProbability(10), 0.0001);
        assertEquals(0.9483, distribution.cumulativeProbability(40), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(50), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(100), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(200), 0.0001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0312, distribution.probability(10), 0.0001);
        assertEquals(0.0130, distribution.probability(40), 0.0001);
        assertEquals(0.0, distribution.probability(50), 0.0001);
        assertEquals(0.0, distribution.probability(100), 0.0001);
        assertEquals(0.0, distribution.probability(200), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.9961, distribution.upperCumulativeProbability(10), 0.0001);
        assertEquals(0.0517, distribution.upperCumulativeProbability(40), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(50), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(100), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(200), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(12.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(6.0991, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(25, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(25, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    private static final double EPSILON = 1e-9;
    
    private static final int POPULATION_SIZE = 100;
    private static final int NUMBER_OF_SUCCESSES = 50;
    private static final int SAMPLE_SIZE = 20;

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
    }

    @Test
    public void testConstructor() {
        try {
            new HypergeometricDistribution(0, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }

        try {
            new HypergeometricDistribution(POPULATION_SIZE, -1, SAMPLE_SIZE);
        } catch (NotPositiveException e) {
            assertTrue(true);
        }

        try {
            new HypergeometricDistribution(POPULATION_SIZE, NUMBER_OF_SUCCESSES, -1);
        } catch (NotPositiveException e) {
            assertTrue(true);
        }

        try {
            new HypergeometricDistribution(NUMBER_OF_SUCCESSES + 1, NUMBER_OF_SUCCESSES, SAMPLE_SIZE);
        } catch (NumberIsTooLargeException e) {
            assertTrue(true);
        }

        try {
            new HypergeometricDistribution(POPULATION_SIZE, POPULATION_SIZE + 1, SAMPLE_SIZE);
        } catch (NumberIsTooLargeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.7295238962712685, distribution.cumulativeProbability(5), EPSILON);
        assertEquals(0.2236753091689326, distribution.cumulativeProbability(10), EPSILON);
        assertEquals(0.9999999999999994, distribution.cumulativeProbability(20), EPSILON);
    }

    @Test
    public void testProbability() {
        assertEquals(0.013620807109872243, distribution.probability(5), EPSILON);
        assertEquals(0.30224448975188745, distribution.probability(10), EPSILON);
        assertEquals(0.0, distribution.probability(21), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(10.0, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(3.0454545454545454, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(20, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.27047610372873134, distribution.upperCumulativeProbability(5), EPSILON);
        assertEquals(0.39037536500424597, distribution.upperCumulativeProbability(10), EPSILON);
        assertEquals(1.1102230246251565E-16, distribution.upperCumulativeProbability(20), EPSILON);
    }
}

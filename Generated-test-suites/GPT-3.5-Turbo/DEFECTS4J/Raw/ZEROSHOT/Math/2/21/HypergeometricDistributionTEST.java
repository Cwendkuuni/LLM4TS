import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(10, 5, 3);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        assertEquals(3, distribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.42857142857142855, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.42857142857142855, distribution.probability(1), 0.0001);
        assertEquals(0.5714285714285714, distribution.probability(2), 0.0001);
        assertEquals(0.0, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.5714285714285714, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.6428571428571429, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}
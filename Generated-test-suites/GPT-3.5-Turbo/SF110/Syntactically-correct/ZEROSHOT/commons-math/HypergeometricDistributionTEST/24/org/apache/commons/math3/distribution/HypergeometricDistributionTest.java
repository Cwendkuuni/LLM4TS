import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {

    HypergeometricDistribution distribution;

    @Before
    public void setUp() throws Exception {
        distribution = new HypergeometricDistribution(10, 5, 7);
    }

    @After
    public void tearDown() throws Exception {
        distribution = null;
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0000001);
        assertEquals(0.0, distribution.cumulativeProbability(2), 0.0000001);
        assertEquals(0.3333333333, distribution.cumulativeProbability(3), 0.0000001);
        assertEquals(0.6666666667, distribution.cumulativeProbability(4), 0.0000001);
        assertEquals(1.0, distribution.cumulativeProbability(7), 0.0000001);
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
        assertEquals(7, distribution.getSampleSize());
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(0), 0.0000001);
        assertEquals(0.0, distribution.probability(2), 0.0000001);
        assertEquals(0.0769230769, distribution.probability(3), 0.0000001);
        assertEquals(0.6666666667, distribution.probability(4), 0.0000001);
        assertEquals(0.0285714286, distribution.probability(7), 0.0000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0000001);
        assertEquals(0.0, distribution.upperCumulativeProbability(2), 0.0000001);
        assertEquals(0.3333333333, distribution.upperCumulativeProbability(3), 0.0000001);
        assertEquals(0.6666666667, distribution.upperCumulativeProbability(4), 0.0000001);
        assertEquals(0.0457142857, distribution.upperCumulativeProbability(7), 0.0000001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(3.5, distribution.getNumericalMean(), 0.0000001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.4583333333, distribution.getNumericalVariance(), 0.0000001);
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
}
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(10, 4, 2);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.2727, distribution.probability(0), 0.0001);
        assertEquals(0.3636, distribution.probability(1), 0.0001);
        assertEquals(0.3636, distribution.probability(2), 0.0001);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.7272, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.3636, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        assertEquals(0.8, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        assertEquals(0.16, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        assertEquals(1, distribution.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        assertEquals(2, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}
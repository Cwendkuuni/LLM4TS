import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(100, 20, 10);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.182, distribution.cumulativeProbability(5), 0.001);
        assertEquals(0.676, distribution.cumulativeProbability(10), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(20), 0.001);
    }

    @Test
    public void testGetDomain() {
        int[] domain = distribution.getDomain(100, 20, 10);
        assertArrayEquals(new int[]{10, 20}, domain);
    }

    @Test
    public void testGetLowerDomain() {
        int lowerDomain = distribution.getLowerDomain(100, 20, 10);
        assertEquals(10, lowerDomain);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        int numberOfSuccesses = distribution.getNumberOfSuccesses();
        assertEquals(20, numberOfSuccesses);
    }

    @Test
    public void testGetPopulationSize() {
        int populationSize = distribution.getPopulationSize();
        assertEquals(100, populationSize);
    }

    @Test
    public void testGetSampleSize() {
        int sampleSize = distribution.getSampleSize();
        assertEquals(10, sampleSize);
    }

    @Test
    public void testGetUpperDomain() {
        int upperDomain = distribution.getUpperDomain(20, 10);
        assertEquals(10, upperDomain);
    }

    @Test
    public void testProbability() {
        assertEquals(0.176, distribution.probability(5), 0.001);
        assertEquals(0.193, distribution.probability(10), 0.001);
        assertEquals(0.14, distribution.probability(20), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.818, distribution.upperCumulativeProbability(5), 0.001);
        assertEquals(0.324, distribution.upperCumulativeProbability(10), 0.001);
        assertEquals(0.0, distribution.upperCumulativeProbability(20), 0.001);
    }

    @Test
    public void testInnerCumulativeProbability() {
        double innerCumulativeProbability = distribution.innerCumulativeProbability(10, 20, 1);
        assertEquals(1.0, innerCumulativeProbability, 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(2.0, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.285, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        double calculateNumericalVariance = distribution.calculateNumericalVariance();
        assertEquals(1.285, calculateNumericalVariance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        int supportLowerBound = distribution.getSupportLowerBound();
        assertEquals(10, supportLowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        int supportUpperBound = distribution.getSupportUpperBound();
        assertEquals(10, supportUpperBound);
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(100, 50, 20);
    }

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        double cumulativeProb = distribution.cumulativeProbability(10);
        assertEquals(0.047, cumulativeProb, 0.001);

        // Edge case: value less than lower bound
        double cumulativeProb2 = distribution.cumulativeProbability(-5);
        assertEquals(0.0, cumulativeProb2, 0.001);

        // Edge case: value equal to upper bound
        double cumulativeProb3 = distribution.cumulativeProbability(20);
        assertEquals(1.0, cumulativeProb3, 0.001);

        // Error scenario: value greater than upper bound
        double cumulativeProb4 = distribution.cumulativeProbability(25);
        assertEquals(0.0, cumulativeProb4, 0.001);
    }

    @Test
    public void testProbability() {
        // Typical use case
        double probability = distribution.probability(15);
        assertEquals(0.064, probability, 0.001);

        // Edge case: value less than lower bound
        double probability2 = distribution.probability(-5);
        assertEquals(0.0, probability2, 0.001);

        // Edge case: value equal to upper bound
        double probability3 = distribution.probability(20);
        assertEquals(0.012, probability3, 0.001);

        // Error scenario: value greater than upper bound
        double probability4 = distribution.probability(25);
        assertEquals(0.0, probability4, 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Typical use case
        double upperCumulativeProb = distribution.upperCumulativeProbability(10);
        assertEquals(0.90, upperCumulativeProb, 0.001);

        // Edge case: value less than lower bound
        double upperCumulativeProb2 = distribution.upperCumulativeProbability(-5);
        assertEquals(1.0, upperCumulativeProb2, 0.001);

        // Edge case: value equal to upper bound
        double upperCumulativeProb3 = distribution.upperCumulativeProbability(20);
        assertEquals(0.0, upperCumulativeProb3, 0.001);

        // Error scenario: value greater than upper bound
        double upperCumulativeProb4 = distribution.upperCumulativeProbability(25);
        assertEquals(0.0, upperCumulativeProb4, 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        double mean = distribution.getNumericalMean();
        assertEquals(10.0, mean, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        double variance = distribution.getNumericalVariance();
        assertEquals(2.222, variance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        int lowerBound = distribution.getSupportLowerBound();
        assertEquals(30, lowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        int upperBound = distribution.getSupportUpperBound();
        assertEquals(20, upperBound);
    }

    @Test
    public void testIsSupportConnected() {
        boolean isSupportConnected = distribution.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}
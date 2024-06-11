import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    // Test probability method

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.22313016014842982, distribution.probability(1), 0.00001);
    }

    @Test
    public void testProbabilityWithNegativeX() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.0, distribution.probability(-1), 0.00001);
    }

    @Test
    public void testProbabilityWithZeroX() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.22313016014842982, distribution.probability(0), 0.00001);
    }

    // Test cumulativeProbability method

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.5578254003710751, distribution.cumulativeProbability(1), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeX() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityWithZeroX() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.22313016014842982, distribution.cumulativeProbability(0), 0.00001);
    }

    // Test normalApproximateProbability method

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.6680859088784478, distribution.normalApproximateProbability(1), 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityWithNegativeX() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.0, distribution.normalApproximateProbability(-1), 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityWithZeroX() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.22313016014842982, distribution.normalApproximateProbability(0), 0.00001);
    }

    // Test sample method

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

}
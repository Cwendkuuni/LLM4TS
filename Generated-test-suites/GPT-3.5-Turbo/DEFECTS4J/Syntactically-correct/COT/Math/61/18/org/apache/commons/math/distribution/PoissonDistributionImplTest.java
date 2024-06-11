import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        // Setup
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);

        // Verify
        assertEquals(5, distribution.getMean(), 0.00001);
    }

    @Test
    public void testProbability() {
        // Setup
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);

        // Verify
        assertEquals(0.03368, distribution.probability(3), 0.00001);
        assertEquals(0.08208, distribution.probability(6), 0.00001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Setup
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);

        // Verify
        assertEquals(0.26502, distribution.cumulativeProbability(3), 0.00001);
        assertEquals(0.95229, distribution.cumulativeProbability(8), 0.00001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        // Setup
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);

        // Verify
        assertEquals(0.67417, distribution.normalApproximateProbability(3), 0.00001);
        assertEquals(0.91924, distribution.normalApproximateProbability(7), 0.00001);
    }

    @Test
    public void testSample() throws MathException {
        // Setup
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);

        // Verify
        int sampleValue = distribution.sample();
        assertTrue(sampleValue >= 0 && sampleValue <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        // Setup
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);

        // Verify
        assertEquals(0, distribution.getDomainLowerBound(0.5), 0.00001);
    }

    @Test
    public void testGetDomainUpperBound() {
        // Setup
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);

        // Verify
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.00001);
    }
}
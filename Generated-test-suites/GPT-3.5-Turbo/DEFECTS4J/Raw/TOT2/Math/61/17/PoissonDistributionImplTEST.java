import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl distribution;

    @Test
    public void testProbabilityForValidInput() {
        distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.27067, distribution.probability(2), 0.00001);
    }

    @Test
    public void testProbabilityForZeroInput() {
        distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.13534, distribution.probability(0), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityForValidInput() {
        distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.9197, distribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityForNegativeInput() {
        distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityForValidInput() {
        distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.7112, distribution.normalApproximateProbability(2), 0.0001);
    }

    @Test
    public void testSample() {
        distribution = new PoissonDistributionImpl(2.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
    }
}
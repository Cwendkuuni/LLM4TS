import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);

    @Test
    public void testProbability() {
        assertEquals(0.0, poissonDistribution.probability(-1), 0.0001);
        assertEquals(0.2231, poissonDistribution.probability(0), 0.0001);
        assertEquals(0.3347, poissonDistribution.probability(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.2231, poissonDistribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.5578, poissonDistribution.cumulativeProbability(1), 0.0001);
        assertEquals(1.0, poissonDistribution.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() {
        assertEquals(0.7157, poissonDistribution.normalApproximateProbability(1), 0.0001);
        assertEquals(0.9772, poissonDistribution.normalApproximateProbability(2), 0.0001);
        assertEquals(0.9996, poissonDistribution.normalApproximateProbability(4), 0.0001);
    }

    @Test
    public void testSample() {
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new PoissonDistributionImpl(2.5);
    }

    @Test
    public void testMean() {
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0668, distribution.probability(0), 0.0001);
        assertEquals(0.1839, distribution.probability(1), 0.0001);
        assertEquals(0.2622, distribution.probability(2), 0.0001);
        assertEquals(0.2310, distribution.probability(3), 0.0001);
        assertEquals(0.1444, distribution.probability(4), 0.0001);
        assertEquals(0.0509, distribution.probability(5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0668, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.2507, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.5129, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.7439, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.8883, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(0.9392, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() {
        assertEquals(0.1187, distribution.normalApproximateProbability(0), 0.0001);
        assertEquals(0.7141, distribution.normalApproximateProbability(1), 0.0001);
        assertEquals(0.9641, distribution.normalApproximateProbability(2), 0.0001);
        assertEquals(0.9988, distribution.normalApproximateProbability(3), 0.0001);
        assertEquals(0.9999, distribution.normalApproximateProbability(4), 0.0001);
        assertEquals(1.0, distribution.normalApproximateProbability(5), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }
}
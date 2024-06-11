import static org.junit.Assert.*;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.08507, distribution.probability(0), 0.0001);
        assertEquals(0.2137, distribution.probability(1), 0.0001);
        assertEquals(0.338, distribution.probability(2), 0.0001);
        assertEquals(0.355, distribution.probability(3), 0.0001);
        assertEquals(0.2218, distribution.probability(4), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.082084, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.295175, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.633311, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.889623, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.99609, distribution.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.203076, distribution.normalApproximateProbability(0), 0.0001);
        assertEquals(0.593085, distribution.normalApproximateProbability(1), 0.0001);
        assertEquals(0.85877, distribution.normalApproximateProbability(2), 0.0001);
        assertEquals(0.978256, distribution.normalApproximateProbability(3), 0.0001);
        assertEquals(0.999911, distribution.normalApproximateProbability(4), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}
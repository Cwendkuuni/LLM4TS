import org.junit.Test;
import org.apache.commons.math.distribution.PoissonDistributionImpl;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, dist.getMean(), 0.001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(0.03368973499542734, dist.probability(2), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(0.9279271138263841, dist.cumulativeProbability(4), 0.001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals(0.8228082162572562, dist.normalApproximateProbability(4), 0.001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        int sample = dist.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }
}
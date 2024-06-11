import org.apache.commons.math.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.2707, distribution.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        try {
            assertEquals(0.1991, distribution.cumulativeProbability(2), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        try {
            assertEquals(0.5, distribution.normalApproximateProbability(3), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        try {
            int sample = distribution.sample();
            // Sample should be a non-negative integer
            assertEquals(true, sample >= 0);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
}
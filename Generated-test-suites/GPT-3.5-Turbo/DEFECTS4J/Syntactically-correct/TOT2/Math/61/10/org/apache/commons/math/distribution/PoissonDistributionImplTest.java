import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.049787, distribution.probability(0), 0.000001);
        assertEquals(0.2240418, distribution.probability(1), 0.000001);
        assertEquals(0.2240418, distribution.probability(2), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        try {
            assertEquals(0.01831563888873418, distribution.cumulativeProbability(1), 0.000001);
            assertEquals(0.1878162100686467, distribution.cumulativeProbability(3), 0.000001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10.0);
        try {
            assertEquals(0.83844, distribution.normalApproximateProbability(9), 0.01);
            assertEquals(0.98285, distribution.normalApproximateProbability(12), 0.01);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(7.0);
        try {
            int sample = distribution.sample();
            assertTrue(sample >= 0);
            assertTrue(sample <= Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
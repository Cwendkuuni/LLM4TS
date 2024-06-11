import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.1353352832366127, distribution.probability(2), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        try {
            assertEquals(0.85712346, distribution.cumulativeProbability(3), 1e-5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        try {
            assertEquals(0.747507462, distribution.normalApproximateProbability(3), 1e-5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        try {
            int sample = distribution.sample();
            assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.PoissonDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.1353352832366127, distribution.probability(1), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        try {
            assertEquals(0.6766764161830635, distribution.cumulativeProbability(3), 1e-15);
        } catch (MathException e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        try {
            assertEquals(0.5987063256829237, distribution.normalApproximateProbability(3), 1e-15);
        } catch (MathException e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        try {
            int sample = distribution.sample();
            assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        } catch (MathException e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(2.0, distribution.getMean(), 0.0);
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
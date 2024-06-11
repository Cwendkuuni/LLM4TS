import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor() {
        try {
            PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
            assertEquals(2.0, distribution.getMean(), 0.0);
        } catch (IllegalArgumentException e) {
            fail("Constructor should not throw IllegalArgumentException with positive mean");
        }
        
        try {
            new PoissonDistributionImpl(-2.0);
            fail("Constructor should throw IllegalArgumentException with negative mean");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.2706705664732254, distribution.probability(1), 1e-10);
        assertEquals(0.1353352832366127, distribution.probability(0), 1e-10);
        assertEquals(0.03625393373054712, distribution.probability(3), 1e-10);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.32332358381693654, distribution.cumulativeProbability(1), 1e-10);
        assertEquals(0.8646647167633873, distribution.cumulativeProbability(3), 1e-10);
        try {
            distribution.cumulativeProbability(-1);
            fail("cumulativeProbability should throw MathException with negative argument");
        } catch (MathException e) {
            // success
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.15957691228882384, distribution.normalApproximateProbability(1), 1e-10);
        assertEquals(0.9544997361036416, distribution.normalApproximateProbability(3), 1e-10);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
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
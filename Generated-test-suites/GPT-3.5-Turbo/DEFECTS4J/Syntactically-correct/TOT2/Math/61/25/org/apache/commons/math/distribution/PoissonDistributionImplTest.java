import static org.junit.Assert.*;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(Math.exp(-2.5), distribution.probability(0), 0.0001);
        assertEquals(2.5, distribution.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        try {
            assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
            assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
            assertEquals(0.543813, distribution.cumulativeProbability(2), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        try {
            assertEquals(0.893035, distribution.normalApproximateProbability(2), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        try {
            assertTrue(distribution.sample() >= 0);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

}
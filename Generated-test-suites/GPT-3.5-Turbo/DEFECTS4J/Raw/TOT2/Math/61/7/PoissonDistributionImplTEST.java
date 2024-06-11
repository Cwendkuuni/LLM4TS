import static org.junit.Assert.*;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);
        assertEquals(0.2706705664732254, poisson.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.0);
        assertEquals(0.42319008112684364, poisson.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(4.0);
        assertEquals(0.6914624612740131, poisson.normalApproximateProbability(3), 0.0001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        int sample = poisson.sample();
        assertTrue(sample >= 0);
    }

    @Test
    public void testInvalidMeanConstructor() {
        try {
            new PoissonDistributionImpl(0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInvalidCumulativeProbabilityInput() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);
        try {
            poisson.cumulativeProbability(-1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }
}
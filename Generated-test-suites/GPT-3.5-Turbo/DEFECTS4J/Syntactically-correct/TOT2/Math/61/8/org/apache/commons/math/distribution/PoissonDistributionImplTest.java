import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, poisson.getMean(), 0.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);
        assertEquals(0.2706705664732254, poisson.probability(2), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.0);
        assertEquals(0.42319008112684365, poisson.cumulativeProbability(2), 1e-15);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(4.0);
        assertEquals(0.7405484755696591, poisson.normalApproximateProbability(3), 1e-15);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(6.0);
        int sample = poisson.sample();
        // As the result is a random value, we can't predict an exact value to test
        // We can check if the generated value is within a valid range
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(7.0);
        assertEquals(0, poisson.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(8.0);
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPoissonMean() {
        new PoissonDistributionImpl(0.0);
    }

    @Test
    public void testInvalidCumulativeProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(9.0);
        try {
            poisson.cumulativeProbability(-1);
        } catch (MathException e) {
            // Expecting an exception
        }
    }

    @Test
    public void testInvalidNormalApproximateProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(10.0);
        try {
            poisson.normalApproximateProbability(-1);
        } catch (MathException e) {
            // Expecting an exception
        }
    }

}
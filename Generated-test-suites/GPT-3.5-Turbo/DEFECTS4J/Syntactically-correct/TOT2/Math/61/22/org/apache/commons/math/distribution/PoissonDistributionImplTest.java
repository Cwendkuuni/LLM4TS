import static org.junit.Assert.*;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor_DefaultEpsilon_MaxIterations() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(5, distribution.getMean(), 0);
        assertEquals(PoissonDistributionImpl.DEFAULT_EPSILON, distribution.epsilon, 0);
        assertEquals(PoissonDistributionImpl.DEFAULT_MAX_ITERATIONS, distribution.maxIterations);
    }

    @Test
    public void testConstructor_CustomEpsilon_MaxIterations() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(7, 1E-10, 20000000);
        assertEquals(7, distribution.getMean(), 0);
        assertEquals(1E-10, distribution.epsilon, 0);
        assertEquals(20000000, distribution.maxIterations);
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(3, distribution.getMean(), 0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2);
        assertEquals(0.2706705664732254, distribution.probability(2), 1e-10);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(0.42319008112684364, distribution.cumulativeProbability(2), 1e-10);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4);
        assertEquals(0.7969245270248328, distribution.normalApproximateProbability(3), 1e-10);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0, distribution.getDomainLowerBound(0), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(6);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1), 0);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(7);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeMean() {
        new PoissonDistributionImpl(-3);
    }

    @Test
    public void testProbability_LargeValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.0, distribution.probability(Integer.MAX_VALUE), 0);
    }

    @Test
    public void testCumulativeProbability_LargeValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(1, distribution.cumulativeProbability(Integer.MAX_VALUE), 0);
    }

}
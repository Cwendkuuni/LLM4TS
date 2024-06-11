import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        assertEquals(3.5, distribution.getMean(), 0.001);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.2706705664719236, distribution.probability(2), 1e-15);
    }
    
    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.7977478271531458, distribution.cumulativeProbability(2), 1e-15);
    }
    
    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10.0);
        assertEquals(0.9984345977419972, distribution.normalApproximateProbability(15), 1e-15);
    }
    
    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        int sample = distribution.sample();
        assert(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}
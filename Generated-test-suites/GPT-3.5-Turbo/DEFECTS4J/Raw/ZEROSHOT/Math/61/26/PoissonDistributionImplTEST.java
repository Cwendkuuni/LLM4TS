import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(3, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(0.1493612051, distribution.probability(2), 0.0000000001);
    }
    
    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(0.4231900811, distribution.cumulativeProbability(2), 0.0000000001);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(0.5286554218, distribution.normalApproximateProbability(2), 0.0000000001);
    }
    
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        int sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}
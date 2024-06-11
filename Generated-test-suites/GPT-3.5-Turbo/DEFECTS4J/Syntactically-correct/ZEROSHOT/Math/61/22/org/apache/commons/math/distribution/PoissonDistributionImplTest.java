import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PoissonDistributionImplTest {
    
    private static final double DELTA = 1e-15;
    private PoissonDistributionImpl poissonDistribution;
    
    @Before
    public void setUp() {
        poissonDistribution = new PoissonDistributionImpl(1.5);
    }
    
    @Test
    public void testGetMean() {
        assertEquals(1.5, poissonDistribution.getMean(), DELTA);
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.22313016014842982, poissonDistribution.probability(0), DELTA);
        assertEquals(0.3346952402226443, poissonDistribution.probability(1), DELTA);
        assertEquals(0.25102193016698324, poissonDistribution.probability(2), DELTA);
        assertEquals(0.12551096508349162, poissonDistribution.probability(3), DELTA);
        assertEquals(0.047800057674013524, poissonDistribution.probability(4), DELTA);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.22313016014842982, poissonDistribution.cumulativeProbability(0), DELTA);
        assertEquals(0.5578254003710741, poissonDistribution.cumulativeProbability(1), DELTA);
        assertEquals(0.8088473305380573, poissonDistribution.cumulativeProbability(2), DELTA);
        assertEquals(0.9343582956215488, poissonDistribution.cumulativeProbability(3), DELTA);
        assertEquals(0.9821583532955623, poissonDistribution.cumulativeProbability(4), DELTA);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.6479799767909776, poissonDistribution.normalApproximateProbability(0), DELTA);
        assertEquals(0.9200444146169373, poissonDistribution.normalApproximateProbability(1), DELTA);
        assertEquals(0.9970855939511103, poissonDistribution.normalApproximateProbability(2), DELTA);
        assertEquals(0.999865102011725, poissonDistribution.normalApproximateProbability(3), DELTA);
        assertEquals(0.9999952828519153, poissonDistribution.normalApproximateProbability(4), DELTA);
    }
    
    @Test
    public void testSample() throws MathException {
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, poissonDistribution.getDomainLowerBound(0.3), DELTA);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.3), DELTA);
    }
}
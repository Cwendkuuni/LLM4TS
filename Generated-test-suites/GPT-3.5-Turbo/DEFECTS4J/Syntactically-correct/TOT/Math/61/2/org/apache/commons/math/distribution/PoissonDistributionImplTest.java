import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {
    
    private PoissonDistributionImpl poissonDistribution;
    
    @Before
    public void setUp() {
        poissonDistribution = new PoissonDistributionImpl(2.5);
    }
    
    @Test
    public void testProbability() {
        // typical use case
        assertEquals(0.18394, poissonDistribution.probability(3), 0.00001);
        // edge case
        assertEquals(0.02275, poissonDistribution.probability(0), 0.00001);
        // error scenario
        assertEquals(0.0, poissonDistribution.probability(-1), 0.00001);
        assertEquals(0.0, poissonDistribution.probability(Integer.MAX_VALUE), 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() {
        // typical use case
        assertEquals(0.54314, poissonDistribution.cumulativeProbability(3), 0.00001);
        // edge case
        assertEquals(0.09158, poissonDistribution.cumulativeProbability(0), 0.00001);
        // error scenario
        assertEquals(0.0, poissonDistribution.cumulativeProbability(-1), 0.00001);
        assertEquals(1.0, poissonDistribution.cumulativeProbability(Integer.MAX_VALUE), 0.00001);
    }
    
    @Test
    public void testNormalApproximateProbability() {
        // typical use case
        assertEquals(0.46098, poissonDistribution.normalApproximateProbability(3), 0.00001);
        // edge case
        assertEquals(0.07966, poissonDistribution.normalApproximateProbability(0), 0.00001);
        // error scenario
        assertEquals(0.0, poissonDistribution.normalApproximateProbability(-1), 0.00001);
    }
    
    @Test
    public void testSample() {
        // typical use case
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }
    
    @Test
    public void testGetMean() {
        // typical use case
        assertEquals(2.5, poissonDistribution.getMean(), 0.00001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        // typical use case
        assertEquals(0, poissonDistribution.getDomainLowerBound(0.5));
    }
    
    @Test
    public void testGetDomainUpperBound() {
        // typical use case
        assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.5));
    }
}
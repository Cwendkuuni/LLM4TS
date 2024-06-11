import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new PoissonDistributionImpl(5);
    }

    @Test
    public void testGetMean() {
        assertEquals(5.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testProbability() {
        assertEquals(0.175, distribution.probability(2), 0.001);
        assertEquals(0.0336, distribution.probability(0), 0.001);
        assertEquals(0.0071, distribution.probability(8), 0.001);
        assertEquals(0.0, distribution.probability(-1), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.12465, distribution.cumulativeProbability(3), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.001);
        assertEquals(0.00674, distribution.cumulativeProbability(-1), 0.001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.756, distribution.normalApproximateProbability(6), 0.001);
        assertEquals(0.439, distribution.normalApproximateProbability(3), 0.001);
        assertEquals(0.0, distribution.normalApproximateProbability(-1), 0.001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl poisson;

    @Before
    public void setUp() {
        poisson = new PoissonDistributionImpl(3.5);
    }

    @Test
    public void testGetMean() {
        assertEquals(3.5, poisson.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0301974, poisson.probability(2), 0.0001);
        assertEquals(0.2181393, poisson.probability(4), 0.0001);
        assertEquals(0.0, poisson.probability(-1), 0.0001);
        assertEquals(0.0, poisson.probability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0198641, poisson.cumulativeProbability(2), 0.0001);
        assertEquals(0.9252610, poisson.cumulativeProbability(7), 0.0001);
        assertEquals(0.0, poisson.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, poisson.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.1160856, poisson.normalApproximateProbability(2), 0.0001);
        assertEquals(0.2319234, poisson.normalApproximateProbability(4), 0.0001);
        assertEquals(0.0, poisson.normalApproximateProbability(-1), 0.0001);
        assertEquals(1.0, poisson.normalApproximateProbability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = poisson.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}
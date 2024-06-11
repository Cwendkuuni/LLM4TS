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
        assertEquals(5, distribution.getMean(), 0);
    }

    @Test
    public void testProbability() {
        assertEquals(0.1754673697678507, distribution.probability(2), 0.00001);
    }

    @Test
    public void testProbabilityWithNegativeInput() {
        assertEquals(0.0, distribution.probability(-2), 0.00001);
    }

    @Test
    public void testProbabilityWithMaxInput() {
        assertEquals(0.0, distribution.probability(Integer.MAX_VALUE), 0.00001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.12465201948308106, distribution.cumulativeProbability(2), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeInput() throws MathException {
        assertEquals(0.0, distribution.cumulativeProbability(-2), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityWithMaxInput() throws MathException {
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.00001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.48168438521550095, distribution.normalApproximateProbability(2), 0.00001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, distribution.getDomainLowerBound(0.5), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
    }

}
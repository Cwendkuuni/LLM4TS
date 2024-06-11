import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(-2.5);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.205199, distribution.probability(3), 0.000001);
    }

    @Test
    public void testProbabilityNegativeInput() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, distribution.probability(-3), 0.000001);
    }

    @Test
    public void testProbabilityMaxValueInput() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, distribution.probability(Integer.MAX_VALUE), 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.543813, distribution.cumulativeProbability(3), 0.000001);
    }

    @Test
    public void testCumulativeProbabilityNegativeInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, distribution.cumulativeProbability(-3), 0.000001);
    }

    @Test
    public void testCumulativeProbabilityMaxValueInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.000001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.591988, distribution.normalApproximateProbability(3), 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0, distribution.getDomainLowerBound(0.0), 0.000001);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1.0), 0.000001);
    }
}
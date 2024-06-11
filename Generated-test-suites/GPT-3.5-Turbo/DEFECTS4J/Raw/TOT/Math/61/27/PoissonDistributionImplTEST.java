import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability_whenXEqualsZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        double expected = Math.exp(-3.0);
        double actual = distribution.probability(0);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testProbability_whenXIsNegative() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        double expected = 0.0;
        double actual = distribution.probability(-1);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testProbability_whenXIsPositive() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        double expected = Math.exp(-3.0 - PoissonDistributionImpl.SaddlePointExpansion.getStirlingError(3) -
                PoissonDistributionImpl.SaddlePointExpansion.getDeviancePart(3, 3.0)) /
                Math.sqrt(2 * Math.PI * 3);
        double actual = distribution.probability(3);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testCumulativeProbability_whenXEqualsZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        double expected = 0.0504021746;
        double actual = distribution.cumulativeProbability(0);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testCumulativeProbability_whenXIsNegative() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        double expected = 0.0;
        double actual = distribution.cumulativeProbability(-1);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testCumulativeProbability_whenXIsPositive() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        double expected = 0.950796443;
        double actual = distribution.cumulativeProbability(3);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testNormalApproximateProbability_whenXEqualsZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        double expected = 0.3520653268;
        double actual = distribution.normalApproximateProbability(0);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testNormalApproximateProbability_whenXIsPositive() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        double expected = 0.7127025049;
        double actual = distribution.normalApproximateProbability(3);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

}
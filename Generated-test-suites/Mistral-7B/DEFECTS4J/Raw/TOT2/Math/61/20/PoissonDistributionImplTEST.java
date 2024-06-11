import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor_ValidParameters() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        assertEquals("Mean should be set correctly", 3.0, dist.getMean(), 0.001);
    }

    @Test
    public void testProbability_ValidParameters() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals("Probability for x = 0", 0.0821, dist.probability(0), 0.001);
        assertEquals("Probability for x = 2", 0.3135, dist.probability(2), 0.001);
        assertEquals("Probability for x = 5", 0.1138, dist.probability(5), 0.001);
    }

    @Test
    public void testCumulativeProbability_ValidParameters() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals("Cumulative probability for x = 0", 0.0000, dist.cumulativeProbability(0), 0.001);
        assertEquals("Cumulative probability for x = 5", 0.7796, dist.cumulativeProbability(5), 0.001);
        assertEquals("Cumulative probability for x = 10", 0.9974, dist.cumulativeProbability(10), 0.001);
    }

    @Test
    public void testNormalApproximateProbability_ValidParameters() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        assertEquals("Normal approximation probability for x = 0", 0.0067, dist.normalApproximateProbability(0), 0.001);
        assertEquals("Normal approximation probability for x = 5", 0.7821, dist.normalApproximateProbability(5), 0.001);
        assertEquals("Normal approximation probability for x = 10", 0.9934, dist.normalApproximateProbability(10), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidParameters() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testSample_SmallMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.0);
        int sample = dist.sample();
        assertEquals("Sample should be less than or equal to 1000 * mean", 4, sample);
    }

    @Test
    public void testSample_LargeMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(50.0);
        int sample = dist.sample();
        assertEquals("Sample should be less than or equal to Integer.MAX_VALUE", 63, sample);
    }

    @Test
    public void testCumulativeProbability_EdgeCases() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals("Cumulative probability for x = 0 should be close to 0", 0.0000, dist.cumulativeProbability(0), 0.01);
        assertEquals("Cumulative probability for x = Integer.MAX_VALUE should be close to 1", 0.9999, dist.cumulativeProbability(Integer.MAX_VALUE), 0.01);
    }
}
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.stat.Stats;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link PoissonDistributionImpl} class.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid argument.
     */
    @Test
    public void testConstruction() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
    }

    /**
     * Test constructor with negative argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    /**
     * Test probability method with valid arguments.
     */
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double probability = distribution.probability(i);
            double expected = Stats.poissonProbability(2.5, i);
            assertEquals(expected, probability, 0.001);
        }
    }

    /**
     * Test probability method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityInvalidArgument() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        distribution.probability(-1);
    }

    /**
     * Test cumulativeProbability method with valid arguments.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double probability = distribution.cumulativeProbability(i);
            double expected = Stats.poissonCumulativeProbability(2.5, i);
            assertEquals(expected, probability, 0.001);
        }
    }

    /**
     * Test cumulativeProbability method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityInvalidArgument() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        distribution.cumulativeProbability(-1);
    }

    /**
     * Test normalApproximateProbability method with valid arguments.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double probability = distribution.normalApproximateProbability(i);
            double expected = Stats.poissonProbability(2.5, i);
            assertEquals(expected, probability, 0.01);
        }
    }

    /**
     * Test normalApproximateProbability method with large argument.
     */
    @Test
    public void testNormalApproximateProbabilityLargeValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(50.0);
        double probability = distribution.normalApproximateProbability(100);
        double expected = Math.exp(-50.0) * Math.sqrt(2.0 * Math.PI * 100.0) / Math.sqrt(Math.E);
        assertEquals(expected, probability, 0.01);
    }

    /**
     * Test sample method with valid arguments.
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 10; i++) {
            int sample = distribution.sample();
            assertEquals(2.5, distribution.cumulativeProbability(sample), 0.001);
        }
    }

    /**
     * Test sample method with invalid argument.
     */
    @Test(expected = MathException.class)
    public void testSampleInvalidArgument() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        distribution.sample(-1);
    }
}
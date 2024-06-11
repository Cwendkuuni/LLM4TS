import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals("Mean should be set correctly", mean, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        double mean = -1.5;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double probability = poissonDistribution.probability(0);
        assertEquals("Probability of zero should be e^(-mean)", Math.exp(-1.0), probability, 0.001);
    }

    @Test
    public void testProbabilityNonZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(1);
        double expected = FastMath.exp(-2.5) * FastMath.sqrt(2.0 / MathUtils.PI) / FastMath.sqrt(1.0);
        assertEquals("Probability of a non-zero value should match the formula", expected, probability, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNegativeValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.probability(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityMaxValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.probability(Integer.MAX_VALUE);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals("Cumulative probability of zero should be 0", 0.0, cumulativeProbability, 0.001);
    }

    @Test
    public void testCumulativeProbabilityOne() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        double expected = Gamma.regularizedGammaP(1.0, 1.5);
        assertEquals("Cumulative probability of one should match Gamma.regularizedGammaP", expected, cumulativeProbability, 0.001);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
        double expected = 1.0;
        assertEquals("Cumulative probability of max value should be 1", expected, cumulativeProbability, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeValue() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.cumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfBounds() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.cumulativeProbability(Integer.MIN_VALUE);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(0);
        assertEquals("Normal approximation probability of zero should match Poisson distribution probability of zero",
                Math.exp(-1.0), normalApproximateProbability, 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityOne() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        double expected = Gamma.regularizedGammaP(1.5, 1.5) * Math.sqrt(2.0 / MathUtils.PI);
        assertEquals("Normal approximation probability of one should match Poisson distribution probability of one",
                expected, normalApproximateProbability, 0.01);
    }

    @Test
    public void testNormalApproximateProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(Integer.MAX_VALUE);
        double expected = 1.0;
        assertEquals("Normal approximation probability of max value should be 1", expected, normalApproximateProbability, 0.001);
    }

    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityNegativeValue() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.normalApproximateProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityOutOfBounds() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.normalApproximateProbability(Integer.MIN_VALUE);
    }

    @Test
    public void testSample() throws MathException {
        Random random = new Random();
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        int sample = poissonDistribution.sample();
        int expected = random.nextInt(1000 * 1.5) + 1;
        assertTrue("Sample should be less than 1000 * mean", expected < 1000 * 1.5);
        assertEquals("Sample should match expected value", expected, sample);
    }

    @Test(expected = MathException.class)
    public void testSampleOutOfBounds() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.sample(Integer.MIN_VALUE);
    }
}
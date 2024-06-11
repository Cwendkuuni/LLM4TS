import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        double mean = 1.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        assertEquals(mean, dist.getMean(), 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        double mean = -1.5;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbabilityZero() {
        double mean = 0.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        double prob = dist.probability(0);
        assertEquals(Math.exp(-mean), prob, 1e-5);
    }

    @Test
    public void testProbabilityPositive() {
        double mean = 1.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        int x = 3;
        double prob = dist.probability(x);
        double expected = FastMath.exp(-mean) * Gamma.gamma(x + 1) / Gamma.gamma(x + 1 + mean);
        assertEquals(expected, prob, 1e-5);
    }

    @Test
    public void testProbabilityMaxValue() {
        double mean = 1.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        int x = Integer.MAX_VALUE;
        double prob = dist.probability(x);
        assertEquals(0.0, prob, 1e-5);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        double mean = 0.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        double cdf = dist.cumulativeProbability(0);
        assertEquals(0.0, cdf, 1e-5);
    }

    @Test
    public void testCumulativeProbabilityPositive() throws Exception {
        double mean = 1.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        int x = 3;
        double cdf = dist.cumulativeProbability(x);
        double expected = Gamma.regularizedGammaP(x + 1, mean, DEFAULT_EPSILON, DEFAULT_MAX_ITERATIONS);
        assertEquals(expected, cdf, Precision.SAFE_EPSILON);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMaxValue() throws Exception {
        double mean = 1.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        int x = Integer.MAX_VALUE;
        dist.cumulativeProbability(x);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws Exception {
        double mean = 0.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        double prob = dist.normalApproximateProbability(0);
        assertEquals(FastMath.sqrt(2 * Math.PI * mean) * FastMath.exp(-(FastMath.pow(0.5, 2) / (2 * mean))), prob, 1e-5);
    }

    @Test
    public void testNormalApproximateProbabilityPositive() throws Exception {
        double mean = 1.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        int x = 3;
        double prob = dist.normalApproximateProbability(x);
        double expected = 0.5 * (1 + Math.erf((x - mean) / FastMath.sqrt(2 * mean)));
        assertEquals(expected, prob, 1e-5);
    }

    @Test
    public void testSample() throws Exception {
        double mean = 1.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        int sample = dist.sample();
        assertTrue(sample >= 0 && sample <= 1000 * mean);
    }
}
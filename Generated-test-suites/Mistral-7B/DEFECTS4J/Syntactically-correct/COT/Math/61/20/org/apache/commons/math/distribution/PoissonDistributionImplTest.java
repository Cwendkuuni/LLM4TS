import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidMean() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDistribution.getMean());
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithInvalidMean() {
        double mean = -1.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        fail("Expected MathRuntimeException when mean is negative");
    }

    @Test
    public void testProbabilityZero() {
        double mean = 0.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double probability = poissonDistribution.probability(0);
        assertEquals(Math.exp(-mean), probability, 1e-6);
    }

    @Test
    public void testProbabilityPositive() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double probability = poissonDistribution.probability(1);
        double expected = FastMath.exp(-mean) * Math.exp(mean) / Math.sqrt(2 * Math.PI * 1.0);
        assertEquals(expected, probability, 1e-6);
    }

    @Test
    public void testProbabilityNegativeAndMaxValue() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double probabilityNegative = poissonDistribution.probability(-1);
        double probabilityMaxValue = poissonDistribution.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probabilityNegative, 1e-6);
        assertEquals(0.0, probabilityMaxValue, 1e-6);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegative() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        poissonDistribution.cumulativeProbability(-1);
        fail("Expected MathException when x is negative");
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        double mean = 0.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals(0.0, cumulativeProbability, 1e-6);
    }

    @Test
    public void testCumulativeProbabilityPositive() throws MathException {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        double expected = Gamma.regularizedGammaP(1.0, mean);
        assertEquals(expected, cumulativeProbability, 1e-6);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        double mean = 0.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(0);
        double expected = FastMath.erf(FastMath.SQRT12 * mean) / (2 * Math.SQRT12);
        assertEquals(expected, normalApproximateProbability, 1e-6);
    }

    @Test
    public void testNormalApproximateProbabilityPositive() throws MathException {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        double expected = Gamma.regularizedGammaQ(1.0, mean);
        assertEquals(expected, normalApproximateProbability, 1e-6);
    }

    @Test
    public void testSample() throws MathException {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0 && sample <= 1000 * mean);
    }
}
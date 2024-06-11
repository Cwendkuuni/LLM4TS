import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.27066026837698656E-7, poissonDistribution.probability(0), 1e-10);
    }

    @Test
    public void testProbabilityNonZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        int x = 3;
        double expectedProbability = FastMath.exp(-1.0) * Gamma.gamma(x + 1) / Math.pow(FastMath.E, x) / Math.sqrt(2 * Math.PI * x);
        assertEquals(expectedProbability, poissonDistribution.probability(x), 1e-10);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double expectedCumulativeProbability = 0.5445509279887926;
        assertEquals(expectedCumulativeProbability, poissonDistribution.cumulativeProbability(0), 1e-8);
    }

    @Test
    public void testCumulativeProbabilityNonZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        int x = 3;
        double expectedCumulativeProbability = Gamma.regularizedGammaP(x + 1, 1.0, 1e-12, 10000000) / Math.pow(FastMath.E, 1.0);
        assertEquals(expectedCumulativeProbability, poissonDistribution.cumulativeProbability(x), 1e-8);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double expectedNormalApproximateProbability = 0.5445509279887926;
        assertEquals(expectedNormalApproximateProbability, poissonDistribution.normalApproximateProbability(0), 1e-8);
    }

    @Test
    public void testNormalApproximateProbabilityNonZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        int x = 3;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.5, FastMath.sqrt(1.5));
        double expectedNormalApproximateProbability = normalDistribution.cumulativeProbability(x + 0.5);
        assertEquals(expectedNormalApproximateProbability, poissonDistribution.normalApproximateProbability(x), 1e-8);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= 1000 * 1.0);
    }

    @Test
    public void testSampleLargeMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(100.0);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= 100 * 100.0);
    }

    @Test
    public void testInverseCumulativeProbabilitySmallMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double p = 0.9;
        double expectedX = 0;
        assertEquals(expectedX, poissonDistribution.inverseCumulativeProbability(p), 1e-6);
    }

    @Test
    public void testInverseCumulativeProbabilityLargeMean() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(100.0);
        double p = 0.9;
        double expectedX = 99;
        assertEquals(expectedX, poissonDistribution.inverseCumulativeProbability(p), 1e-6);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        poissonDistribution.inverseCumulativeProbability(-1.0);
    }
}
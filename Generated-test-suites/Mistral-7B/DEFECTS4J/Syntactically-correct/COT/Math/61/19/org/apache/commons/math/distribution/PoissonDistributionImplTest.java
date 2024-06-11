import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, distribution.getMean(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        double mean = -1.5;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbability() {
        double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 5; i++) {
            double probability = distribution.probability(i);
            double expectedProbability = Math.exp(-mean) * Math.pow(mean, i) / Math.factorial(i);
            assertEquals(expectedProbability, probability, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 5; i++) {
            double cumulativeProbability = distribution.cumulativeProbability(i);
            double expectedCumulativeProbability = Gamma.regularizedGammaQ(i + 1, mean);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 5; i++) {
            double normalApproximateProbability = distribution.normalApproximateProbability(i);
            double expectedNormalApproximateProbability = new NormalDistributionImpl(mean, FastMath.sqrt(mean)).cumulativeProbability(i + 0.5);
            assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 0.001);
        }
    }

    @Test
    public void testSample() throws Exception {
        double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= 1000 * mean);
        assertEquals(mean, distribution.getMean(), 0.0);
        assertEquals(sample, distribution.cumulativeProbability(sample), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 5; i++) {
            double p = i / (double) (5 + 1);
            int inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
            assertEquals(i, inverseCumulativeProbability);
        }

        for (int i = 6; i <= 10; i++) {
            double p = (i - 5) / (double) (10 - 5 + 1);
            int inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
            assertFalse(i == inverseCumulativeProbability);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithNegativeArgument() throws Exception {
        double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        distribution.cumulativeProbability(-1);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMaxIterations() {
        double mean = 2.5;
        new PoissonDistributionImpl(mean, -1);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeEpsilon() {
        double mean = 2.5;
        new PoissonDistributionImpl(mean, -0.1);
    }

    @Test
    public void testGetMean() {
        double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, distribution.getMean(), 0.0);
    }
}
import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;

import java.util.Random;

public class PoissonDistributionImplTest {

    @org.junit.Before
    public void setup() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        this.d = dist;
    }

    private PoissonDistributionImpl d;

    @org.junit.Test
    public void testProbability() {
        for (int i = 0; i <= 10; i++) {
            double prob = d.probability(i);
            double approx = Math.exp(-d.getMean()) / Gamma.gamma(i + 1) / Math.sqrt(Math.PI * i);
            assertEquals("Probability for i = " + i, approx, prob, 1e-6);
        }
    }

    @org.junit.Test
    public void testCumulativeProbability() throws Exception {
        for (int i = 0; i <= 10; i++) {
            double cumProb = d.cumulativeProbability(i);
            double approx = Gamma.regularizedGammaP(i + 1, d.getMean());
            assertEquals("Cumulative Probability for i = " + i, approx, cumProb, 1e-6);
        }
    }

    @org.junit.Test
    public void testNormalApproximateProbability() throws Exception {
        for (int i = 0; i <= 10; i++) {
            double prob = d.normalApproximateProbability(i);
            double approx = NormalDistributionImpl.cumulativeProbability(i, d.getMean(), d.getStandardDeviation());
            assertEquals("Normal Approximate Probability for i = " + i, approx, prob, 1e-6);
        }
    }

    @org.junit.Test
    public void testSample() throws Exception {
        Random rnd = new Random();
        PoissonDistributionImpl dist = new PoissonDistributionImpl(5.0);
        for (int i = 0; i < 1000; i++) {
            int sample = dist.sample();
            assertTrue("Sample is non-negative", sample >= 0);
            assertTrue("Sample is within bounds", sample <= 1000 * 5);
        }
    }

    @org.junit.Test
    public void testInverseCumulativeProbability() throws Exception {
        for (double p = 0.01; p <= 1.0; p += 0.01) {
            int x = d.inverseCumulativeProbability(p);
            double cumProb = d.cumulativeProbability(x);
            double approx = p + (1 - p) * ((x + 1) - x);
            assertEquals("Inverse Cumulative Probability for p = " + p, approx, cumProb, 1e-6);
        }
    }

    @org.junit.Test
    public void testCumulativeProbabilityLargeMean() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(10000.0);
        double cumProb = dist.cumulativeProbability(100);
        double approx = Gamma.regularizedGammaP(101, 10000.0);
        assertEquals("Cumulative Probability for large mean", approx, cumProb, 1e-6);
    }

    @org.junit.Test
    public void testNormalApproximateProbabilityLargeMean() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(10000.0);
        double prob = dist.normalApproximateProbability(100);
        double approx = Gamma.regularizedGammaQ(101, 10000.0);
        assertEquals("Normal Approximate Probability for large mean", approx, prob, 1e-6);
    }

    @org.junit.Test
    public void testSampleLargeMean() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(10000.0);
        int sample = dist.sample();
        assertTrue("Sample is non-negative for large mean", sample >= 0);
        assertTrue("Sample is within bounds for large mean", sample <= 10000 * 1000);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        new PoissonDistributionImpl(-5.0);
    }

    @org.junit.Test(expected = MathException.class)
    public void testMathException() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(Double.POSITIVE_INFINITY);
        dist.cumulativeProbability(1);
    }

    @org.junit.Test(expected = MathException.class)
    public void testMathException2() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(Double.NEGATIVE_INFINITY);
        dist.cumulativeProbability(1);
    }
}
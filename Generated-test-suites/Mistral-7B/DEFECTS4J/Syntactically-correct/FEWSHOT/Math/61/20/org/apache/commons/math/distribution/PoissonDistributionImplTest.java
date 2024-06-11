package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.Arrays;
import java.util.List;

public class PoissonDistributionImplTest {
    private final PoissonDistributionImpl pd;
    private final RandomDataGenerator rng;

    public PoissonDistributionImplTest() {
        pd = new PoissonDistributionImpl(2.5);
        rng = new MersenneTwister();
    }

    @Test
    public void testProbability() {
        for (int i = 0; i <= 10; i++) {
            double prob = pd.probability(i);
            double np = pd.normalApproximateProbability(i);
            double eps = 1e-6;
            assertEquals(np, prob, eps);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        for (int i = 0; i <= 10; i++) {
            double cp = pd.cumulativeProbability(i);
            double np = pd.normalApproximateProbability(i);

            // Check that the normal approximation is monotonically increasing
            assertTrue(np <= pd.cumulativeProbability(i + 1));

            // Check the limit as x approaches Integer.MAX_VALUE
            if (i == 10) {
                assertEquals(1.0, cp, 1e-6);
            }
        }

        // Test negative and out-of-range inputs
        assertFalse(pd.cumulativeProbability(-1) > 0);
        assertNull(pd.cumulativeProbability(Integer.MAX_VALUE + 1));
    }

    @Test
    public void testDistributionFunction() throws MathException {
        int expected[] = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        List<Double> inputs = Arrays.asList(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9);

        for (int i = 0; i < inputs.size(); i++) {
            int result = pd.inverseCumulativeProbability(inputs.get(i));
            assertEquals(expected[i], result);
        }

        // Test negative and out-of-range inputs
        assertNull(pd.inverseCumulativeProbability(-1.0));
        assertNull(pd.inverseCumulativeProbability(1.1));
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution pdInstance = new PoissonDistribution(2.5);
        int expectedSum = 0;

        for (int i = 0; i < 1000; i++) {
            expectedSum += pdInstance.sample();
        }

        assertEquals(pd.getMean() * 1000, expectedSum);
    }

    @Test
    public void testMeanAndVariance() {
        double mean = pd.getMean();
        double variance = pd.getVariance();

        assertEquals(mean, pd.expectedValue(), 1e-6);
        assertEquals(variance, pd.variance(), 1e-6);
    }

    @Test
    public void testMaxIterationsAndEpsilon() {
        PoissonDistributionImpl pd1 = new PoissonDistributionImpl(5.0, 1e-12, 100000);
        PoissonDistributionImpl pd2 = new PoissonDistributionImpl(5.0, 1e-12);

        assertEquals(100000, pd1.getMaxIterations());
        assertEquals(1e-12, pd1.getEpsilon(), 1e-12);

        assertEquals(-1, pd2.getMaxIterations());
        assertEquals(1e-12, pd2.getEpsilon(), 1e-12);
    }

    @Test(expected = MathRuntimeException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-2.5);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        for (int i = 0; i <= 10; i++) {
            double prob = pd.probability(i);
            double np = pd.normalApproximateProbability(i);
            double eps = 1e-6;
            assertEquals(np, prob, eps);
        }
    }

    @Test
    public void testSampleUsingPoissonProcess() throws MathException {
        PoissonDistribution pdInstance = new PoissonDistribution(0.5);
        int sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum += pdInstance.sample();
        }

        assertEquals(0.5 * 10000, sum);
    }

    @Test
    public void testSampleUsingRejectionAlgorithm() throws MathException {
        PoissonDistribution pdInstance = new PoissonDistribution(100.0);
        int result = pdInstance.sample();

        // A large mean is expected to result in a large value
        assertTrue(result > 50);

        // Verify that the generated value is within the bounds
        assertTrue(result <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, pd.getDomainLowerBound(0.8));
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, pd.getDomainUpperBound(0.8));
    }

    @Test
    public void testProbabilityForLargeMean() throws MathException {
        PoissonDistributionImpl pdLargeMean = new PoissonDistributionImpl(100.0, 1e-12, 100000);
        double prob = pdLargeMean.probability(50);

        // Check that the generated value is within the bounds
        assertTrue(prob >= 0);
        assertTrue(prob <= 1);
    }

    @Test
    public void testCumulativeProbabilityForLargeMean() throws MathException {
        PoissonDistributionImpl pdLargeMean = new PoissonDistributionImpl(100.0, 1e-12, 1e6);
        double cp = pdLargeMean.cumulativeProbability(50);

        // Check that the generated value is close to the expected value
        double expectedCp = Gamma.regularizedGammaP(50.5, 100.0);
        assertTrue(Precision.closeTo(expectedCp, cp, 1e-6));
    }
}
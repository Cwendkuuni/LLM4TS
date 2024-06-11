package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionCreation() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, pd.getMean(), 0.0);
        PoissonDistributionImpl pdZero = new PoissonDistributionImpl(0.0);
        assertEquals(0.0, pdZero.getMean(), 0.0);
        PoissonDistributionImpl pdNegative = new PoissonDistributionImpl(-1.5);
        assertFalse(pdNegative instanceof PoissonDistributionImpl);
        try {
            new PoissonDistributionImpl(-0.5);
            assertFalse(true); // should throw exception
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.5);
        assertEquals(0.222989586465, pd.probability(0), 1e-6);
        assertEquals(0.223141283155, pd.probability(1), 1e-6);
        assertEquals(0.198977136372, pd.probability(2), 1e-6);
        assertEquals(0.171176494866, pd.probability(3), 1e-6);
        assertEquals(0.146912884131, pd.probability(4), 1e-6);
        assertEquals(0.123427141333, pd.probability(5), 1e-6);
        assertEquals(0.099336668871, pd.probability(6), 1e-6);

        PoissonDistributionImpl pdZero = new PoissonDistributionImpl(0.0);
        assertEquals(1.0, pdZero.probability(0), 1e-6);

        PoissonDistributionImpl pdMaxInt = new PoissonDistributionImpl(Integer.MAX_VALUE);
        assertEquals(0.0, pdMaxInt.probability(Integer.MAX_VALUE), 1e-6);
        assertEquals(1.0, pdMaxInt.probability(Integer.MAX_VALUE - 1), 1e-6);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.5);
        assertEquals(0.0, pd.cumulativeProbability(-1), 1e-6);
        assertEquals(0.222989586465, pd.cumulativeProbability(0), 1e-6);
        assertEquals(0.446560391234, pd.cumulativeProbability(1), 1e-6);
        assertEquals(0.669494861345, pd.cumulativeProbability(2), 1e-6);
        assertEquals(0.892233879591, pd.cumulativeProbability(3), 1e-6);
        assertEquals(0.985584685836, pd.cumulativeProbability(4), 1e-6);
        assertEquals(0.999753854776, pd.cumulativeProbability(5), 1e-6);
        assertEquals(1.0, pd.cumulativeProbability(6), 1e-6);

        PoissonDistributionImpl pdZero = new PoissonDistributionImpl(0.0);
        assertEquals(1.0, pdZero.cumulativeProbability(Integer.MAX_VALUE), 1e-6);

        PoissonDistributionImpl pdMaxInt = new PoissonDistributionImpl(Integer.MAX_VALUE);
        assertEquals(1.0, pdMaxInt.cumulativeProbability(Integer.MAX_VALUE), 1e-6);
        assertEquals(1.0, pdMaxInt.cumulativeProbability(Integer.MAX_VALUE - 1), 1e-6);

        // test with different epsilon and maxIterations
        PoissonDistributionImpl pdCustom = new PoissonDistributionImpl(1.5, 0.01, 1000000);
        assertEquals(0.446560391234, pdCustom.cumulativeProbability(1), 1e-7);
        assertEquals(0.669494861345, pdCustom.cumulativeProbability(2), 1e-7);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.5);
        assertEquals(0.222989586465, pd.probability(0), 1e-6);
        assertEquals(0.223141283155, pd.probability(1), 1e-6);
        assertEquals(0.198977136372, pd.probability(2), 1e-6);
        assertEquals(0.171176494866, pd.probability(3), 1e-6);
        assertEquals(0.146912884131, pd.probability(4), 1e-6);
        assertEquals(0.123427141333, pd.probability(5), 1e-6);
        assertEquals(0.099336668871, pd.probability(6), 1e-6);

        PoissonDistributionImpl pdNormalApprox = new PoissonDistributionImpl(1.5, 1e-12, 100000);
        double[] expected = {0.223141283155, 0.221665509118, 0.219615475335, 0.217045851693, 0.214020587142, 0.210533537593, 0.206610135726};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], pdNormalApprox.normalApproximateProbability(i), 1e-6);
        }
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.5);
        for (int i = 0; i < 100; i++) {
            int result = pd.sample();
            assertTrue(result >= 0);
            assertTrue(result <= 1000 * pd.getMean());
        }

        PoissonDistributionImpl pdLargeMean = new PoissonDistributionImpl(1000.0);
        for (int i = 0; i < 100; i++) {
            int result = pdLargeMean.sample();
            assertTrue(result >= 0);
            assertTrue(result <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testCumulativeProbabilityWithLargeValues() throws MathException, NumberIsTooSmallException, MaxIterationsExceededException {
        double mean = 1000.0;
        PoissonDistributionImpl pd = new PoissonDistributionImpl(mean);

        double p = 0.99;
        int expected = (int) Math.floor(pd.cumulativeProbability(Integer.MAX_VALUE - 1) * (1.0 + 1.0 / Math.pow(2, 32)));
        int result = pd.inverseCumulativeProbability(p);
        assertEquals(expected, result);

        p = 0.5;
        result = pd.inverseCumulativeProbability(p);
        assertEquals(mean * result, pd.sample(), 1);
    }
}
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Test;

/**
 * Test for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
        PoissonDistributionImpl distribution2 = new PoissonDistributionImpl(2.0, 0.1, 1000000);
        assertEquals(2.0, distribution2.getMean(), 0.0);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorInvalidMean() {
        new PoissonDistributionImpl(-1.0);
    }

    /**
     * Test probability method with typical use case.
     */
    @Test
    public void testProbabilityTypicalUseCase() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.24009854, distribution.probability(0), 0.000001);
        assertEquals(0.36787944, distribution.probability(1), 0.000001);
        assertEquals(0.17317793, distribution.probability(2), 0.000001);
    }

    /**
     * Test probability method with edge cases.
     */
    @Test
    public void testProbabilityEdgeCases() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.0, distribution.probability(-1), 0.0);
        assertEquals(0.0, distribution.probability(Integer.MAX_VALUE), 0.0);
    }

    /**
     * Test probability method with error scenarios.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testProbabilityErrorScenarios() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        distribution.probability((int) Double.NaN);
    }

    /**
     * Test cumulativeProbability method with typical use case.
     */
    @Test
    public void testCumulativeProbabilityTypicalUseCase() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.36787944, distribution.cumulativeProbability(1), 0.000001);
        assertEquals(0.68275888, distribution.cumulativeProbability(2), 0.000001);
    }

    /**
     * Test cumulativeProbability method with edge cases.
     */
    @Test
    public void testCumulativeProbabilityEdgeCases() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.000001);
    }

    /**
     * Test cumulativeProbability method with error scenarios.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityErrorScenarios() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        distribution.cumulativeProbability((int) Double.NaN);
    }

    /**
     * Test normalApproximateProbability method with typical use case.
     */
    @Test
    public void testNormalApproximateProbabilityTypicalUseCase() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.36787944, distribution.normalApproximateProbability(1), 0.000001);
        assertEquals(0.17317793, distribution.normalApproximateProbability(2), 0.000001);
    }

    /**
     * Test normalApproximateProbability method with edge cases.
     */
    @Test
    public void testNormalApproximateProbabilityEdgeCases() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.5, distribution.normalApproximateProbability(-1), 0.000001);
        assertEquals(0.5, distribution.normalApproximateProbability(Integer.MAX_VALUE), 0.000001);
    }

    /**
     * Test sample method with typical use case.
     */
    @Test
    public void testSampleTypicalUseCase() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        int sample = distribution.sample();
        assertEquals(0, sample, 0);
        assertEquals(1, sample, 0);
        assertEquals(2, sample, 0);
    }

    /**
     * Test sample method with edge cases.
     */
    @Test
    public void testSampleEdgeCases() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        int sample = distribution.sample();
        assertEquals(-1, sample, 0);
        assertEquals(Integer.MAX_VALUE, sample, 0);
    }

    /**
     * Test sample method with error scenarios.
     */
    @Test(expected = MathException.class)
    public void testSampleErrorScenarios() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        distribution.sample((int) Double.NaN);
    }

    /**
     * Test inverseCumulativeProbability method with typical use case.
     */
    @Test
    public void testInverseCumulativeProbabilityTypicalUseCase() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double p = 0.36787944;
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(1, x, 0);

        p = 0.68275888;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(2, x, 0);
    }

    /**
     * Test inverseCumulativeProbability method with edge cases.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double p = 0.0;
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(0, x, 0);

        p = 1.0;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(Integer.MAX_VALUE, x, 0);
    }

    /**
     * Test inverseCumulativeProbability method with error scenarios.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInverseCumulativeProbabilityErrorScenarios() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double p = 1.5;
        distribution.inverseCumulativeProbability(p);
    }

    /**
     * Test moment calculation.
     */
    @Test
    public void testMoments() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(1.0, new StandardDeviation().evaluate(distribution), 0.0);
    }
}
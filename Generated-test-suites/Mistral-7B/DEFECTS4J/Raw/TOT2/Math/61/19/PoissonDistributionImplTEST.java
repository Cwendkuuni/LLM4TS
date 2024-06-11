package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

/**
 * Test cases for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid parameters.
     * @throws MathException if an error occurs initializing the distribution
     */
    @Test
    public void testPoissonDistributionImplValid() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals("Mean should be set correctly.", 2.5, poissonDist.getMean(), 0.001);
    }

    /**
     * Test constructor with invalid parameters.
     */
    @Test(expected = MathRuntimeException.class)
    public void testPoissonDistributionImplInvalid() {
        new PoissonDistributionImpl(-1);
    }

    /**
     * Test probability method with typical use case.
     * @throws MathException if an error occurs calculating the probability
     */
    @Test
    public void testProbabilityTypicalUseCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals("Probability of x = 1 should be close to 0.1353352832366127.", 0.1353, poissonDist.probability(1), 0.001);
    }

    /**
     * Test probability method with edge case.
     * @throws MathException if an error occurs calculating the probability
     */
    @Test
    public void testProbabilityEdgeCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals("Probability of x = 0 should be close to 0.02271287351523462.", 0.0227, poissonDist.probability(0), 0.001);
    }

    /**
     * Test probability method with error scenario.
     * @throws MathException if an error occurs calculating the probability
     */
    @Test
    public void testProbabilityErrorScenario() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals("Probability of x = Integer.MAX_VALUE should be 0.", 0.0, poissonDist.probability(Integer.MAX_VALUE), 0.0);
    }

    /**
     * Test cumulativeProbability method with typical use case.
     * @throws MathException if an error occurs calculating the cumulative probability
     */
    @Test
    public void testCumulativeProbabilityTypicalUseCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        double cumProb = poissonDist.cumulativeProbability(1);
        assertEquals("Cumulative probability of x = 1 should be close to 0.3679027049577667.", 0.3679, cumProb, 0.001);
    }

    /**
     * Test cumulativeProbability method with edge cases.
     * @throws MathException if an error occurs calculating the cumulative probability
     */
    @Test
    public void testCumulativeProbabilityEdgeCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        double cumProb = poissonDist.cumulativeProbability(0);
        assertEquals("Cumulative probability of x = 0 should be close to 0.1353352832366127.", 0.1353, cumProb, 0.001);

        cumProb = poissonDist.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals("Cumulative probability of x = Integer.MAX_VALUE should be close to 1.", 1.0, cumProb, 0.01);
    }

    /**
     * Test cumulativeProbability method with error scenarios.
     * @throws MathException if an error occurs calculating the cumulative probability
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityErrorScenario() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        poissonDist.cumulativeProbability(-1);
    }

    /**
     * Test normalApproximateProbability method with typical use case.
     * @throws MathException if an error occurs calculating the normal approximation probability
     */
    @Test
    public void testNormalApproximateProbabilityTypicalUseCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        double normProb = poissonDist.normalApproximateProbability(1);
        assertEquals("Normal approximation probability of x = 1 should be close to 0.1353352832366127.", 0.1353, normProb, 0.001);
    }

    /**
     * Test normalApproximateProbability method with edge cases.
     * @throws MathException if an error occurs calculating the normal approximation probability
     */
    @Test
    public void testNormalApproximateProbabilityEdgeCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        double normProb = poissonDist.normalApproximateProbability(0);
        assertEquals("Normal approximation probability of x = 0 should be close to 0.1353352832366127.", 0.1353, normProb, 0.001);

        normProb = poissonDist.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals("Normal approximation probability of x = Integer.MAX_VALUE should be close to 0.", 0.0, normProb, 0.01);
    }

    /**
     * Test sample method with typical use case.
     * @throws MathException if an error occurs generating the random value
     */
    @Test
    public void testSampleTypicalUseCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        int sample = poissonDist.sample();
        assertEquals("Sample value should be positive and less than 1000 * mean.", sample, Math.round(2.5 * 1000), 0);
    }

    /**
     * Test sample method with large mean.
     * @throws MathException if an error occurs generating the random value
     */
    @Test
    public void testSampleLargeMean() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(500.0);
        int sample = poissonDist.sample();
        assertTrue("Sample value should be positive.", sample > 0);
        assertTrue("Sample value should be less than 1000 * mean.", sample < 500000);
    }

    /**
     * Test sample method with error scenarios.
     * @throws MathException if an error occurs generating the random value
     */
    @Test
    public void testSampleErrorScenario() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        poissonDist.sample(); // call sample method to initialize the random number generator

        try {
            poissonDist.sample(); // should throw MathException
            fail();
        } catch (MathException e) {
            // expected
        }
    }
}
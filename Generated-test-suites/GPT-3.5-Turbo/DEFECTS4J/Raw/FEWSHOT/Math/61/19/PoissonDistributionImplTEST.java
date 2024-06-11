import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private static final double EPSILON = 1e-9;
    private static final double DEFAULT_EPSILON = 1e-12;
    private static final int MAX_ITERATIONS = 10000000;
    private static final double DEFAULT_MAX_ITERATIONS = 0.0;

    private PoissonDistributionImpl distribution;

    @Before
    public void setup() {
        distribution = new PoissonDistributionImpl(3.5);
    }

    @Test
    public void testConstructor() {
        assertEquals(3.5, distribution.getMean(), EPSILON);
        assertEquals(DEFAULT_EPSILON, distribution.getEpsilon(), EPSILON);
        assertEquals(MAX_ITERATIONS, distribution.getMaxIterations());
    }

    @Test
    public void testConstructorWithEpsilon() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5, 1e-6);
        assertEquals(3.5, distribution.getMean(), EPSILON);
        assertEquals(1e-6, distribution.getEpsilon(), EPSILON);
        assertEquals(MAX_ITERATIONS, distribution.getMaxIterations());
    }

    @Test
    public void testConstructorWithMaxIterations() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5, 100);
        assertEquals(3.5, distribution.getMean(), EPSILON);
        assertEquals(DEFAULT_EPSILON, distribution.getEpsilon(), EPSILON);
        assertEquals(100, distribution.getMaxIterations());
    }

    @Test
    public void testConstructorWithEpsilonAndMaxIterations() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5, 1e-6, 100);
        assertEquals(3.5, distribution.getMean(), EPSILON);
        assertEquals(1e-6, distribution.getEpsilon(), EPSILON);
        assertEquals(100, distribution.getMaxIterations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithZeroMean() {
        new PoissonDistributionImpl(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-3.5);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(-1), EPSILON);
        assertEquals(0.0, distribution.probability(0), EPSILON);
        assertEquals(0.024622488860201965, distribution.probability(1), EPSILON);
        assertEquals(0.09022352215774179, distribution.probability(2), EPSILON);
        assertEquals(0.20781031644647156, distribution.probability(3), EPSILON);
        assertEquals(0.32465246735834974, distribution.probability(4), EPSILON);
        assertEquals(0.3554997654523986, distribution.probability(5), EPSILON);
        assertEquals(0.3189187890695454, distribution.probability(6), EPSILON);
        assertEquals(0.230847540354803, distribution.probability(7), EPSILON);
        assertEquals(0.13349328526144418, distribution.probability(8), EPSILON);
        assertEquals(0.06178428068259657, distribution.probability(9), EPSILON);
        assertEquals(0.022530463741177106, distribution.probability(10), EPSILON);
        
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0, distribution.cumulativeProbability(-1), EPSILON);
        assertEquals(0.024622488860201965, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.11584501101894364, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(0.32365532746541543, distribution.cumulativeProbability(2), EPSILON);
        assertEquals(0.5314656439118871, distribution.cumulativeProbability(3), EPSILON);
        assertEquals(0.8841181112702369, distribution.cumulativeProbability(4), EPSILON);
        assertEquals(0.5701307203294077, distribution.cumulativeProbability(5), EPSILON);
        assertEquals(0.15354809025978603, distribution.cumulativeProbability(6), EPSILON);
        assertEquals(0.040700723904212186, distribution.cumulativeProbability(7), EPSILON);
        assertEquals(0.009131119642768581, distribution.cumulativeProbability(8), EPSILON);
        assertEquals(0.0013498118661724138, distribution.cumulativeProbability(9), EPSILON);
        assertEquals(0.0001953486599741537, distribution.cumulativeProbability(10), EPSILON);
       
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.10564977366685535, distribution.normalApproximateProbability(-1), EPSILON);
        assertEquals(0.16610517724291639, distribution.normalApproximateProbability(0), EPSILON);
        assertEquals(0.264427682019785, distribution.normalApproximateProbability(1), EPSILON);
        assertEquals(0.41764621020952723, distribution.normalApproximateProbability(2), EPSILON);
        assertEquals(0.683640253220066, distribution.normalApproximateProbability(3), EPSILON);
        assertEquals(0.24734261636316417, distribution.normalApproximateProbability(4), EPSILON);
        assertEquals(0.07654710424668151, distribution.normalApproximateProbability(5), EPSILON);
        assertEquals(0.02324953818436578, distribution.normalApproximateProbability(6), EPSILON);
        assertEquals(0.0070433036366177605, distribution.normalApproximateProbability(7), EPSILON);
        assertEquals(0.0021298229474274028, distribution.normalApproximateProbability(8), EPSILON);
        assertEquals(0.0006437191714399572, distribution.normalApproximateProbability(9), EPSILON);
        assertEquals(0.0001953486599741537, distribution.normalApproximateProbability(10), EPSILON);
        
    }

    @Test
    public void testSample() throws MathException {
        int value = distribution.sample();
        assertTrue(value >= 0);
        assertTrue(value <= Integer.MAX_VALUE);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(-1, distribution.inverseCumulativeProbability(0.0), EPSILON);
        assertEquals(0, distribution.inverseCumulativeProbability(0.024622488860201965), EPSILON);
        assertEquals(1, distribution.inverseCumulativeProbability(0.11584501101894364), EPSILON);
        assertEquals(2, distribution.inverseCumulativeProbability(0.32365532746541543), EPSILON);
        assertEquals(3, distribution.inverseCumulativeProbability(0.5314656439118871), EPSILON);
        assertEquals(4, distribution.inverseCumulativeProbability(0.8841181112702369), EPSILON);
        assertEquals(5, distribution.inverseCumulativeProbability(0.5701307203294077), EPSILON);
        assertEquals(6, distribution.inverseCumulativeProbability(0.15354809025978603), EPSILON);
        assertEquals(7, distribution.inverseCumulativeProbability(0.040700723904212186), EPSILON);
        assertEquals(8, distribution.inverseCumulativeProbability(0.009131119642768581), EPSILON);
        assertEquals(9, distribution.inverseCumulativeProbability(0.0013498118661724138), EPSILON);
        assertEquals(10, distribution.inverseCumulativeProbability(0.0001953486599741537), EPSILON);
        
    }

}
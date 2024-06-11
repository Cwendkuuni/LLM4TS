package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;

/**
 * Test for the {@link FDistribution} class.
 */
public class FDistributionTest extends AbstractRealDistributionTest {

    /**
     * Test default constructor.
     * @throws NotStrictlyPositiveException if parameters are not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testDefaultConstructorNegativeParameters() throws NotStrictlyPositiveException {
        new FDistribution(-1.0, -1.0);
    }

    /**
     * Test constructor with positive parameters.
     */
    @Test
    public void testConstructorsPositiveParameters() {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution dist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals("getNumeratorDegreesOfFreedom incorrect.", numeratorDegreesOfFreedom, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals("getDenominatorDegreesOfFreedom incorrect.", denominatorDegreesOfFreedom, dist.getDenominatorDegreesOfFreedom(), 0.0);

        // test density
        assertEquals("density at 0.0 incorrect.", 0.0, dist.density(0.0), 1e-15);
        assertEquals("density at 1.0 incorrect.", 0.12891694034783552, dist.density(1.0), 1e-15);
        assertEquals("density at 5.0 incorrect.", 0.0027611475213861597, dist.density(5.0), 1e-15);

        // test cumulative probability
        assertEquals("cumulative probability at 0.0 incorrect.", 0.0, dist.cumulativeProbability(0.0), 1e-15);
        assertEquals("cumulative probability at 1.0 incorrect.", 0.30689077184638611, dist.cumulativeProbability(1.0), 1e-15);
        assertEquals("cumulative probability at 5.0 incorrect.", 0.99999999999999938, dist.cumulativeProbability(5.0), 1e-15);

        // test inverse cumulative probability
        assertEquals("inverse cumulative probability at 0.0 incorrect.", Double.NaN, dist.inverseCumulativeProbability(0.0), 1e-15);
        assertEquals("inverse cumulative probability at 0.3 incorrect.", 1.3123938111236677, dist.inverseCumulativeProbability(0.3), 1e-15);
        assertEquals("inverse cumulative probability at 0.99 incorrect.", 5.332858903313597, dist.inverseCumulativeProbability(0.99), 1e-15);

        // test mean
        assertEquals("numerical mean incorrect.", 1.5, dist.getNumericalMean(), 1e-15);

        // test variance
        assertEquals("numerical variance incorrect.", 2.482007132173695, dist.getNumericalVariance(), 1e-15);

        // test getSolverAbsoluteAccuracy
        assertEquals("solver absolute accuracy incorrect.", FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 1e-15);
    }

    /**
     * Test constructor with random number generator.
     * @throws NotStrictlyPositiveException if parameters are not positive.
     */
    @Test
    public void testConstructorRandomGeneratorPositiveParameters() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final Well19937c rng = new Well19937c();
        final FDistribution dist = new FDistribution(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals("getNumeratorDegreesOfFreedom incorrect.", numeratorDegreesOfFreedom, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals("getDenominatorDegreesOfFreedom incorrect.", denominatorDegreesOfFreedom, dist.getDenominatorDegreesOfFreedom(), 0.0);

        // test density
        assertEquals("density at 0.0 incorrect.", 0.0, dist.density(0.0), 1e-15);
        assertEquals("density at 1.0 incorrect.", 0.12891694034783552, dist.density(1.0), 1e-15);
        assertEquals("density at 5.0 incorrect.", 0.0027611475213861597, dist.density(5.0), 1e-15);

        // test cumulative probability
        assertEquals("cumulative probability at 0.0 incorrect.", 0.0, dist.cumulativeProbability(0.0), 1e-15);
        assertEquals("cumulative probability at 1.0 incorrect.", 0.30689077184638611, dist.cumulativeProbability(1.0), 1e-15);
        assertEquals("cumulative probability at 5.0 incorrect.", 0.99999999999999938, dist.cumulativeProbability(5.0), 1e-15);

        // test inverse cumulative probability
        assertEquals("inverse cumulative probability at 0.0 incorrect.", Double.NaN, dist.inverseCumulativeProbability(0.0), 1e-15);
        assertEquals("inverse cumulative probability at 0.3 incorrect.", 1.3123938111236677, dist.inverseCumulativeProbability(0.3), 1e-15);
        assertEquals("inverse cumulative probability at 0.99 incorrect.", 5.332858903313597, dist.inverseCumulativeProbability(0.99), 1e-15);

        // test mean
        assertEquals("numerical mean incorrect.", 1.5, dist.getNumericalMean(), 1e-15);

        // test variance
        assertEquals("numerical variance incorrect.", 2.482007132173695, dist.getNumericalVariance(), 1e-15);
    }
}
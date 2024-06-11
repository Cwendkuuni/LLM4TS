package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.point.PointValuePairComparator;
import org.junit.Test;

/**
 * Test cases for MultiDirectional class.
 */
public class MultiDirectionalTest {

    /**
     * Test default constructor.
     * @throws Exception if an error occurs during test
     */
    @Test
    public void testDefaultConstructor() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.khi, 0.001);
        assertEquals(0.5, optimizer.gamma, 0.001);
    }

    /**
     * Test constructor with specified coefficients.
     * @throws Exception if an error occurs during test
     */
    @Test
    public void testConstructorWithCoefficients() throws Exception {
        final double khi = 3.14;
        final double gamma = 0.25;
        final MultiDirectional optimizer = new MultiDirectional(khi, gamma);
        assertEquals(khi, optimizer.khi, 0.001);
        assertEquals(gamma, optimizer.gamma, 0.001);
    }

    /**
     * Test iterateSimplex method with default coefficients.
     * @throws Exception if an error occurs during test
     */
    @Test
    public void testIterateSimplexDefaultCoefficients() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] simplex = new RealPointValuePair[6];
        optimizer.iterateSimplex(new PointValuePairComparator<RealPointValuePair>());
    }

    /**
     * Test iterateSimplex method with specified coefficients.
     * @throws Exception if an error occurs during test
     */
    @Test
    public void testIterateSimplexSpecifiedCoefficients() throws Exception {
        final double khi = 3.14;
        final double gamma = 0.25;
        final MultiDirectional optimizer = new MultiDirectional(khi, gamma);
        final RealPointValuePair[] simplex = new RealPointValuePair[6];
        optimizer.iterateSimplex(new PointValuePairComparator<RealPointValuePair>());
    }

    /**
     * Test evaluateNewSimplex method.
     * @throws Exception if an error occurs during test
     */
    @Test
    public void testEvaluateNewSimplex() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false);
        original[1] = new RealPointValuePair(new double[]{3.0, 3.0}, 5.0, false);
        original[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);

        final RealPointValuePair result = optimizer.evaluateNewSimplex(original, 2.0, new PointValuePairComparator<RealPointValuePair>());
        assertNotNull(result);
    }

    /**
     * Test evaluateNewSimplex method with reflection.
     * @throws Exception if an error occurs during test
     */
    @Test
    public void testEvaluateNewSimplexReflection() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false);
        original[1] = new RealPointValuePair(new double[]{3.0, 3.0}, 5.0, false);
        original[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);

        final RealPointValuePair[] reflected = optimizer.evaluateNewSimplex(original, 1.0, new PointValuePairComparator<RealPointValuePair>());
        assertNotNull(reflected);
        assertArrayEquals(original[1].getPointRef(), reflected[0].getPointRef(), 0.001);
    }

    /**
     * Test evaluateNewSimplex method with expansion.
     * @throws Exception if an error occurs during test
     */
    @Test
    public void testEvaluateNewSimplexExpansion() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false);
        original[1] = new RealPointValuePair(new double[]{3.0, 3.0}, 5.0, false);
        original[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);

        final RealPointValuePair[] expanded = optimizer.evaluateNewSimplex(original, 2.0, new PointValuePairComparator<RealPointValuePair>());
        assertNotNull(expanded);
        assertArrayEquals(original[2].getPointRef(), expanded[0].getPointRef(), 0.001);
    }

    /**
     * Test evaluateNewSimplex method with contraction.
     * @throws Exception if an error occurs during test
     */
    @Test
    public void testEvaluateNewSimplexContraction() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false);
        original[1] = new RealPointValuePair(new double[]{3.0, 3.0}, 5.0, false);
        original[2] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);

        final RealPointValuePair contracted = optimizer.evaluateNewSimplex(original, 0.5, new PointValuePairComparator<RealPointValuePair>());
        assertNotNull(contracted);
        assertNull(contracted.getValue());
    }

    /**
     * Test convergence.
     * @throws Exception if an error occurs during test
     */
    @Test
    public void testConvergence() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] simplex = new RealPointValuePair[6];
        optimizer.iterateSimplex(new PointValuePairComparator<RealPointValuePair>());
        assertNotNull(simplex[5]);
        assertEquals(Double.POSITIVE_INFINITY, simplex[5].getValue(), 0.001);
    }

}
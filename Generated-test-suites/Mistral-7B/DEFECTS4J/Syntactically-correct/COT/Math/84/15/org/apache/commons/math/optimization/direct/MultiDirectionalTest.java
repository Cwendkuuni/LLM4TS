package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.direct.MultiDirectional;

/**
 * JUnit test file for MultiDirectional class.
 */
public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }

    /**
     * Test constructor without arguments.
     */
    @Test
    public void testDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.khi, 0.001);
        assertEquals(0.5, optimizer.gamma, 0.001);
    }

    /**
     * Test constructor with arguments.
     */
    @Test
    public void testConstructorWithArguments() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.6);
        assertEquals(3.0, optimizer.khi, 0.001);
        assertEquals(0.6, optimizer.gamma, 0.001);
    }

    /**
     * Test method iterateSimplex when simplex contains the minimal point.
     * @throws Exception when an exception is thrown
     */
    @Test
    public void testIterateSimplexMinimalPoint() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(new double[]{0.0, 0.0}, new double[]{1.0, 1.0});
        optimizer.optimize(new FooFunction(), bounds, null);
        PointValuePair bestSolution = optimizer.getBestSolution();
        double bestValue = bestSolution.getValue();
        double[] bestPoint = bestSolution.getPoint();
        assertArrayEquals(new double[]{0.5, 0.5}, bestPoint, 0.001);
        assertEquals(0.0, bestValue, 0.001);
    }

    /**
     * Test method iterateSimplex when simplex is not bracketing the minimal point.
     * @throws Exception when an exception is thrown
     */
    @Test
    public void testIterateSimplexNotBracketingMinimalPoint() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(new double[]{0.0, 0.0}, new double[]{2.0, 2.0});
        optimizer.optimize(new FooFunction(), bounds, null);
        PointValuePair bestSolution = optimizer.getBestSolution();
        double bestValue = bestSolution.getValue();
        double[] bestPoint = bestSolution.getPoint();
        assertArrayEquals(new double[]{1.0, 1.0}, bestPoint, 0.001);
        assertEquals(Math.PI, bestValue, 0.001);
        assertThrows(NoBracketingException.class, () -> optimizer.getMinimum());
    }

    /**
     * Test method iterateSimplex when maximal number of evaluations is exceeded.
     * @throws Exception when an exception is thrown
     */
    @Test
    public void testIterateSimplexMaxEvalExceeded() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(new double[]{-10.0, -10.0}, new double[]{10.0, 10.0});
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(new FooFunction(), bounds, null);
        assertThrows(MaxEvalExceededException.class, () -> optimizer.getBestSolution());
    }

    /**
     * Test method iterateSimplex when initial guess is provided.
     * @throws Exception when an exception is thrown
     */
    @Test
    public void testIterateSimplexInitialGuess() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public double[] getInitialPoint() {
                return new double[]{0.1, 0.1};
            }
        };
        SimpleBounds bounds = new SimpleBounds(new double[]{0.0, 0.0}, new double[]{1.0, 1.0});
        optimizer.optimize(new FooFunction(), bounds, initialGuess);
        PointValuePair bestSolution = optimizer.getBestSolution();
        double bestValue = bestSolution.getValue();
        double[] bestPoint = bestSolution.getPoint();
        assertArrayEquals(new double[]{0.5, 0.5}, bestPoint, 0.001);
        assertEquals(0.0, bestValue, 0.001);
    }

    /**
     * Test method evaluateNewSimplex when simplex is empty.
     * @throws Exception when an exception is thrown
     */
    @Test
    public void testEvaluateNewSimplexEmptySimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] original = new double[2];
        SimpleComparator comparator = new SimpleComparator();
        RealPointValuePair bestPoint = optimizer.evaluateNewSimplex(new RealPointValuePair[0], 1.0, comparator);
        assertNull(bestPoint);
    }

    /**
     * Test method evaluateNewSimplex when simplex contains one point.
     * @throws Exception when an exception is thrown
     */
    @Test
    public void testEvaluateNewSimplexOnePoint() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = new double[]{0.0};
        SimpleComparator comparator = new SimpleComparator();
        RealPointValuePair bestPoint = optimizer.evaluateNewSimplex(new RealPointValuePair[]{new RealPointValuePair(xSmallest, 0.0, false)}, 1.0, comparator);
        assertArrayEquals(new double[]{0.0}, bestPoint.getPointRef(), 0.001);
        assertEquals(Double.NaN, bestPoint.getValue(), 0.001);
    }

    /**
     * Test method evaluateNewSimplex when simplex contains multiple points.
     * @throws Exception when an exception is thrown
     */
    @Test
    public void testEvaluateNewSimplexMultiplePoints() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = new double[]{0.0, 0.0};
        SimpleComparator comparator = new SimpleComparator();
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(xSmallest, 0.0, false);
        original[1] = new RealPointValuePair(new double[]{1.0, 1.0}, 4.0, false);
        RealPointValuePair bestPoint = optimizer.evaluateNewSimplex(original, 1.0, comparator);
        assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, bestPoint.getPointRef(), 0.001);
        assertEquals(Math.sqrt(2.0) / 3.0, bestPoint.getValue(), 0.001);
    }

    /**
     * Test method evaluateNewSimplex when coefficients are zero.
     * @throws Exception when an exception is thrown
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateNewSimplexZeroCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = new double[]{0.0};
        SimpleComparator comparator = new SimpleComparator();
        optimizer.evaluateNewSimplex(new RealPointValuePair[]{new RealPointValuePair(xSmallest, 0.0, false)}, 0.0, comparator);
    }

    /**
     * Test method evaluateNewSimplex when coefficients are negative.
     * @throws Exception when an exception is thrown
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateNewSimplexNegativeCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = new double[]{0.0};
        SimpleComparator comparator = new SimpleComparator();
        optimizer.evaluateNewSimplex(new RealPointValuePair[]{new RealPointValuePair(xSmallest, 0.0, false)}, -1.0, comparator);
    }

}

class FooFunction implements org.apache.commons.math3.optim.UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(Math.sin(x), 2);
    }

    @Override
    public double derivative(double x) {
        return -2 * Math.sin(x) * Math.cos(x);
    }

}

class SimpleComparator implements Comparator<RealPointValuePair> {

    @Override
    public int compare(RealPointValuePair o1, RealPointValuePair o2) {
        return Double.compare(o1.getValue(), o2.getValue());
    }

}
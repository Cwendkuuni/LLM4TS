package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoRealSolutionException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealConvergenceChecker;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.functions.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.functions.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.functions.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintVector;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testConstructorDefault() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.getKhi(), 0.0);
        assertEquals(0.5, optimizer.getGamma(), 0.0);
    }

    @Test
    public void testConstructorSpecified() {
        MultiDirectional optimizer = new MultiDirectional(1.5, 0.7);
        assertEquals(1.5, optimizer.getKhi(), 0.0);
        assertEquals(0.7, optimizer.getGamma(), 0.0);
    }

    @Test
    public void testIterateSimplexNullComparator() {
        MultiDirectional optimizer = new MultiDirectional();
        assertThrows(IllegalArgumentException.class, () -> optimizer.iterateSimplex(null));
    }

    @Test
    public void testIterateSimplexEmptyComparator() {
        MultiDirectional optimizer = new MultiDirectional();
        final Comparator<PointValuePair> comparator = Comparator.comparingDouble(pp -> pp.getValue());
        assertThrows(IllegalArgumentException.class, () -> optimizer.iterateSimplex(Comparator.comparing()));
        optimizer.iterateSimplex(comparator); // this is fine
    }

    @Test
    public void testIterateSimplexMaxEvaluations() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.setMaxEvaluations(1);
        final Function f = new PolynomialFunction(new double[]{-1.0, 1.0}, 1.0);
        optimizer.optimize(f, new SimpleBounds(1.0, 2.0), new RealConvergenceChecker(1e-4));
        assertThrows(MaxEvaluationsExceededException.class, () -> optimizer.iterateSimplex(Comparator.comparingDouble(pp -> pp.getValue())));
    }

    @Test
    public void testIterateSimplexUnsupportedOperationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        final UnivariateFunction f = () -> 0.0; // not supported
        optimizer.optimize(f, new SimpleBounds(1.0, 2.0), new RealConvergenceChecker(1e-4));
        assertThrows(UnsupportedOperationException.class, () -> optimizer.iterateSimplex(Comparator.comparingDouble(pp -> pp.getValue())));
    }

    @Test
    public void testIterateSimplexNoSolution() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        final UnivariateFunction f = x -> Math.sqrt(Math.abs(x));
        optimizer.optimize(f, new SimpleBounds(-10.0, 10.0), new RealConvergenceChecker(1e-4));
        optimizer.iterateSimplex(Comparator.comparingDouble(pp -> pp.getValue()));
        assertNull(optimizer.getBestSolution());
    }

    @Test
    public void testIterateSimplexConvergence() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        final UnivariateFunction f = x -> Math.pow(Math.sin(x), 2.0);
        optimizer.optimize(f, new SimpleBounds(-10.0, 10.0), new RealConvergenceChecker(1e-8));
        optimizer.iterateSimplex(Comparator.comparingDouble(pp -> pp.getValue()));
        PointValuePair solution = optimizer.getBestSolution();
        assertEquals(0.0, solution.getPoint()[0], 1e-7);
        assertEquals(-Math.PI / 2.0, solution.getValue(), 1e-8);
    }

    @Test
    public void testEvaluateNewSimplexNullComparators() {
        MultiDirectional optimizer = new MultiDirectional();
        assertThrows(NullPointerException.class, () -> optimizer.evaluateNewSimplex(null, 1.0, Comparator.comparingDouble(pp -> pp.getValue())));
        assertThrows(NullPointerException.class, () -> optimizer.evaluateNewSimplex(new RealPointValuePair[]{}, 1.0, Comparator.comparingDouble(pp -> pp.getValue())));
        assertThrows(NullPointerException.class, () -> optimizer.evaluateNewSimplex(new RealPointValuePair[]{new RealPointValuePair(new double[]{})}, 1.0, null));
    }

    @Test
    public void testEvaluateNewSimplexEmptyComparators() {
        MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] original = new RealPointValuePair[]{new RealPointValuePair(new double[]{0.0})};
        assertThrows(NullPointerException.class, () -> optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparing()));
        optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(pp -> pp.getValue()));
    }

    @Test
    public void testEvaluateNewSimplexEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        final UnivariateFunction f = x -> {
            throw new FunctionEvaluationException("Test");
        };
        final RealPointValuePair[] original = new RealPointValuePair[]{new RealPointValuePair(new double[]{0.0})};
        assertThrows(FunctionEvaluationException.class, () -> optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(pp -> pp.getValue())));
    }

    @Test
    public void testEvaluateNewSimplexOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.setMaxEvaluations(1);
        final UnivariateFunction f = x -> Math.pow(Math.sin(x), 2.0);
        final RealPointValuePair[] original = new RealPointValuePair[]{new RealPointValuePair(new double[]{0.0})};
        optimizer.optimize(f, new SimpleBounds(-10.0, 10.0), new RealConvergenceChecker(1e-4));
        assertThrows(OptimizationException.class, () -> optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(pp -> pp.getValue())));
    }

    @Test
    public void testEvaluateNewSimplexIllegalArgumentException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        final UnivariateFunction f = x -> Math.pow(Math.sin(x), 2.0);
        final RealPointValuePair[] original = new RealPointValuePair[]{new RealPointValuePair(new double[]{0.0})};
        optimizer.optimize(f, new SimpleBounds(-10.0, 10.0), new RealConvergenceChecker(1e-4));
        assertThrows(IllegalArgumentException.class, () -> optimizer.evaluateNewSimplex(original, -1.0, Comparator.comparingDouble(pp -> pp.getValue())));
    }

    @Test
    public void testEvaluateNewSimplexLinearFunction() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        final LinearFunction f = new LinearFunction(new double[]{});
        final RealPointValuePair[] original = new RealPointValuePair[]{new RealPointValuePair(new double[]{0.0})};
        optimizer.optimize(f, new SimpleBounds(-10.0, 10.0), new RealConvergenceChecker(1e-4));
        final RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(pp -> pp.getValue()));
        assertArrayEquals(new double[]{0.0}, result.getPointRef(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test
    public void testEvaluateNewSimplexPolynomialFunction() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        final PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0}, 3.0);
        final RealPointValuePair[] original = new RealPointValuePair[]{new RealPointValuePair(new double[]{0.0})};
        optimizer.optimize(f, new SimpleBounds(-10.0, 10.0), new RealConvergenceChecker(1e-4));
        final RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(pp -> pp.getValue()));
        assertArrayEquals(new double[]{0.0}, result.getPointRef(), 1e-7);
        assertEquals(-2.25, result.getValue(), 1e-7);
    }

    @Test
    public void testEvaluateNewSimplexLinearObjectiveFunction() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        final LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{0.0});
        final RealPointValuePair[] original = new RealPointValuePair[]{new RealPointValuePair(new double[]{0.0})};
        optimizer.optimize(f, new SimpleBounds(-10.0, 10.0), new RealConvergenceChecker(1e-4));
        final RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(pp -> pp.getValue()));
        assertArrayEquals(new double[]{0.0}, result.getPointRef(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test
    public void testEvaluateNewSimplexLinearConstraintSet() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        final LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0}, 0.0));
        final UnivariateFunction f = x -> {
            final double[] xConstraint = new double[]{x};
            LinearConstraintVector cv = new LinearConstraintVector(constraints, xConstraint);
            return cv.getValue();
        };
        final RealPointValuePair[] original = new RealPointValuePair[]{new RealPointValuePair(new double[]{0.0})};
        optimizer.optimize(f, new SimpleBounds(-10.0, 10.0), new RealConvergenceChecker(1e-4));
        final RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(pp -> pp.getValue()));
        assertArrayEquals(new double[]{0.0}, result.getPointRef(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

}
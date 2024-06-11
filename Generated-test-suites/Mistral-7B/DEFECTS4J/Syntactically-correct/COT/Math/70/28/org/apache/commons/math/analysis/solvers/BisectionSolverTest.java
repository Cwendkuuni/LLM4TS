package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 4);
        BisectionSolver solver = new BisectionSolver(function);
        assertNotNull(solver.getFunction());
        assertEquals(function, solver.getFunction());
    }

    @Test
    public void testConstructorWithoutFunction() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver.getFunction());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithFunctionAndWrongArguments() {
        UnivariateRealFunction function = x -> 1.0 / x;
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithMinGreaterThanMax() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 5.0, 3.0);
    }

    @Test
 public void testSolveWithSimpleFunction() {
        UnivariateRealFunction function = x -> x * x - 4;
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -3.0, 3.0);
        assertEquals(0.0, root, Precision.EPSILON);
    }

    @Test
    public void testSolveWithFunctionAndInitialValue() {
        UnivariateRealFunction function = x -> x * x - 4;
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -3.0, 3.0, 1.0);
        assertEquals(0.0, root, Precision.EPSILON);
    }

    @Test
    public void testSolveWithMaxIterationsExceeded() {
        UnivariateRealFunction function = x -> Math.sin(Math.PI * x) / x;
        BisectionSolver solver = new BisectionSolver();
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, -1.5, 1.5, 0.0));
    }

    @Test
    public void testSolveWithZeroFunction() {
        UnivariateRealFunction function = x -> 0.0;
        BisectionSolver solver = new BisectionSolver();
        assertThrows(ZeroException.class, () -> solver.solve(function, -1.0, 1.0));
    }

    @Test
    public void testSolveWithFunctionThatDoesNotChangeSign() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) + 1.0;
        BisectionSolver solver = new BisectionSolver();
        assertFalse(solver.solve(function, -1.0, 1.0) instanceof NumberIsTooSmallException);
    }

    @Test
    public void testSolveWithFunctionThatDoesNotChangeSignAndInitialValue() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) + 1.0;
        BisectionSolver solver = new BisectionSolver();
        assertFalse(solver.solve(function, -1.0, 1.0, 0.0) instanceof NumberIsTooSmallException);
    }

    @Test
    public void testSolveWithFunctionThatDoesNotChangeSignAndInitialValueCloseToRoot() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) + 1.0;
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 0.99, 1.01);
        assertTrue(Math.abs(root - 1.0) < 0.01);
    }

    @Test
    public void testSolveWithFunctionThatDoesNotChangeSignAndInitialValueFarFromRoot() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) + 1.0;
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -5.0, 5.0);
        assertTrue(Math.abs(root - 1.0) < 1.0);
    }

    @Test
    public void testSolveWithFunctionThatChangesSignAndInitialValueCloseToRoot() {
        UnivariateRealFunction function = x -> Math.sin(Math.PI * x);
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -0.5, 0.5);
        assertEquals(0.0, root, Precision.EPSILON);
    }

    @Test
    public void testSolveWithFunctionThatChangesSignAndInitialValueFarFromRoot() {
        UnivariateRealFunction function = x -> Math.sin(Math.PI * x);
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -5.0, 5.0);
        assertArrayEquals(new double[] { 1.3779551337556562, 2.6589247853981638 }, new double[] { Math.asin(0.5), Math.PI - Math.asin(0.5) }, FunctionUtils.arraysEqual(new double[] { root, Math.PI - root }));
    }
}
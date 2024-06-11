package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        BisectionSolver solver = new BisectionSolver();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedSolveWithFunctionOnly() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 1);
        BisectionSolver solver = new BisectionSolver(function);
        solver.solve(); // Method is deprecated and throws UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedSolveWithMinMax() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 1);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0); // Method is deprecated and throws UnsupportedOperationException
    }

    @Test
    public void testSolveWithInitialGuess() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 1);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -2.0, 0.0, -1.5);
        assertEquals(0.0, Precision.doubleValue(result, 5), 0.0);
    }

    @Test
    public void testSolveWithMinMax() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 1);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -2.0, 0.0);
        assertEquals(0.0, Precision.doubleValue(result, 5), 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionOutsideInterval() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 1);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 1.0, 0.0); // Function outside the interval
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.sin(x));
        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(0);
        solver.solve(function, -1.0, 1.0);
    }

    @Test
    public void testSolveWithSmallInterval() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.sin(x) - 0.5);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -2.0, 2.0);
        assertEquals(0.5465025006649518, Precision.doubleValue(result, 5), 0.001);
    }

    @Test
    public void testSolveWithLargeInterval() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.sin(x) - 0.5);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -10.0, 10.0);
        assertEquals(0.5465025006649518, Precision.doubleValue(result, 5), 0.001);
    }

}
package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        Assert.assertEquals(100, solver.getMaxIterations());
        Assert.assertEquals(1e-6, solver.getRelativeAccuracy(), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedSolveWithFunctionAndMinMax() {
        BisectionSolver solver = new BisectionSolver(f -> 0.0);
        solver.solve(1.0, 2.0, 1.5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedSolveWithMinMax() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(1.0, 2.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFailingFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = f -> {
            throw new ArithmeticException("Failed function");
        };
        solver.solve(function, 1.0, 2.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new SimpleFunction();
        solver.solve(function, 1.0, 3.0);
    }

    @Test
    public void testSolveSimpleFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new SimpleFunction();
        double result = solver.solve(function, 1.0, 2.0);
        Assert.assertEquals(1.5, result, 0.001);
    }

    @Test
    public void testSolveFunctionWithInitialGuess() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new SimpleFunction();
        double result = solver.solve(function, 1.0, 2.0, 1.75);
        Assert.assertEquals(1.5, result, 0.001);
    }

    private static class SimpleFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x - 1.5, 2);
        }
    }
}
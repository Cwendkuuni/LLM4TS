package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolverFactory;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.E;

public class UnivariateRealSolverUtilsTest {

    @org.junit.Test
    public void testSolveDefaultSolver() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 1;
            }
        };
        double x0 = 0.0;
        double x1 = 1.0;
        double solution = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(E.EULER_NUMBER.doubleValue(), solution, 1e-5);
    }

    @org.junit.Test
    public void testSolveDefaultSolverWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 1;
            }
        };
        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 1e-10;
        double solution = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
        assertEquals(E.EULER_NUMBER.doubleValue(), solution, 1e-11);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() throws Exception {
        UnivariateRealFunction function = null;
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ConvergenceException("Test ConvergenceException");
            }
        };
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Test FunctionEvaluationException");
            }
        };
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double x0 = 3.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testBracketNullFunction() throws Exception {
        UnivariateRealFunction function = null;
        double initial = 0.0;
        double lowerBound = 1.0;
        double upperBound = 2.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testBracketInvalidInterval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double initial = 0.0;
        double lowerBound = 3.0;
        double upperBound = 2.0;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidMaxIterations() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double initial = 0.0;
        double lowerBound = 1.0;
        double upperBound = 2.0;
        int maximumIterations = -1;
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
    }

    @org.junit.Test
    public void testBracketExample() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double initial = 1.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertEquals(1.0, result[0], 1e-5);
        assertEquals(3.0, result[1], 1e-5);
    }

    @org.junit.Test
    public void testBracketExampleWithMaxIterations() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double initial = 1.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 10;
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
        assertEquals(1.0, result[0], 1e-5);
        assertEquals(3.0, result[1], 1e-5);
    }

    @org.junit.Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(1.5, result, 1e-5);
    }

    @org.junit.Test
    public void testSetupFunctionNull() throws Exception {
        UnivariateRealFunction function = null;
        UnivariateRealSolverUtils.setup(function);
        fail();
    }

    @org.junit.Test
    public void testDefaultSolverFactory() throws Exception {
        UnivariateRealSolverFactory factory = UnivariateRealSolverUtils.LazyHolder.FACTORY;
        UnivariateRealSolver solver = factory.newInstance();
        assertTrue(solver instanceof BrentSolver);
    }
}
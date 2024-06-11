package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.special.Log;
import org.apache.commons.math3.special.Logistic;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testDeprecatedSolverConstructor() {
        BisectionSolver solver = new BisectionSolver(mock(UnivariateRealFunction.class));
        assertFalse(solver.isSolveSupported());
    }

    @Test
    public void testSolveWithMinMaxAndFunction() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.sin(Math.PI / 3);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = Math.PI / 6;
        solver.solve(f, 0, 3 * Math.PI / 2);
        assertEquals(expectedResult, solver.getResult(), 1e-9);
    }

    @Test
    public void testSolveWithMinMax() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.sin(Math.PI / 3);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = Math.PI / 6;
        solver.solve(f, 0, 3 * Math.PI / 2);
        assertEquals(expectedResult, solver.getResult(), 1e-9);
    }

    @Test
    public void testSolveWithMinMaxAndInitial() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.sin(Math.PI / 3);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = Math.PI / 6;
        solver.solve(f, 0, 3 * Math.PI / 2, Math.PI / 3);
        assertEquals(expectedResult, solver.getResult(), 1e-9);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithMinOnly() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.sin(Math.PI / 3);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithMaxOnly() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5 * Math.sin(Math.PI / 3);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 3 * Math.PI / 2);
    }

    @Test
    public void testSolveWithFunctionThatHasNoRoots() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -Math.PI, Math.PI));
    }

    @Test
    public void testSolveWithFunctionThatHasOneRoot() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - Math.log(2);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = E.E;
        solver.solve(f, 1, Double.POSITIVE_INFINITY);
        assertEquals(expectedResult, solver.getResult(), 1e-9);
    }

    @Test
    public void testSolveWithFunctionThatHasTwoRoots() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double expectedResult1 = -1;
        double expectedResult2 = 1;
        double[] results = new double[2];
        for (int i = 0; i < 2; i++) {
            solver.clearResult();
            if (i == 0) {
                solver.solve(f, -2, 0);
            } else {
                solver.solve(f, 0, 2);
            }
            results[i] = solver.getResult();
        }
        assertArrayEquals(new double[]{expectedResult1, expectedResult2}, results, 1e-9);
    }

    @Test
    public void testSolveWithLogisticFunction() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new Logistic();
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 1;
        solver.solve(f, 0, 2);
        assertEquals(expectedResult, solver.getResult(), 1e-9);
    }
}
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolveSimple() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        double result;
        try {
            result = solver.solve(f, -2.5, 2.5);
            assertEquals(0.0, Precision.round(result, 5));
        } catch (MaxIterationsExceededException e) {
            fail("Expected to find a solution within the given iterations");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not have thrown an exception");
        }
    }

    @Test
    public void testSolveNegativeFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 2) + 1.0;
            }
        };
        double result;
        try {
            result = solver.solve(f, -2.0, 1.0);
            assertEquals(0.0, Precision.round(result, 5));
        } catch (MaxIterationsExceededException e) {
            fail("Expected to find a solution within the given iterations");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not have thrown an exception");
        }
    }

    @Test
    public void testSolveInvalidFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        try {
            solver.solve(f, 0.0, 0.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveZeroFunction() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        try {
            solver.solve(f, 0.0, 1.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        solver.solve(f, 2.0, -1.0);
    }
}
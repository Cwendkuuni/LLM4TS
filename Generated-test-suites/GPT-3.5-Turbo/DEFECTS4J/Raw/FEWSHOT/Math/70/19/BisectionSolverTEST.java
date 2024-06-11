package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.BeforeClass;
import org.junit.Test;

public class BisectionSolverTest {

    private static BisectionSolver solver;

    @BeforeClass
    public static void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver(f);
        assertNotNull(solver);
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double solution = solver.solve(f, -10, 10);
        assertEquals(2.0, solution, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        solver.solve(f, 2, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        solver.solve(f, -10, 10);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceededException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };

        solver.solve(f, -10, 10);
    }

    @Test
    public void testSolveWithZeroTolerance() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double solution = solver.solve(f, -10, 10, 0);
        assertEquals(2.0, solution, 0);
    }

    @Test
    public void testSolveWithPositiveTolerance() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double solution = solver.solve(f, -10, 10, 0.001);
        assertEquals(2.0, solution, 0.001);
    }

    @Test
    public void testSolveWithNegativeTolerance() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double solution = solver.solve(f, -10, 10, -0.001);
        assertEquals(2.0, solution, 0.001);
    }
}
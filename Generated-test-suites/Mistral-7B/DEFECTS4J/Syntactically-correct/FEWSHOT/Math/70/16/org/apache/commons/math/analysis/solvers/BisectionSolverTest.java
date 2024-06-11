package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Test;

public class BisectionSolverTest {

    private static final double DELTA = 1.0e-12;

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1.0E-6, solver.getRelativeAccuracy(), DELTA);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithFunctionOnly() {
        BisectionSolver solver = new BisectionSolver(FunctionUtils.IDENTITY_FUNCTION);
        solver.solve();
    }

    @Test
    public void testSolveMinMax() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(-1.0, root, DELTA);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-2.0, 2.0};
        solver.solve(f, bounds[0], bounds[1], 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-1.0, 1.0};
        solver.solve(f, bounds[0], bounds[1]);
    }

    @Test
    public void testSolveSameSign() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.5 * x * x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-1.0, 1.0};
        try {
            solver.solve(f, bounds[0], bounds[1]);
            fail();
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testSolveDifferentSign() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.sin(Math.PI / 2.0);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-1.0, 1.0};
        double root = solver.solve(f, bounds[0], bounds[1]);
        assertTrue(Math.abs(Math.PI / 2.0 - root) < DELTA);
    }

    @Test
    public void testSolveInitialGuess() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-3.0, 3.0};
        double root = solver.solve(f, bounds[0], bounds[1], 1.5);
        assertEquals(-1.0, root, DELTA);
    }

    @Test
    public void testSolveWithEpsAbs() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-2.0, 2.0};
        solver.setAbsoluteAccuracy(1.0E-5);
        double root = solver.solve(f, bounds[0], bounds[1]);
        assertTrue(Math.abs(Math.PI / 2.0 - root) < 1.0E-5);
    }

    @Test
    public void testSolveWithEpsRel() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-3.0, 3.0};
        solver.setRelativeAccuracy(1.0E-4);
        double root = solver.solve(f, bounds[0], bounds[1]);
        assertTrue(Math.abs(root - 1.0) < 0.0001);
    }

    @Test
    public void testSolveDifferentFunction() {
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 1.0;
            }
        };
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-3.0, 3.0};
        double root1 = solver.solve(f1, bounds[0], bounds[1]);
        double root2 = solver.solve(f2, bounds[0], bounds[1]);
        assertFalse(Precision.isClose(root1, root2, DELTA));
    }

    @Test
    public void testSolveWithMidpoint() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double[] bounds = {-2.0, 2.0};
        double root = solver.solve(f, bounds[0], bounds[1]);
        double midpoint = UnivariateRealSolverUtils.midpoint(bounds[0], bounds[1]);
        assertTrue(Math.abs(midpoint - root) < Precision.SAFE_EPSILON);
    }
}
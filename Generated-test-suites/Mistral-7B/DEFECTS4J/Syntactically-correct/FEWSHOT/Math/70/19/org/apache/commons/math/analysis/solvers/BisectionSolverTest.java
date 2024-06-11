package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructorNoArgs() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver.getFunction());
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1E-6, solver.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testConstructorFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);
        assertNotNull(solver.getFunction());
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1E-6, solver.getRelativeAccuracy(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithNoFunction() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(0.0, 1.0);
    }

    @Test
    public void testSolveMinMax() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -2.0, 2.0);
        assertEquals(-sqrt(2.0), root, 1E-6);
    }

    @Test
    public void testSolveMinInitial() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -2.0, -1.0);
        assertEquals(-sqrt(2.0), root, 1E-6);
    }

    @Test
    public void testSolveMaxInitial() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, 2.0, 3.0);
        assertEquals(sqrt(2.0), root, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -1.0, 1.0));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws NumberIsTooSmallException {
                if (x < 0) {
                    throw new NumberIsTooSmallException("x");
                }
                return Math.sqrt(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(f, -1.0, 0.0));
    }
}
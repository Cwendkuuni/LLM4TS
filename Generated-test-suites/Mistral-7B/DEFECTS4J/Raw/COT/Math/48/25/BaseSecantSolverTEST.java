package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        function = new MockFunction();
        solver = new BaseSecantSolver(1e-6, Method.PEGASUS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f,
                                double min, double max) {
                return super.solve(maxEval, f, min, max, 0.0);
            }
        };
    }

    @Test
    public void testConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        assertNotNull(solver);
    }

    @Test
    public void testSolveWithDefaultArguments() {
        double result = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, result, 1e-6);
    }

    @Test
    public void testSolveWithMinAndMaxSwapped() {
        solver.solve(function, 1.0, 0.0);
        // The test will fail if the method doesn't handle the case correctly.
    }

    @Test
    public void testSolveWithInitialGuessAtRoot() {
        solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        double result = solver.solve(function, 0.0, 0.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolverThrowsConvergenceException() {
        solver = new BaseSecantSolver(1e-16, Method.PEGASUS);
        solver.solve(function, 0.0, 1.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolverThrowsMaxEvalExceededException() {
        solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.setMaxEval(0);
        solver.solve(function, 0.0, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverThrowsMathIllegalArgumentException() {
        solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(function, Double.NaN, Double.NaN);
    }

    @Test(expected = MathInternalError.class)
    public void testSolverThrowsMathInternalError() {
        solver = new BaseSecantSolver(1e-6, Method.PEGASUS) {
            @Override
            protected double doSolve() {
                throw new MathInternalError();
            }
        };
        solver.solve(function, 0.0, 1.0);
    }
}
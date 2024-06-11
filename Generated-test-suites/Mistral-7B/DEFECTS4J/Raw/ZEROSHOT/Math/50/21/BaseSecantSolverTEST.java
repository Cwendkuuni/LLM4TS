package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        function = f -> Math.pow(f, 2) - 1;
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_minMax_allowedAnySide() throws MaxEvalException, MathIllegalArgumentException {
        double root = solver.solve(10, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertEquals(-0.5, Precision.round(root, 5));
    }

    @Test
    public void testSolve_minMax_allowedLeftSide() throws MaxEvalException, MathIllegalArgumentException, TooManyIterationsException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(10, function, -10.0, 10.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-0.5, Precision.round(root, 5));
    }

    @Test
    public void testSolve_minMax_allowedRightSide() throws MaxEvalException, MathIllegalArgumentException, TooManyIterationsException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(10, function, -10.0, 10.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(-0.5, Precision.round(root, 5));
    }

    @Test
    public void testSolve_minMax_allowedBelowSide() throws MaxEvalException, MathIllegalArgumentException, TooManyIterationsException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.BELOW_SIDE);
        double root = solver.solve(10, function, -10.0, 10.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-0.5, Precision.round(root, 5));
    }

    @Test
    public void testSolve_minMax_allowedAboveSide() throws MaxEvalException, MathIllegalArgumentException, TooManyIterationsException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.ABOVE_SIDE);
        double root = solver.solve(10, function, -10.0, 10.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(-0.5, Precision.round(root, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_minMax_allowedInvalidSide() throws MaxEvalException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.INVALID_SIDE);
        solver.solve(10, function, -10.0, 10.0, AllowedSolution.INVALID_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolve_minMax_invalidFunction() throws MaxEvalException {
        UnivariateRealFunction invalidFunction = f -> {
            throw new UnsupportedOperationException();
        };
        solver.solve(10, invalidFunction, -10.0, 10.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxEvalException.class)
    public void testSolve_maxEval() throws MathIllegalArgumentException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            solver.solve(0, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
            fail();
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    @Test(expected = TooManyIterationsException.class)
    public void testSolve_tooManyIterations() throws MathIllegalArgumentException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI, 1);
        try {
            solver.solve(10, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
            fail();
        } catch (TooManyIterationsException e) {
            // Expected
        }
    }
}
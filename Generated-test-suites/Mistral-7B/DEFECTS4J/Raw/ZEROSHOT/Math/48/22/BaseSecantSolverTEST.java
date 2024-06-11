package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;

public class BaseSecantSolverTest {

    private static final double DELTA = 1e-9;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static UnivariateRealFunction testFunction(double x) {
        return new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
    }

    @org.junit.Test
    public void testSolve_defaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double root = solver.solve(10, testFunction, -10.0, 10.0, 0.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSolve_relativeAndAbsoluteAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, testFunction, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, Precision.SAFE_EPSILON);
    }

    @org.junit.Test
    public void testSolve_minAndMaxValues() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, testFunction, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, Precision.SAFE_EPSILON);
    }

    @org.junit.Test
    public void testSolve_allowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, testFunction, -10.0, 10.0, 0.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(0.0, root, Precision.SAFE_EPSILON);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolve_allowedSolution_invalid() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, testFunction, -10.0, 10.0, 0.0, AllowedSolution. values()[4]);
    }
}

enum AllowedSolution {
    ANY_SIDE, LEFT_SIDE, RIGHT_SIDE, BELOW_SIDE, ABOVE_SIDE;
}
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;

    // Test computeObjectiveValue() method
    @Test
    public void testComputeObjectiveValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() throws ConvergenceException {
                return 0;
            }
        };

        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = solver.computeObjectiveValue(2.0);
        assertEquals(0.0, result, EPSILON);
    }

    // Test verifyBracketing() method
    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() throws ConvergenceException {
                return 0;
            }
        };

        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Case 1: min and max are brackets
        try {
            solver.verifyBracketing(2.0, 3.0);
        } catch (ConvergenceException e) {
            fail("Failed to validate bracketing");
        }

        // Case 2: min and max are not brackets
        try {
            solver.verifyBracketing(2.0, 5.0);
            fail("Failed to throw exception");
        } catch (ConvergenceException e) {
            // Exception expected
        }
    }

    // Test solve() method
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() throws ConvergenceException {
                return 0;
            }
        };

        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Case 1: maxEval > 0
        double result = solver.solve(100, function, 2.0, 3.0);
        assertEquals(0.0, result, EPSILON);

        // Case 2: maxEval = 0
        try {
            solver.solve(0, function, 2.0, 3.0);
            fail("Failed to throw exception");
        } catch (ConvergenceException e) {
            // Exception expected
        }
    }
}
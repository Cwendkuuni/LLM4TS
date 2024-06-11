package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateFunctionUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS);
    }

    @Test
    public void testSolveSimpleCase() {
        double root = solver.solve(10, function, -2.0, 2.0, 0.0);
        assertEquals("The root should be approximately 1.4142135625",
                1.4142135625, Precision.round(root, 12));
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS, 1);
        solver.solve(10, function, -2.0, 2.0, 0.0);
    }

    @Test(expected = TooManyIterationsException.class)
    public void testTooManyIterationsException() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS, 1, 100);
        solver.solve(10, function, -2.0, 2.0, 0.0);
    }

    @Test
    public void testSolveWithRelativeAccuracy() {
        double root = solver.solve(10, function, -2.0, 2.0, 0.0, 1.0e-5);
        assertEquals("The root should be approximately 1.4142135625",
                1.4142135625, Precision.round(root, 12));
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        double root = solver.solve(10, function, -2.0, 2.0, 0.0, 1.0e-5, 1.0e-5);
        assertEquals("The root should be approximately 1.4142135625",
                1.4142135625, Precision.round(root, 12));
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        double root = solver.solve(10, function, -2.0, 2.0, 0.0, 1.0e-5, 1.0e-5, 1.0e-5);
        assertEquals("The root should be approximately 1.4142135625",
                1.4142135625, Precision.round(root, 12));

        // Test very small function value accuracy
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS, 1.0e-6, 1.0e-12);
        root = solver.solve(10, function, -2.0, 2.0, 0.0);
        assertTrue("The root should be approximately 1.4142135625",
                Precision.abs(root - 1.4142135625) < 1.0e-10);
    }

    @Test
    public void testSolveWithAllowedSolutions() {
        // Test left side
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS, 1.0e-12, 1.0e-12,
                BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        double root = solver.solve(10, function, -2.0, 2.0, 0.0);
        assertTrue("The root should be less than 0", root < 0);

        // Test right side
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS, 1.0e-12, 1.0e-12,
                BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        root = solver.solve(10, function, -2.0, 2.0, 0.0);
        assertTrue("The root should be greater than 0", root > 0);

        // Test below side
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS, 1.0e-12, 1.0e-12,
                BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        root = solver.solve(10, function, -2.0, 2.0, 0.0);
        assertTrue("The root should be negative", root < 0);

        // Test above side
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS, 1.0e-12, 1.0e-12,
                BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        root = solver.solve(10, function, -2.0, 2.0, 0.0);
        assertTrue("The root should be positive", root > 0);

        // Test any side
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS, 1.0e-12, 1.0e-12,
                BaseSecantSolver.AllowedSolution.ANY_SIDE);
        root = solver.solve(10, function, -2.0, 2.0, 0.0);
        assertTrue("The root should be approximately 1.4142135625",
                Precision.round(root, 12) == 1.4142135625);
    }

    @Test
    public void testVerifyBracketing() {
        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS);
        double x0 = 1.0;
        double x1 = 2.0;
        double f0 = function.value(x0);
        double f1 = function.value(x1);

        // Test bracketing
        try {
            solver.doSolve();
            fail("Expected BracketingViolationException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test non-bracketing
        x1 = x0 + 0.1;
        try {
            solver.doSolve();
            fail("Expected BracketingViolationException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test bracketing with inverted interval
        x1 = x0 - 0.1;
        solver.doSolve();
    }
}
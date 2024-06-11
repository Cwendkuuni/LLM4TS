package org.junit.tests.univariaterealsolverutils;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {
    // ... test cases from the previous example ...

    @Test
    public void testSolveWithNullFunction() {
        UnivariateRealFunction f = null;
        double x0 = 0.0;
        double x1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // Expected exceptions are not IllegalStateException
        }
    }

    @Test
    public void testSolveWithInvalidIntervalLowerBoundGreaterThanUpperBound() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0});
        double x0 = 2.0;
        double x1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid bracketing parameters: lower bound=2.0, initial=1.0, "
                         + "upper bound=1.0", e.getMessage());
        }
    }

    @Test
    public void testSolveWithDifferentSolvers() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0});
        double x0 = 0.0;
        double x1 = 1.0;
        double solution = UnivariateRealSolverUtils.solve(f, x0, x1);
        // Test different solvers
        UnivariateRealSolver solver1 = UnivariateRealSolverFactory.newBrentSolver();
        UnivariateRealSolver solver2 = UnivariateRealSolverFactory.newBisectionSolver();
        double solution1 = solver1.solve(f, x0, x1);
        double solution2 = solver2.solve(f, x0, x1);
        assertEquals(solution, solution1, 0.001);
        assertEquals(solution, solution2, 0.001);
    }

    // ... other test cases ...
}
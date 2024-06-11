package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.solvers.BracketingBrentSolver;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.LBFGSBolver;
import org.apache.commons.math3.solvers.SecantSolver;
import org.apache.commons.math3.solvers.UnivariateSolver;
import org.junit.Test;

/**
 * Test class for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {
    /**
     * Test of solve method, of class UnivariateRealSolverUtils.
     */
    @Test
    public void testSolve() throws Exception {
        double[] roots = {1.0, 2.0, 3.0};
        UnivariateRealFunction f = new PolynomialFunction(3, roots);
        UnivariateRealSolver solver = null;

        // Test default solver
        double x0 = 0.0;
        double x1 = 5.0;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        solver = LazyHolder.FACTORY.newDefaultSolver();
        assertEquals(roots[0], solver.solve(f, x0, x1), 1e-6);

        // Test setAbsoluteAccuracy method
        solver.setAbsoluteAccuracy(1e-12);
        result = UnivariateRealSolverUtils.solve(f, x0, x1, 1e-12);
        assertEquals(roots[0], result, 1e-13);

        // Test exception handling
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(null, x0, x1));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, x1, x0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x0));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1, -1.0));

        // Test BrentSolver
        solver = new BracketingBrentSolver();
        result = UnivariateRealSolverUtils.solve(f, x0, x1, solver);
        assertEquals(roots[0], result, 1e-6);

        // Test LBFGSBolver
        solver = new LBFGSBolver();
        result = UnivariateRealSolverUtils.solve(f, x0, x1, solver);
        assertEquals(roots[0], result, 1e-6);

        // Test SecantSolver
        solver = new SecantSolver();
        result = UnivariateRealSolverUtils.solve(f, x0, x1, solver);
        assertEquals(roots[0], result, 1e-6);

        // Test bracket method
        double[] bracketingRoots = UnivariateRealSolverUtils.bracket(f, 2.5, 1.0, 4.0)[0];
        assertEquals(1.0, bracketingRoots[0], 1e-6);
        assertEquals(2.5, bracketingRoots[1], 1e-6);

        // Test exception handling in bracket method
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, 2.5, 1.0, 1.0, 0));
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(null, 2.5, 1.0, 1.0, 10));
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, 2.5, 1.0, 1.0, -1));
    }

    /**
     * Test of bracket method, of class UnivariateRealSolverUtils.
     */
    @Test
    public void testBracket() throws MaxIterationsExceededException, PrecisionException {
        UnivariateRealFunction f = new PolynomialFunction(3, new double[]{1.0, 2.0, 3.0});
        double[] result = UnivariateRealSolverUtils.bracket(f, 2.5, 1.0, 4.0, 100);
        assertEquals(1.0, result[0], 1e-6);
        assertEquals(2.5, result[1], 1e-6);
    }
}
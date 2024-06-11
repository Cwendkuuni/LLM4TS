package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test class for BaseSecantSolver.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolve() throws Exception {
        // Test with Regula Falsi method
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        UnivariateFunction function = new PolynomialFunctions.SixthDegreePolynomial(1);
        double min = -5.0;
        double max = 5.0;
        double root = 1.0;
        double result = solver.solve(100, function, min, max, root);
        assertEquals(root, Precision.round(result, 6), 1e-6);

        // Test with Illinois method
        solver = new PegasusSolver(1e-6);
        result = solver.solve(100, function, min, max, root);
        assertEquals(root, Precision.round(result, 6), 1e-6);

        // Test with Pegasus method
        solver = new RegulaFalsiSolver(1e-6);
        result = solver.solve(100, function, min, max, root);
        assertEquals(root, Precision.round(result, 6), 1e-6);

        // Test with maximum number of evaluations
        function = new PolynomialFunctions.QuarticPolynomial(1);
        solver = new IllinoisSolver(1e-6);
        assertThrows(MaxEvalExceededException.class, () -> solver.solve(0, function, min, max, root));

        // Test with function value out of range
        solver = new IllinoisSolver(1e-6);
        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(100, new PolynomialFunctions.FourthDegreePolynomial(-1), min, max, root));

        // Test with unsupported operation
        solver = new IllinoisSolver(1e-6);
        UnivariateFunction unsupportedFunction = () -> { throw new UnsupportedOperationException(); };
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(100, unsupportedFunction, min, max, root));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveAllowedSolution() throws Exception {
        // Test with Regula Falsi method and left solution
        BaseSecantSolver solver = new IllinoisSolver(1e-6, AllowedSolution.LEFT_SIDE);
        UnivariateFunction function = new PolynomialFunctions.SixthDegreePolynomial(1);
        double min = -5.0;
        double max = 5.0;
        double root = 1.0;
        double result = solver.solve(100, function, min, max, root);
        assertEquals(root, Precision.round(result, 6), 1e-6);

        // Test with Illinois method and right solution
        solver = new PegasusSolver(1e-6, AllowedSolution.RIGHT_SIDE);
        result = solver.solve(100, function, min, max, root);
        assertEquals(root, Precision.round(result, 6), 1e-6);

        // Test with Pegasus method and below solution
        solver = new RegulaFalsiSolver(1e-6, AllowedSolution.BELOW_SIDE);
        result = solver.solve(100, function, min, max, root);
        assertEquals(root, Precision.round(result, 6), 1e-6);

        // Test with Pegasus method and above solution
        solver = new RegulaFalsiSolver(1e-6, AllowedSolution.ABOVE_SIDE);
        result = solver.solve(100, function, min, max, root);
        assertEquals(root, Precision.round(result, 6), 1e-6);
    }

}
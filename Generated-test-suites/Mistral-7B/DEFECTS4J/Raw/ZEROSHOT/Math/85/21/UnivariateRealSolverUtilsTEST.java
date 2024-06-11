package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BracketedUnivariateSolver;
import org.apache.commons.math3.solvers.UnivariateSolver;

/**
 * Test class for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @org.junit.Test
    public void testSolve() {
        // Test with a simple quadratic function
        UnivariateRealFunction function = new PolynomialFunction(2.0, -1.0, 1.0);
        double root = UnivariateRealSolverUtils.solve(function, 1.0, 3.0);
        assertEquals(1.5, root, 1e-12);

        // Test with a function that has no roots
        function = new PolynomialFunction(2.0, 1.0, -1.0);
        assertThrows(NoRootsException.class, () -> UnivariateRealSolverUtils.solve(function, 1.0, 3.0));

        // Test with a function that does not evaluate correctly
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(function, 1.0, 3.0));

        // Test with a function that is not UnivariateRealFunction
        UnivariateFunction nonFunction = () -> {};
        assertThrows(ClassCastException.class, () -> UnivariateRealSolverUtils.solve((UnivariateRealFunction) nonFunction, 1.0, 3.0));

        // Test with lower bound equal to upper bound
        function = new PolynomialFunction(1.0, 0.0, 0.0);
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(function, 0.0, 0.0));

        // Test with absolute accuracy less than zero
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(function, 1.0, 3.0, -1.0));

        // Test with a default solver
        UnivariateRealFunction f = new PolynomialFunction(3.0, -2.0, 2.0);
        double x0 = 1.0;
        double x1 = 2.0;
        double rootDefault = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertArrayEquals(new double[] { 1.5, 1.5 }, UnivariateRealSolverUtils.bracket(f, x0, x1)[0], 0.0);
        assertEquals(1.5, rootDefault, 1e-12);

        // Test with a specified solver
        UnivariateRealSolver solver = new BrentSolver();
        double rootSpecified = UnivariateRealSolverUtils.solve(f, x0, x1, 1e-12, solver);
        assertEquals(1.5, rootSpecified, 1e-12);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     */
    @org.junit.Test
    public void testBracket() {
        // Test with a simple quadratic function
        UnivariateRealFunction function = new PolynomialFunction(2.0, -1.0, 1.0);
        double[] bracketed = UnivariateRealSolverUtils.bracket(function, 1.0, -2.0, 5.0, 100);
        assertArrayEquals(new double[] { 1.0, 3.0 }, bracketed, 1e-12);

        // Test with a function that has no roots
        function = new PolynomialFunction(2.0, 1.0, -1.0);
        assertThrows(NoRootsException.class, () -> UnivariateRealSolverUtils.bracket(function, 1.0, -2.0, 5.0, 100));

        // Test with a function that does not evaluate correctly
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(function, 1.0, -2.0, 5.0, 100));

        // Test with a function that is not UnivariateRealFunction
        UnivariateFunction nonFunction = () -> {};
        assertThrows(ClassCastException.class, () -> UnivariateRealSolverUtils.bracket((UnivariateRealFunction) nonFunction, 1.0, -2.0, 5.0, 100));

        // Test with maximum iterations less than zero
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(function, 1.0, -2.0, 5.0, -1));

        // Test with lower bound equal to upper bound
        function = new PolynomialFunction(1.0, 0.0, 0.0);
        assertFalse(UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 0.0, 100)[0] instanceof double[]);

        // Test with lower bound greater than upper bound
        function = new PolynomialFunction(1.0, 2.0, 1.0);
        assertFalse(UnivariateRealSolverUtils.bracket(function, 2.0, 1.0, 1.0, 100)[0] instanceof double[]);

        // Test with a specified solver
        UnivariateRealSolver solver = new BrentSolver();
        double[] bracketedSpecified = UnivariateRealSolverUtils.bracket(function, 1.0, -2.0, 5.0, 100, solver);
        assertArrayEquals(new double[] { 1.0, 3.0 }, bracketedSpecified, 1e-12);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}.
     */
    @org.junit.Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double midpointExpected = 1.5;
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(midpointExpected, midpoint, 1e-12);
    }
}
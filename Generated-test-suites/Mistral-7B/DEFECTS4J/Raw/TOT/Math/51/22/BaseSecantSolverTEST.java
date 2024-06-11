package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        solver = new RegulaFalsiSolver(1e-6);
        function = new PolynomialFunction(new double[]{-1, 4, -1}, 3);
    }

    /**
     * Test that the solver correctly finds the root when the function is a
     * polynomial of degree 3 and bracketing condition is satisfied.
     */
    @Test
    public void testSolvePolynomialRoot() {
        double root = solver.solve(50, function, -5.0, 5.0);
        assertEquals(3.0, root, 1e-8);
    }

    /**
     * Test that the solver throws an exception when the function is not
     * bracketing the root.
     */
    @Test(expected = TooManyIterationsException.class)
    public void testSolveNotBracketing() {
        solver.solve(50, function, 5.0, 5.0);
    }

    /**
     * Test that the solver correctly handles a null function.
     */
    @Test(expected = NullArgumentException.class)
    public void testSolveNullFunction() {
        solver.solve(50, null, 0.0, 0.0);
    }

    /**
     * Test that the solver correctly handles a function that throws an
     * exception for some input values.
     */
    @Test
    public void testSolveThrowingFunction() {
        UnivariateFunction throwingFunction = new UnivariateFunction() {
            @Override
            public double value(double x) throws MathIllegalArgumentException {
                if (x < 0) {
                    throw new MathIllegalArgumentException("x must be non-negative");
                }
                return Math.sqrt(Math.abs(x));
            }
        };
        double root = solver.solve(50, throwingFunction, 0.0, 1.0);
        assertEquals(1.0, root, Precision.EPSILON);
    }

    /**
     * Test that the solver correctly handles a function with no roots.
     */
    @Test
    public void testSolveNoRoots() {
        UnivariateFunction noRootsFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        try {
            solver.solve(50, noRootsFunction, -10.0, 10.0);
            fail();
        } catch (AssertionError e) {
            // Expected
        }
    }
}
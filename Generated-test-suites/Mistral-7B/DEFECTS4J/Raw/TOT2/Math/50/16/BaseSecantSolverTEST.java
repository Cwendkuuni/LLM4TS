package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** The solver to test. */
    private BaseSecantSolver solver;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        // Create a solver with the default method and accuracy.
        solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
    }

    /**
     * Test solver with a simple function.
     */
    @Test
    public void testSolverSimpleFunction() {
        final UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 4);
        final double solution = solver.solve(function, -3.0, 3.0);
        assertEquals(2.0, solution, Precision.EPSILON);
    }

    /**
     * Test solver with a function having one root.
     */
    @Test
    public void testSolverFunctionOneRoot() {
        final UnivariateFunction function = x -> Math.pow(x, 2) - 1;
        final double solution = solver.solve(function, -3.0, 3.0);
        assertEquals(-1.0, solution, Precision.EPSILON);
    }

    /**
     * Test solver with a function having two roots.
     */
    @Test
    public void testSolverFunctionTwoRoots() {
        final UnivariateFunction function = x -> Math.pow(x, 2) - 4.0;
        final double solution = solver.solve(function, -3.0, 3.0);
        assertEquals(-2.0, solution, Precision.EPSILON);
        final double solution2 = solver.solve(function, -3.0, 3.0);
        assertEquals(2.0, solution2, Precision.EPSILON);
    }

    /**
     * Test solver with a function having no roots.
     */
    @Test
    public void testSolverFunctionNoRoots() {
        final UnivariateRealFunction function = x -> Math.pow(x, 2) + 1;
        assertThrows(MaxEvalException.class, () -> solver.solve(function, -3.0, 3.0));
    }

    /**
     * Test solver with a function having incorrect input.
     */
    @Test
    public void testSolverFunctionIncorrectInput() {
        final UnivariateRealFunction function = null;
        assertThrows(NullArgumentException.class, () -> solver.solve(function, -3.0, 3.0));
    }

    /**
     * Test solver with a function having an unsupported operation.
     */
    @Test
    public void testSolverFunctionUnsupportedOperation() {
        final UnivariateRealFunction function = (x) -> {
            throw new UnsupportedOperationException();
        };
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(function, -3.0, 3.0));
    }

}
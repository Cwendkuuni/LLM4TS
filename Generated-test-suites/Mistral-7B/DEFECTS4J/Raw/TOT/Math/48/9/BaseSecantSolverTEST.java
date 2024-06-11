package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * A simple function for testing purposes.
     */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 1;
        }
    };

    /**
     * Tests {@code BaseSecantSolver} with the default constructor and
     * {@code RegulaFalsiSolver} method.
     *
     * @throws ConvergenceException if the solver doesn't converge.
     */
    @Test
    public void testDefaultConstructorRegulaFalsi() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver(
                BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(10, FUNCTION, -2.0, 2.0);
        assertEquals(-1.0, result, Precision.EPSILON);
    }

    /**
     * Tests {@code BaseSecantSolver} with a custom constructor and
     * {@code IllinoisSolver} method.
     *
     * @throws ConvergenceException if the solver doesn't converge.
     */
    @Test
    public void testCustomConstructorIllinois() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8,
                BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(10, FUNCTION, -2.0, 2.0);
        assertEquals(-1.0, result, Precision.EPSILON);
    }

    /**
     * Tests {@code BaseSecantSolver} with a custom constructor and
     * {@code PegasusSolver} method.
     *
     * @throws ConvergenceException if the solver doesn't converge.
     */
    @Test
    public void testCustomConstructorPegasus() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-8, 1e-8,
                BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(10, FUNCTION, -2.0, 2.0);
        assertEquals(-1.0, result, Precision.EPSILON);
    }

    /**
     * Tests {@code BaseSecantSolver} with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputs() {
        new BaseSecantSolver(1e-8, BaseSecantSolver.Method.PEGASUS, null);
    }

    /**
     * Tests {@code BaseSecantSolver} with a non-bracketing function.
     *
     * @throws ConvergenceException if the solver doesn't converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonBracketingFunction() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8,
                BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 1;
            }
        }, -2.0, 2.0);
    }
}
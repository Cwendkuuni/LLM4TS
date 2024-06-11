package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * JUnit test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.IllinoisMethod.IL13);
        assertEquals(-1.0, Precision.round(solver.solve(10, f, -2.0, 2.0, 0.0), 6), 0.0);

        solver = new PegasusSolver(1e-6, PegasusSolver.PegasusMethod.PEGASUS_U);
        assertEquals(-1.0, Precision.round(solver.solve(10, f, -2.0, 2.0, 0.0), 6), 0.0);

        solver = new RegulaFalsiSolver(1e-6);
        assertEquals(-1.0, Precision.round(solver.solve(10, f, -2.0, 2.0, 0.0), 6), 0.0);

        // Test with MaxEvalException
        solver = new RegulaFalsiSolver(1e-6);
        assertThrows(MaxEvalException.class, () -> solver.solve(0, f, -2.0, 2.0, 0.0));

        // Test with NumberIsTooSmallException
        solver = new RegulaFalsiSolver(1e-6);
        assertThrows(NumberIsTooSmallException.class,
                () -> solver.solve(10, f, 2.0, 2.0, 0.0));

        // Test with NumberIsTooLargeException
        solver = new RegulaFalsiSolver(1e-6);
        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(10, f, -2.0, 2.0, 3.0));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.IllinoisMethod.IL13);
        assertEquals(-1.0, Precision.round(solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE), 6), 0.0);

        solver = new PegasusSolver(1e-6, PegasusSolver.PegasusMethod.PEGASUS_U);
        assertEquals(-1.0, Precision.round(solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.ABOVE_SIDE), 6), 0.0);

        solver = new RegulaFalsiSolver(1e-6);
        assertEquals(-1.0, Precision.round(solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.BELOW_SIDE), 6), 0.0);

        // Test with MaxEvalException
        solver = new RegulaFalsiSolver(1e-6);
        assertThrows(MaxEvalException.class, () -> solver.solve(0, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE));

        // Test with NumberIsTooSmallException
        solver = new RegulaFalsiSolver(1e-6);
        assertThrows(NumberIsTooSmallException.class,
                () -> solver.solve(10, f, 2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE));

        // Test with NumberIsTooLargeException
        solver = new RegulaFalsiSolver(1e-6);
        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(10, f, -2.0, 2.0, 3.0, AllowedSolution.BELOW_SIDE));
    }
}
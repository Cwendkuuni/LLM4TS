package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test with a simple function whose root is easily found by all methods.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleFunction() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        double xMin = -2.0;
        double xMax = 2.0;
        double absTol = 1e-6;
        double relTol = 1e-6;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(absTol, relTol);
        double root = solver.solve(f, xMin, xMax);
        assertEquals("Root not found", -2.0, root, 1e-6);

        solver = new IllinoisSolver(absTol, relTol);
        root = solver.solve(f, xMin, xMax);
        assertEquals("Root not found", -2.0, root, 1e-6);

        solver = new PegasusSolver(absTol, relTol);
        root = solver.solve(f, xMin, xMax);
        assertEquals("Root not found", -2.0, root, 1e-6);
    }

    /**
     * Test with a function that has no roots in the given interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoBracketingException.class)
    public void testNoRoots() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double xMin = 0.0;
        double xMax = 2.0 * Math.PI;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6, 1e-6);
        solver.solve(f, xMin, xMax);
    }

    /**
     * Test with a function that has multiple roots in the given interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxEvalException.class)
    public void testMultipleRoots() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 3.0) - 1.0;
            }
        };
        double xMin = -3.0;
        double xMax = 3.0;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6, 1e-6);
        solver.solve(f, xMin, xMax, 100);
    }
}
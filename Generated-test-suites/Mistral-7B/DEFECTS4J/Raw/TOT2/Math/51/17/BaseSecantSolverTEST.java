package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;

/**
 * Tests for {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method for {@link RegulaFalsiSolver}.
     */
    @Test
    public void testRegulaFalsiSolver() {
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final double functionValueTol = 1e-8;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        final double min = -3.0;
        final double max = 3.0;
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(absTol, relTol, functionValueTol);
        final double root = solver.solve(f, min, max);
        assertEquals("The root is not -1.0", -1.0, root, Precision.SAFE_EPSILON);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method for {@link RegulaFalsiSolver}.
     */
    @Test
    public void testRegulaFalsiSolverAllowedSolution() {
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final double functionValueTol = 1e-8;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        final double min = -3.0;
        final double max = 3.0;
        final AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(absTol, relTol, functionValueTol);
        final double root = solver.solve(f, min, max, allowedSolution);
        assertEquals("The root is not -1.0", -1.0, root, Precision.SAFE_EPSILON);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method for {@link IllinoisSolver}.
     */
    @Test
    public void testIllinoisSolver() {
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final double functionValueTol = 1e-8;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        final double min = -3.0;
        final double max = 3.0;
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new IllinoisSolver(absTol, relTol, functionValueTol);
        final double root = solver.solve(f, min, max);
        assertEquals("The root is not -1.0", -1.0, root, Precision.SAFE_EPSILON);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method for {@link IllinoisSolver}.
     */
    @Test
    public void testIllinoisSolverAllowedSolution() {
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final double functionValueTol = 1e-8;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        final double min = -3.0;
        final double max = 3.0;
        final AllowedSolution allowedSolution = AllowedSolution.RIGHT_SIDE;
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new IllinoisSolver(absTol, relTol, functionValueTol);
        final double root = solver.solve(f, min, max, allowedSolution);
        assertEquals("The root is not 1.0", 1.0, root, Precision.SAFE_EPSILON);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method for {@link PegasusSolver}.
     */
    @Test
    public void testPegasusSolver() {
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final double functionValueTol = 1e-8;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        final double min = -3.0;
        final double max = 3.0;
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new PegasusSolver(absTol, relTol, functionValueTol);
        final double root = solver.solve(f, min, max);
        assertEquals("The root is not -1.0", -1.0, root, Precision.SAFE_EPSILON);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method for {@link PegasusSolver}.
     */
    @Test
    public void testPegasusSolverAllowedSolution() {
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final double functionValueTol = 1e-8;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };

        final double min = -3.0;
        final double max = 3.0;
        final AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new PegasusSolver(absTol, relTol, functionValueTol);
        try {
            solver.solve(f, min, max, allowedSolution);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method for {@link SecantSolver}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSecantSolver() {
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final double functionValueTol = 1e-8;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        final double min = -3.0;
        final double max = 3.0;
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new SecantSolver(absTol, relTol, functionValueTol);
        solver.solve(f, min, max);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method for {@link SecantSolver} with maxEval.
     */
    @Test(expected = MaxEvalException.class)
    public void testSecantSolverMaxEval() {
        final double absTol = 1e-6;
        final double relTol = 1e-4;
        final double functionValueTol = 1e-8;
        final int maxEval = 1;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        final double min = 0.0;
        final double max = 2.0 * Math.PI;
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new SecantSolver(absTol, relTol, functionValueTol);
        solver.solve(f, min, max, maxEval);
    }
}
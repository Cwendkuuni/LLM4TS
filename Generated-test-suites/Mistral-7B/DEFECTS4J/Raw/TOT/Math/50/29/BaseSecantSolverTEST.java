package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the {@code BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * A simple test function for which the {@code BaseSecantSolver} should
     * find a root.
     */
    private static final UnivariateFunction F_SIMPLE = new UnivariateFunction() {

        @Override
        public double value(double x) {
            return x * x - 2;
        }

        @Override
        public double derivative(double x) {
             return 2 * x;
        }
    };

    /**
     * A test function for which the {@code BaseSecantSolver} may have issues
     * finding a root due to its extreme behavior near the root.
     */
    private static final UnivariateFunction F_EXTREME = new UnivariateFunction() {

        @Override
        public double value(double x) {
            return Math.sin(Math.PI / 2 * x) / (Math.PI / 2 * x);
        }

        @Override
        public double derivative(double x) {
            return (Math.PI / 2 * x - Math.sin(Math.PI / 2 * x) / (Math.PI / 2 * x * x));
        }
    };

    /**
     * Tests the solver with a function for which it can find a root.
     */
    @Test
    public void testSolveSimple() {
        final double tolerance = 1e-9;
        final double x = new BaseSecantSolver(RegulaFalsiSolver.class, tolerance)
                         .solve(F_SIMPLE, -2.5, 2.5);
        assertTrue(Precision.isClose(x, 0.0, tolerance));
    }

    /**
     * Tests the solver with a function for which it may have issues finding a
     * root.
     */
    @Test(expected = PrecisionExceededException.class)
    public void testSolveExtreme() {
        final double tolerance = 1e-6;
        new BaseSecantSolver(RegulaFalsiSolver.class, tolerance)
                .solve(F_EXTREME, -10.0, 10.0);
    }

    /**
     * Tests the solver with a function for which it cannot find a root.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketing() {
        final double tolerance = 1e-6;
        new BaseSecantSolver(RegulaFalsiSolver.class, tolerance)
                .solve(new UnivariateFunction() {

                    @Override
                    public double value(double x) {
                        return Math.sin(Math.PI * x);
                    }

                    @Override
                    public double derivative(double x) {
                        return Math.cos(Math.PI * x);
                    }
                }, 0.0, 2.0);
    }

    /**
     * Tests the solver with a function for which it may exceed the maximum
     * number of allowed evaluations.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testSolveMaxEval() {
        final int maxEval = 10;
        final double tolerance = 1e-6;
        new BaseSecantSolver(RegulaFalsiSolver.class, tolerance, maxEval)
                .solve(F_SIMPLE, -2.5, 2.5);
    }

    /**
     * Tests the solver with a function for which it should find a root on the
     * left side of the interval.
     */
    @Test
    public void testSolveLeftSide() {
        final double tolerance = 1e-9;
        final double x = new BaseSecantSolver(RegulaFalsiSolver.class, tolerance,
                                             AllowedSolution.LEFT_SIDE)
                         .solve(F_SIMPLE, -2.5, 0.0);
        assertTrue(Precision.isClose(x, -1.0, tolerance));
    }

    /**
     * Tests the solver with a function for which it should find a root on the
     * right side of the interval.
     */
    @Test
    public void testSolveRightSide() {
        final double tolerance = 1e-9;
        final double x = new BaseSecantSolver(RegulaFalsiSolver.class, tolerance,
                                             AllowedSolution.RIGHT_SIDE)
                         .solve(F_SIMPLE, 0.0, 2.5);
        assertTrue(Precision.isClose(x, 1.0, tolerance));
    }

    /**
     * Tests the solver with a function for which it should find a root below
     * the current approximation.
     */
    @Test
    public void testSolveBelowSide() {
        final double tolerance = 1e-9;
        final double x = new BaseSecantSolver(RegulaFalsiSolver.class, tolerance,
                                             AllowedSolution.BELOW_SIDE)
                         .solve(F_SIMPLE, -2.5, 0.0);
        assertTrue(Precision.isClose(x, -1.0, tolerance));
    }

    /**
     * Tests the solver with a function for which it should find a root above
     * the current approximation.
     */
    @Test
    public void testSolveAboveSide() {
        final double tolerance = 1e-9;
        final double x = new BaseSecantSolver(RegulaFalsiSolver.class, tolerance,
                                             AllowedSolution.ABOVE_SIDE)
                         .solve(F_SIMPLE, 0.0, 2.5);
        assertTrue(Precision.isClose(x, 1.0, tolerance));
    }
}
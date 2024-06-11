package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test solver with {@link RegulaFalsiSolver} method.
     */
    @Test
    public void testRegulaFalsiSolver() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1e-8, Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -1, 1);
            }
        };

        final double root = solver.solve(100, f, -1, 1, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test solver with {@link IllinoisSolver} method.
     */
    @Test
    public void testIllinoisSolver() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1e-5, Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -Math.PI, Math.PI, 0.0);
            }
        };

        final double root = solver.solve(100, f, -Math.PI, Math.PI, 0.0);
        assertEquals(Math.PI / 2, root, 1e-5);
    }

    /**
     * Test solver with {@link PegasusSolver} method.
     */
    @Test
    public void testPegasusSolver() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -2, 2, 0.0);
            }
        };

        final double root = solver.solve(100, f, -2, 2, 0.0);
        assertEquals(-1.0, root, 1e-6);

        assertThrows(MaxEvalException.class, () -> solver.solve(1, f, -2, 2, 0.0));
        assertThrows(ZeroException.class, () -> solver.solve(100, f, 0, 0, 0.0));
    }

    /**
     * Test solver with {@link PegasusSolver} method and different bracketing
     * solutions.
     */
    @Test
    public void testPegasusSolverWithAllowedSolution() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS,
                0.001, 1e-6, 1e-6, AllowedSolution.BELOW_SIDE) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -2, 2, 0.0);
            }
        };

        final double root = solver.solve(100, f, -2, 2, 0.0);
        assertEquals(-1.0, root, 1e-6);

        final BaseSecantSolver solverLeft = new BaseSecantSolver(1e-6, Method.PEGASUS,
                0.001, 1e-6, 1e-6, AllowedSolution.LEFT_SIDE) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -2, 2, 0.0);
            }
        };

        final double rootLeft = solverLeft.solve(100, f, -2, 2, 0.0);
        assertEquals(-1.0, rootLeft, 1e-6);

        final BaseSecantSolver solverRight = new BaseSecantSolver(1e-6, Method.PEGASUS,
                0.001, 1e-6, 1e-6, AllowedSolution.RIGHT_SIDE) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -2, 2, 0.0);
            }
        };

        final double rootRight = solverRight.solve(100, f, -2, 2, 0.0);
        assertEquals(-1.0, rootRight, 1e-6);
    }

    /**
     * Test solver with {@link PegasusSolver} method and invalid bracketing.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPegasusSolverWithInvalidBracketing() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS,
                0.001, 1e-6, 1e-6, AllowedSolution.ABOVE_SIDE) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -2, 2, 0.0);
            }
        };

        solver.solve(100, f, -2, 2, 0.0);
    }
}
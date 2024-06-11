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
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Function for testing. */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {
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
     * Test regula falsi method.
     *
     * @throws MaxEvalException if max evaluations are reached
     * @throws NoBracketingException if root is not bracketed
     */
    @Test
    public void testRegulaFalsi() throws MaxEvalException, NoBracketingException {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-10;
        final Method method = Method.REGULA_FALSI;
        final BaseSecantSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver<>(relativeAccuracy, absoluteAccuracy, method) {
                    @Override
                    public BracketedUnivariateRealSolver<UnivariateRealFunction>
                            createSolverInstance(double absoluteAccuracy,
                                                Method method) {
                        return new RegulaFalsiSolver<>(absoluteAccuracy);
                    }
                };
        final double root = solver.solve(100, FUNCTION, -5.0, 5.0, 0.0);
        assertEquals("Root should be approximately -2.0", -2.0, root, 1e-6);
    }

    /**
     * Test Illinois method.
     *
     * @throws MaxEvalException if max evaluations are reached
     * @throws NoBracketingException if root is not bracketed
     */
    @Test
    public void testIllinois() throws MaxEvalException, NoBracketingException {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-10;
        final Method method = Method.ILLINOIS;
        final BaseSecantSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver<>(relativeAccuracy, absoluteAccuracy, method) {
                    @Override
                    public BracketedUnivariateRealSolver<UnivariateRealFunction>
                            createSolverInstance(double absoluteAccuracy,
                                                Method method) {
                        return new IllinoisSolver<>(absoluteAccuracy);
                    }
                };
        final double root = solver.solve(100, FUNCTION, -5.0, 5.0, 0.0);
        assertEquals("Root should be approximately -2.0", -2.0, root, 1e-6);
    }

    /**
     * Test pegasus method.
     *
     * @throws MaxEvalException if max evaluations are reached
     * @throws NoBracketingException if root is not bracketed
     */
    @Test
    public void testPegasus() throws MaxEvalException, NoBracketingException {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-10;
        final Method method = Method.PEGASUS;
        final BaseSecantSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver<>(relativeAccuracy, absoluteAccuracy, method) {
                    @Override
                    public BracketedUnivariateRealSolver<UnivariateRealFunction>
                            createSolverInstance(double absoluteAccuracy,
                                                Method method) {
                        return new PegasusSolver<>(absoluteAccuracy);
                    }
                };
        final double root = solver.solve(100, FUNCTION, -5.0, 5.0, 0.0);
        assertEquals("Root should be approximately -2.0", -2.0, root, 1e-6);
    }

    /**
     * Test with incorrect method.
     *
     * @throws UnsupportedOperationException if unsupported method is used
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIncorrectMethod() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-10;
        final Method method = Method.SECANT;
        final BaseSecantSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver<>(relativeAccuracy, absoluteAccuracy, method);
        solver.solve(100, FUNCTION, -5.0, 5.0, 0.0);
    }

    /**
     * Test with function that has no root in the interval.
     *
     * @throws NoBracketingException if root is not bracketed
     */
    @Test(expected = NoBracketingException.class)
    public void testNoRoot() throws NoBracketingException {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-10;
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };
        final BaseSecantSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver<>(relativeAccuracy, absoluteAccuracy, Method.REGULA_FALSI) {
                    @Override
                    public BracketedUnivariateRealSolver<UnivariateRealFunction>
                            createSolverInstance(double absoluteAccuracy,
                                                Method method) {
                        return new RegulaFalsiSolver<>(absoluteAccuracy);
                    }
                };
        solver.solve(100, function, -5.0, 5.0, 0.0);
    }
}
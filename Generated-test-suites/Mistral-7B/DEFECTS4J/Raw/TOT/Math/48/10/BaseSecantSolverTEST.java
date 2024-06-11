package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test Regula Falsi method with a simple quadratic function.
     */
    @Test
    public void testRegulaFalsi() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double relativeAccuracy = 1e-8;
        final double absoluteAccuracy = 1e-8;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                            absoluteAccuracy,
                                                            Method.REGULA_FALSI);

        try {
            final double root = solver.solve(100, function, -5.0, 5.0);
            assertEquals(0.0, root, 1e-9);
        } catch (final RootNotFoundException e) {
            // The function has a root between -5 and 5, so an exception
            // should not be thrown.
            fail();
        }

        try {
            final double root = solver.solve(100, function, -5.0, 3.0);
            fail("Expected RootNotFoundException");
        } catch (final RootNotFoundException e) {
            // The function has no root between -5 and 3, so an exception
            // should be thrown.
        }

        try {
            final double root = solver.solve(100, function, -3.0, 5.0);
            assertEquals(0.0, root, 1e-9);
        } catch (final MaxEvalException e) {
            // The maximum number of evaluations was not reached, so an
            // exception should not be thrown.
            fail();
        }

        try {
            final double root = solver.solve(5, function, -5.0, 5.0);
            fail("Expected NoBracketingException");
        } catch (final NoBracketingException e) {
            // The initial interval does not bracket the root, so an
            // exception should be thrown.
        }

        try {
            final double root = solver.solve(5, function, 3.0, 5.0);
            fail("Expected NoBracketingException");
        } catch (final NoBracketingException e) {
            // The initial interval does not bracket the root, so an
            // exception should be thrown.
        }

        try {
            final double root = solver.solve(5, function, -5.0, 3.0);
            fail("Expected NoBracketingException");
        } catch (final NoBracketingException e) {
            // The initial interval does not bracket the root, so an
            // exception should be thrown.
        }

        try {
            final double root = solver.solve(5, function, 2.0, 4.0);
            assertEquals(0.0, root, 1e-9);
        } catch (final MaxEvalException e) {
            // The maximum number of evaluations was not reached, but the
            // root has been found, so the exception is not a problem.
        }

        try {
            final double root = solver.solve(5, function, 1.0, 4.0);
            fail("Expected RootNotFoundException");
        } catch (final RootNotFoundException e) {
            // The function has no root between 1 and 4, so an exception
            // should be thrown.
        }
    }

    /**
     * Test Illinois method with a simple quadratic function.
     */
    @Test
    public void testIllinois() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double relativeAccuracy = 1e-8;
        final double absoluteAccuracy = 1e-8;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                            absoluteAccuracy,
                                                            Method.ILLINOIS);

        try {
            final double root = solver.solve(100, function, -5.0, 5.0);
            assertEquals(0.0, root, 1e-9);
        } catch (final RootNotFoundException e) {
            // The function has a root between -5 and 5, so an exception
            // should not be thrown.
            fail();
        }

        try {
            final double root = solver.solve(100, function, -5.0, 3.0);
            fail("Expected RootNotFoundException");
        } catch (final RootNotFoundException e) {
            // The function has no root between -5 and 3, so an exception
            // should be thrown.
        }

        try {
            final double root = solver.solve(100, function, -3.0, 5.0);
            assertEquals(0.0, root, 1e-9);
        } catch (final MaxEvalException e) {
            // The maximum number of evaluations was not reached, so an
            // exception should not be thrown.
            fail();
        }

        try {
            final double root = solver.solve(5, function, -5.0, 5.0);
            fail("Expected NoBracketingException");
        } catch (final NoBracketingException e) {
            // The initial interval does not bracket the root, so an
            // exception should be thrown.
        }

        try {
            final double root = solver.solve(5, function, 3.0, 5.0);
            fail("Expected NoBracketingException");
        } catch (final NoBracketingException e) {
            // The initial interval does not bracket the root, so an
            // exception should be thrown.
        }

        try {
            final double root = solver.solve(5, function, -5.0, 3.0);
            fail("Expected NoBracketingException");
        } catch (final NoBracketingException e) {
            // The initial interval does not bracket the root, so an
            // exception should be thrown.
        }

        try {
            final double root = solver.solve(5, function, 2.0, 4.0);
            assertEquals(0.0, root, 1e-9);
        } catch (final MaxEvalException e) {
            // The maximum number of evaluations was not reached, but the
            // root has been found, so the exception is not a problem.
        }

        try {
            final double root = solver.solve(5, function, 1.0, 4.0);
            fail("Expected RootNotFoundException");
        } catch (final RootNotFoundException e) {
            // The function has no root between 1 and 4, so an exception
            // should be thrown.
        }

        // Test exception when absolute accuracy is too large.
        final double largeAbsoluteAccuracy = Double.MAX_VALUE;
        try {
            final BaseSecantSolver solver2 = new BaseSecantSolver(relativeAccuracy,
                                                                largeAbsoluteAccuracy,
                                                                Method.ILLINOIS);
            final double root = solver2.solve(100, function, -5.0, 5.0);
            fail("Expected MathIllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // The absolute accuracy is too large, so an exception should be
            // thrown.
        }
    }

    /**
     * Test Pegasus method with a simple quadratic function.
     */
    @Test
    public void testPegasus() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double relativeAccuracy = 1e-8;
        final double absoluteAccuracy = 1e-8;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                            absoluteAccuracy,
                                                            Method.PEGASUS);

        try {
            final double root = solver.solve(100, function, -5.0, 5.0);
            assertEquals(0.0, root, 1e-9);
        } catch (final RootNotFoundException e) {
            // The function has a root between -5 and 5, so an exception
            // should not be thrown.
            fail();
        }

        try {
            final double root = solver.solve(100, function, -5.0, 3.0);
            fail("Expected RootNotFoundException");
        } catch (final RootNotFoundException e) {
            // The function has no root between -5 and 3, so an exception
            // should be thrown.
        }

        try {
            final double root = solver.solve(100, function, -3.0, 5.0);
            assertEquals(0.0, root, 1e-9);
        } catch (final MaxEvalException e) {
            // The maximum number of evaluations was not reached, so an
            // exception should not be thrown.
            fail();
        }

        try {
            final double root = solver.solve(5, function, -5.0, 5.0);
            fail("Expected NoBracketingException");
        } catch (final NoBracketingException e) {
            // The initial interval does not bracket the root, so an
            // exception should be thrown.
        }

        try {
            final double root = solver.solve(5, function, 3.0, 5.0);
            fail("Expected NoBracketingException");
        } catch (final NoBracketingException e) {
            // The initial interval does not bracket the root, so an
            // exception should be thrown.
        }

        try {
            final double root = solver.solve(5, function, -5.0, 3.0);
            fail("Expected NoBracketingException");
        } catch (final NoBracketingException e) {
            // The initial interval does not bracket the root, so an
            // exception should be thrown.
        }

        try {
            final double root = solver.solve(5, function, 2.0, 4.0);
            assertEquals(0.0, root, 1e-9);
        } catch (final MaxEvalException e) {
            // The maximum number of evaluations was not reached, but the
            // root has been found, so the exception is not a problem.
        }

        try {
            final double root = solver.solve(5, function, 1.0, 4.0);
            fail("Expected RootNotFoundException");
        } catch (final RootNotFoundException e) {
            // The function has no root between 1 and 4, so an exception
            // should be thrown.
        }

        // Test exception when absolute accuracy is too large.
        final double largeAbsoluteAccuracy = Double.MAX_VALUE;
        try {
            final BaseSecantSolver solver2 = new BaseSecantSolver(relativeAccuracy,
                                                                absoluteAccuracy,
                                                                largeAbsoluteAccuracy,
                                                                Method.PEGASUS);
            final double root = solver2.solve(100, function, -5.0, 5.0);
            fail("Expected MathIllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // The absolute accuracy is too large, so an exception should be
            // thrown.
        }

        // Test exception when relative accuracy is too large.
        final double largeRelativeAccuracy = 1e10;
        try {
            final BaseSecantSolver solver2 = new BaseSecantSolver(largeRelativeAccuracy,
                                                                absoluteAccuracy,
                                                                Method.PEGASUS);
            final double root = solver2.solve(100, function, -5.0, 5.0);
            fail("Expected MathIllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // The relative accuracy is too large, so an exception should be
            // thrown.
        }
    }

    /**
     * Test exception when function is not a BracketedUnivariateRealFunction.
     */
    @Test
    public void testExceptionOnNonBracketedFunction() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return x * x;
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        };
        final double relativeAccuracy = 1e-8;
        final double absoluteAccuracy = 1e-8;
        try {
            new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.PEGASUS);
            fail("Expected IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // The function does not implement the BracketedUnivariateRealFunction
            // interface, so an exception should be thrown.
        }
    }

    /**
     * Test exception when allowed solution is not allowed.
     */
    @Test
    public void testExceptionOnInvalidAllowedSolution() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double relativeAccuracy = 1e-8;
        final double absoluteAccuracy = 1e-8;
        try {
            new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.PEGASUS,
                                AllowedSolution.CENTER);
            fail("Expected IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // The allowed solution is not allowed, so an exception should be
            // thrown.
        }
    }
}
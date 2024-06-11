package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.SimpleBracketedUnivariateSolver;
import org.apache.commons.math.analysis.solvers.UnivariateSolver;
import org.apache.commons.math.analysis.solvers.UnivariateSolverCollection;

/**
 * Test {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test {@link BaseSecantSolver} with {@link RegulaFalsiSolver}.
     */
    @Test
    public void testRegulaFalsi() {
        // Test with a simple polynomial function.
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0});
        BracketedUnivariateRealSolver<? extends UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6);
        UnivariateSolver result = solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, result.getValue(), 1e-7);
        Assert.assertTrue(result.isBracketing());

        // Test with a function with roots at infinity.
        f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        try {
            solver = new RegulaFalsiSolver(1e-6);
            result = solver.solve(f, -10.0, 10.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with a function that doesn't bracket a root.
        f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        try {
            solver = new RegulaFalsiSolver(1e-6);
            result = solver.solve(f, 0.0, 0.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with a function that has no solution in the given interval.
        f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 1.0;
            }
        };
        try {
            solver = new RegulaFalsiSolver(1e-6);
            result = solver.solve(f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (MaxEvalException e) {
            // Expected
        }

        // Test with a function that has multiple roots.
        f = new PolynomialFunction(new double[]{-1.0, 4.0, 1.0, -2.0});
        result = solver.solve(f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0, result.getValue(), 1e-7);
        Assert.assertTrue(result.isBracketing());
    }

    /**
     * Test {@link BaseSecantSolver} with {@link IllinoisSolver}.
     */
    @Test
    public void testIllinois() {
        // Test with a simple polynomial function.
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0});
        BracketedUnivariateRealSolver<? extends UnivariateRealFunction> solver = new IllinoisSolver(1e-6);
        UnivariateSolver result = solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, result.getValue(), 1e-7);
        Assert.assertTrue(result.isBracketing());

        // Test with a function that doesn't bracket a root.
        f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        try {
            solver = new IllinoisSolver(1e-6);
            result = solver.solve(f, 0.0, 0.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with a function that has no solution in the given interval.
        f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 1.0;
            }
        };
        try {
            solver = new IllinoisSolver(1e-6);
            result = solver.solve(f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (MaxEvalException e) {
            // Expected
        }

        // Test with a function that has multiple roots.
        f = new PolynomialFunction(new double[]{-1.0, 4.0, 1.0, -2.0});
        result = solver.solve(f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0, result.getValue(), 1e-7);
        Assert.assertTrue(result.isBracketing());
    }

    /**
     * Test {@link BaseSecantSolver} with {@link PegasusSolver}.
     */
    @Test
    public void testPegasus() {
        // Test with a simple polynomial function.
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0});
        BracketedUnivariateRealSolver<? extends UnivariateRealFunction> solver = new PegasusSolver(1e-6, 1e-6, 1e-6);
        UnivariateSolver result = solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, result.getValue(), 1e-7);
        Assert.assertTrue(result.isBracketing());

        // Test with a function that doesn't bracket a root.
        f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        try {
            solver = new PegasusSolver(1e-6, 1e-6, 1e-6);
            result = solver.solve(f, 0.0, 0.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with a function that has no solution in the given interval.
        f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 1.0;
            }
        };
        try {
            solver = new PegasusSolver(1e-6, 1e-6, 1e-6);
            result = solver.solve(f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (MaxEvalException e) {
            // Expected
        }

        // Test with a function that has multiple roots.
        f = new PolynomialFunction(new double[]{-1.0, 4.0, 1.0, -2.0});
        result = solver.solve(f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0, result.getValue(), 1e-7);
        Assert.assertTrue(result.isBracketing());
    }

    /**
     * Test {@link BaseSecantSolver} with custom solver.
     */
    @Test
    public void testCustom() {
        // Test with a simple polynomial function.
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0});
        BracketedUnivariateRealSolver<? extends UnivariateRealFunction> solver = new SimpleBracketedUnivariateSolver() {
            @Override
            public double solve(UnivariateFunction f, double min, double max,
                              double initialGuess, AllowedSolution allowedSolution) {
                // Custom implementation of the solver.
                double x0 = min;
                double x1 = max;
                double f0 = f.value(x0);
                double f1 = f.value(x1);

                if (f0 == 0.0) {
                    return new SimpleBracketedUnivariateSolver.BracketedUnivariateRealSolution(x0, 0.0, 0.0);
                }

                if (f1 == 0.0) {
                    return new SimpleBracketedUnivariateSolver.BracketedUnivariateRealSolution(x1, 0.0, 0.0);
                }

                if (f0 * f1 < 0) {
                    // Bracketing is valid.
                    double x = x1;
                    double fx = f.value(x);

                    if (fx == 0.0) {
                        return new SimpleBracketedUnivariateSolver.BracketedUnivariateRealSolution(x, 0.0, 0.0);
                    }

                    if (Math.abs(fx) <= getFunctionValueAccuracy()) {
                        // Function value is small enough.
                        return new SimpleBracketedUnivariateSolver.BracketedUnivariateRealSolution(x, fx, 0.0);
                    }

                    if (Math.abs(x1 - x0) < getAbsoluteAccuracy()) {
                        // Interval is small enough.
                        return new SimpleBracketedUnivariateSolver.BracketedUnivariateRealSolution(x, fx, 0.0);
                    }

                    double a = 0.5 * (x0 + x1);
                    double fa = f.value(a);

                    if (Math.signum(fa) == Math.signum(f0)) {
                        // Change the interval.
                        x0 = a;
                        f0 = fa;
                    } else {
                        x1 = a;
                        f1 = fa;
                    }
                } else {
                    throw new UnsupportedOperationException("Function value at initial guess is zero.");
                }

                return solve(f, min, max, initialGuess, allowedSolution);
            }
        };
        UnivariateSolver result = solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, result.getValue(), 1e-7);
        Assert.assertTrue(result.isBracketing());
    }
}
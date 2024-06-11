package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        double functionValue = 0;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                functionValue = x * x;
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(maxEval, f, min, max, 0);
            }
        };

        try {
            double root = solver.solve(10, f, -10, 10);
            Assert.assertEquals(2.0, root, 1e-6);
            Assert.assertEquals(-2.0, -root, 1e-6);
        } catch (NoBracketingException e) {
            // This should not happen as the bracketing is guaranteed by the
            // constructor.
            Assert.fail("Expected bracketing but found none.");
        } catch (MaxEvalException e) {
            // This should not happen as the root is found within 10 iterations.
            Assert.fail("Max iterations exceeded.");
        } catch (ConvergenceException e) {
            // This is expected as the algorithm converges to the root.
            // The message says what we want: "Root found".
            Assert.assertEquals("Root found", e.getMessage());
        }
    }

    @Test
    public void testIllinoisSolver() {
        double functionValue = 0;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                functionValue = x * x;
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(maxEval, f, min, max, 0);
            }
        };

        try {
            double root = solver.solve(10, f, -10, 10);
            Assert.assertEquals(2.0, root, 1e-6);
            Assert.assertEquals(-2.0, -root, 1e-6);
        } catch (NoBracketingException e) {
            // This should not happen as the bracketing is guaranteed by the
            // constructor.
            Assert.fail("Expected bracketing but found none.");
        } catch (MaxEvalException e) {
            // This should not happen as the root is found within 10 iterations.
            Assert.fail("Max iterations exceeded.");
        } catch (ConvergenceException e) {
            // This is expected as the algorithm converges to the root.
            // The message says what we want: "Root found".
            Assert.assertEquals("Root found", e.getMessage());
        }
    }

    @Test
    public void testPegasusSolver() {
        double functionValue = 0;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                functionValue = x * x;
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max) {
                return super.solve(maxEval, f, min, max, 0);
            }
        };

        try {
            double root = solver.solve(10, f, -10, 10);
            Assert.assertEquals(2.0, root, 1e-6);
            Assert.assertEquals(-2.0, -root, 1e-6);
        } catch (NoBracketingException e) {
            // This should not happen as the bracketing is guaranteed by the
            // constructor.
            Assert.fail("Expected bracketing but found none.");
        } catch (MaxEvalException e) {
            // This should not happen as the root is found within 10 iterations.
            Assert.fail("Max iterations exceeded.");
        } catch (ConvergenceException e) {
            // This is expected as the algorithm converges to the root.
            // The message says what we want: "Root found".
            Assert.assertEquals("Root found", e.getMessage());
        }
    }

    @Test(expected = MathInternalError.class)
    public void testUnknownMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.UNKNOWN);
        solver.solve(10, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        }, -10, 10);
    }
}
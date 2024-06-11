package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test for the BaseSecantSolver with a simple function, f(x) = x^2 - 2.
     */
    @Test
    public void testBaseSecantSolverRootOfXSquareMinusTwo() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-4, f);
        double root = solver.solve(100, f, -5, 5);
        Assert.assertEquals("Root is incorrect.", -Math.sqrt(2), Precision.round(root, 5));

        solver = new IllinoisSolver(1e-4, f);
        root = solver.solve(100, f, -5, 5);
        Assert.assertEquals("Root is incorrect.", -Math.sqrt(2), Precision.round(root, 5));

        solver = new PegasusSolver(1e-4, f);
        root = solver.solve(100, f, -5, 5);
        Assert.assertEquals("Root is incorrect.", -Math.sqrt(2), Precision.round(root, 5));
    }

    /**
     * Test for the BaseSecantSolver with a function that has no roots.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testBaseSecantSolverFunctionWithNoRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * (x - 1);
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-4, f);
        solver.solve(100, f, -10, 10);
    }

    /**
     * Test for the BaseSecantSolver with a function that does not provide a
     * bracketing interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testBaseSecantSolverFunctionWithNoBracketing() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-4, f);
        solver.solve(100, f, -2, 2);
    }

    /**
     * Test for the BaseSecantSolver with a function that hits the maximum
     * evaluation limit.
     */
    @Test(expected = MaxEvalException.class)
    public void testBaseSecantSolverFunctionWithMaxEvaluationLimit() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x);
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-4, f);
        solver.solve(1, f, -Math.PI, Math.PI);
    }
}
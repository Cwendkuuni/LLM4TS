package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test that the optimizer correctly finds the minimum of a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction function = f -> Math.pow(x, 2) + 3 * x - 2;
        double x0 = 1.5;
        PointValuePair result = optimizer.optimize(function, x0, null);
        Assert.assertEquals(0.5, result.getPoint()[0], 1e-6);
        Assert.assertEquals(-1.5, result.getValue(), 1e-6);
    }

    /**
     * Test that the optimizer correctly finds the minimum of a more complex function.
     */
    @Test
    public void testComplexFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, new SimpleValueChecker(), new BrentSolver());
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2) + Math.cos(x) - 1;
        double x0 = 1.5;
        PointValuePair result = optimizer.optimize(function, x0, null);
        Assert.assertEquals(Math.PI / 2, result.getPoint()[0], 1e-6);
        Assert.assertEquals(-1.25 * Math.sqrt(2) / 2, result.getValue(), 1e-6);
    }

    /**
     * Test that the optimizer correctly throws an exception when it cannot find a bracket.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction function = x -> Math.pow(x, 2) - 3 * x + 2;
        double x0 = 1.0;
        optimizer.optimize(function, x0, null);
    }

    /**
     * Test that the optimizer correctly throws an exception when it does not converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction function = x -> Math.pow(x, 2) + Math.cos(x);
        double x0 = 1.0;
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(function, x0, null);
    }

    /**
     * Test that the optimizer correctly throws an exception when the line search solver cannot find a root.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction function = x -> Math.sqrt(Math.abs(x));
        double x0 = 1.0;
        optimizer.optimize(function, x0, null);
    }

    /**
     * Test that the optimizer correctly handles a null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.optimize(null, 0.0, null);
    }

    /**
     * Test that the optimizer correctly handles a null starting point.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullStartingPoint() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        }, null, null);
    }

    /**
     * Test that the optimizer correctly handles a null checker.
     */
    @Test
    public void testNullChecker() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction function = x -> Math.pow(x, 2);
        double x0 = 0.0;
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(function, x0, null);
    }

    /**
     * Test that the optimizer correctly handles a null line search solver.
     */
    @Test
    public void testNullLineSearchSolver() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction function = x -> Math.pow(x, 2);
        double x0 = 0.0;
        optimizer.setSolver(null);
        optimizer.optimize(function, x0, null);
    }

    /**
     * Test that the optimizer correctly handles a null preconditioner.
     */
    @Test
    public void testNullPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction function = x -> Math.pow(x, 2);
        double x0 = 0.0;
        optimizer.setPreconditioner(null);
        optimizer.optimize(function, x0, null);
    }

    /**
     * Test that the optimizer correctly handles a misconfigured optimizer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer("FLETCHER_REEVES_WRONG");
    }
}
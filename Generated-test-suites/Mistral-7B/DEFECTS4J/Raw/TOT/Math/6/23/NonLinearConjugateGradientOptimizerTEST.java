package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtIterativeSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test with a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        final UnivariateFunction f = new QuadraticFunction(-1.0, 1.0, 1.0);
        final OptimizationProblem problem = new OptimizationProblem(f, new PointValuePair(0.0, 0.0));

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.FLETCHER_REEVES,
            new SimpleValueChecker(),
            new BrentSolver());

        final PointValuePair result = optimizer.optimize(problem);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test with a more complex function.
     */
    @Test
    public void testComplexFunction() {
        final UnivariateFunction f = new ComplexFunction();
        final OptimizationProblem problem = new OptimizationProblem(f, new PointValuePair(0.0, 0.0));

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.POLAK_RIBIERE,
            new SimpleValueChecker(),
            new LevenbergMarquardtIterativeSolver());

        final PointValuePair result = optimizer.optimize(problem);
        Assert.assertEquals(1.5, result.getPoint()[0], 1e-6);
        Assert.assertEquals(1.25, result.getValue(), 1e-6);
    }

    /**
     * Test with a non-convergent function.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testNonConvergentFunction() {
        final UnivariateFunction f = new NonConvergentFunction();
        final OptimizationProblem problem = new OptimizationProblem(f, new PointValuePair(0.0, 0.0));

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.FLETCHER_REEVES,
            new SimpleValueChecker(),
            new BrentSolver());

        optimizer.optimize(problem);
    }

    /**
     * Test with a non-differentiable function.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNonDifferentiableFunction() {
        final UnivariateFunction f = new NonDifferentiableFunction();
        final OptimizationProblem problem = new OptimizationProblem(f, new PointValuePair(0.0, 0.0));

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
            ConjugateGradientFormula.FLETCHER_REEVES,
            new SimpleValueChecker(),
            new BrentSolver());

        optimizer.optimize(problem);
    }
}

class QuadraticFunction implements UnivariateFunction {

    private final double a;
    private final double b;
    private final double c;

    /**
     * Constructor.
     * @param a coefficient a.
     * @param b coefficient b.
     * @param c constant term c.
     */
    public QuadraticFunction(final double a, final double b, final double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /** {@inheritDoc} */
    public double value(double x) {
        return a * x * x + b * x + c;
    }
}

class ComplexFunction implements UnivariateFunction {

    /** {@inheritDoc} */
    public double value(double x) {
        return Math.sin(x) / x;
    }
}

class NonConvergentFunction implements UnivariateFunction {

    /** {@inheritDoc} */
    public double value(double x) {
        return Math.random();
    }
}

class NonDifferentiableFunction implements UnivariateFunction {

    /** {@inheritDoc} */
    public double value(double x) {
        if (x >= 0) {
            return Math.pow(Math.abs(x), 0.5);
        } else {
            throw new UnsupportedOperationException("Not differentiable at x = " + x);
        }
    }
}
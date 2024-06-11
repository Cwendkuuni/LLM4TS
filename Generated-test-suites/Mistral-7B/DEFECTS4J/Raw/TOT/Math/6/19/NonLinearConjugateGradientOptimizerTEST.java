package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.IdentityPreconditioner;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Test objective function. */
    private static final UnivariateFunction TEST_FUNCTION = new TestFunction();

    /**
     * Test the optimizer with default settings.
     */
    @Test
    public void testDefaultSettings() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);

        final PointValuePair solution = optimizer.optimize(TEST_FUNCTION, 0.0);

        Assert.assertEquals(1.0, solution.getValue(), 1e-10);
        Assert.assertArrayEquals(new double[]{-1.0, 1.0}, solution.getPoint(), 1e-10);
    }

    /**
     * Test the optimizer with preconditioner.
     */
    @Test
    public void testPreconditioner() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-10),
                new BrentSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);
        optimizer.setPreconditioner(new IdentityPreconditioner());

        final PointValuePair solution = optimizer.optimize(TEST_FUNCTION, 0.0);

        Assert.assertEquals(1.0, solution.getValue(), 1e-10);
        Assert.assertArrayEquals(new double[]{-1.0, 1.0}, solution.getPoint(), 1e-10);
    }

    /**
     * Test the optimizer with Polak-Ribi&egrave;re update formula.
     */
    @Test
    public void testPolakRibiereUpdateFormula() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);

        final PointValuePair solution = optimizer.optimize(TEST_FUNCTION, 0.0);

        Assert.assertEquals(1.0, solution.getValue(), 1e-10);
        Assert.assertArrayEquals(new double[]{-1.0, 1.0}, solution.getPoint(), 1e-10);
    }

    /**
     * Test the optimizer with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new MyConvergenceChecker(),
                new BrentSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);

        final PointValuePair solution = optimizer.optimize(TEST_FUNCTION, 0.0);

        Assert.assertEquals(1.0, solution.getValue(), 1e-10);
        Assert.assertArrayEquals(new double[]{-1.0, 1.0}, solution.getPoint(), 1e-10);
    }

    /**
     * Test the optimizer with custom line search solver.
     */
    @Test
    public void testCustomLineSearchSolver() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-10),
                new MyLineSearchSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);

        final PointValuePair solution = optimizer.optimize(TEST_FUNCTION, 0.0);

        Assert.assertEquals(1.0, solution.getValue(), 1e-10);
        Assert.assertArrayEquals(new double[]{-1.0, 1.0}, solution.getPoint(), 1e-10);
    }

    /**
     * Test the optimizer with negative initial step.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testNegativeInitialStep() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-10),
                new BrentSolver());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);
        optimizer.setInitialStep(-1.0);

        optimizer.optimize(TEST_FUNCTION, 0.0);
    }

    /**
     * Test function for the optimizer.
     */
    private static class TestFunction implements UnivariateFunction {
        /** {@inheritDoc} */
        public double value(double x) {
            return Math.pow(x - 1.0, 2.0) * Math.exp(x) + Math.sin(Math.PI * x) / x;
        }

        /** {@inheritDoc} */
        public double derivative(double x) {
            final double value = value(x);
            return 2.0 * (x - 1.0) * Math.exp(x) + Math.cos(Math.PI * x) / x - value / x;
        }
    }

    /**
     * Custom convergence checker.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        /** {@inheritDoc} */
        @Override
        public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
            return (current.getValue() - previous.getValue()) < 1e-12;
        }
    }

    /**
     * Custom line search solver.
     */
    private static class MyLineSearchSolver implements UnivariateSolver {
        /** {@inheritDoc} */
        public double solve(int maxIterations, UnivariateFunction f, double x, double xMin, double xMax) {
            for (int i = 0; i < maxIterations; ++i) {
                final double xNew = x + 0.5 * Math.pow(Math.abs(xMax - x), 0.3);
                if (f.value(xNew) * f.value(x) < 0) {
                    x = xNew;
                } else {
                    return x;
                }
            }
            throw new RuntimeException("Maximum number of iterations exceeded.");
        }
    }
}
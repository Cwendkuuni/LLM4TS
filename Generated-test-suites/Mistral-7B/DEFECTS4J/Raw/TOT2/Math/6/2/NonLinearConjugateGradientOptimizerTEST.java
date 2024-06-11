package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.IdentityPreconditioner;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization with default values.
     */
    @Test
    public void testOptimizationWithDefaultValues() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) * FastMath.cos(x);
            }

            @Override
            public double derivative(double x) {
                return FastMath.sin(x) * FastMath.cos(x) + FastMath.cos(x) * FastMath.sin(x);
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        double[] startPoint = new double[] { 1.0 };
        PointValuePair result = optimizer.optimize(f, startPoint, null);

        Assert.assertEquals(0.0, result.getValue(), 1e-12);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-12);
    }

    /**
     * Test optimization with custom convergence checker.
     */
    @Test
    public void testOptimizationWithCustomConvergenceChecker() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        ConvergenceChecker<PointValuePair> myConvergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
                return current.getValue() - previous.getValue() < 0.001;
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                myConvergenceChecker,
                new BrentSolver());
        double[] startPoint = new double[] { 1.0 };
        PointValuePair result = optimizer.optimize(f, startPoint, null);

        Assert.assertEquals(0.0, result.getValue(), 1e-12);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-12);
    }

    /**
     * Test optimization with custom preconditioner.
     */
    @Test
    public void testOptimizationWithCustomPreconditioner() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        double[][] preconditionerMatrix = { { 1.5, 0.0 }, { 0.0, 2.0 } };
        Preconditioner myPreconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] preconditioned = new double[variables.length];
                for (int i = 0; i < variables.length; ++i) {
                    for (int j = 0; j < variables.length; ++j) {
                        preconditioned[i] += preconditionerMatrix[i][j] * r[j];
                    }
                }
                return preconditioned;
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                myPreconditioner);
        double[] startPoint = new double[] { 1.0 };
        PointValuePair result = optimizer.optimize(f, startPoint, null);

        Assert.assertEquals(0.0, result.getValue(), 1e-12);
        Assert.assertEquals(1.0 / FastMath.sqrt(2), result.getPoint()[0], 1e-12);
    }

    /**
     * Test optimization with custom line search solver.
     */
    @Test
    public void testOptimizationWithCustomLineSearchSolver() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        UnivariateSolver myLineSearchSolver = new UnivariateSolver() {
            @Override
            public double solve(double lowerBound, double upperBound, int maxEvaluations) {
                return lowerBound + 0.5 * (upperBound - lowerBound);
            }

            @Override
            public int getEvaluations() {
                return 1;
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                myLineSearchSolver);
        double[] startPoint = new double[] { 1.0 };
        PointValuePair result = optimizer.optimize(f, startPoint, null);

        Assert.assertEquals(0.0, result.getValue(), 1e-12);
        Assert.assertEquals(1.0 / FastMath.sqrt(2), result.getPoint()[0], 1e-12);
    }

    /**
     * Test optimization with custom update formula.
     */
    @Test
    public void testOptimizationWithCustomUpdateFormula() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 3);
            }

            @Override
            public double derivative(double x) {
                return 3 * FastMath.pow(x, 2);
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver());
        double[] startPoint = new double[] { 1.0 };
        PointValuePair result = optimizer.optimize(f, startPoint, null);

        Assert.assertEquals(0.0, result.getValue(), 1e-12);
        Assert.assertEquals(-1.0 / 3.0, result.getPoint()[0], 1e-12);
    }

    /**
     * Test optimization with custom initial step.
     */
    @Test
    public void testOptimizationWithCustomInitialStep() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        optimizer.setInitialStep(0.1);
        double[] startPoint = new double[] { 1.0 };
        PointValuePair result = optimizer.optimize(f, startPoint, null);

        Assert.assertEquals(0.0, result.getValue(), 1e-12);
        Assert.assertEquals(1.0 / FastMath.sqrt(2), result.getPoint()[0], 1e-12);
    }

    /**
     * Test optimization with incorrect input.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizationWithIncorrectInput() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException();
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        PointValuePair result = optimizer.optimize(f, null, null);
    }
}
package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtIterativeSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.StoppingCondition;
import org.apache.commons.math3.optim.convergence.IterationNumber;
import org.apache.commons.math3.optim.initialization.DifferenceInitialGuess;
import org.apache.commons.math3.optim.initialization.RandomInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.ScalarDifferentiableOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ScalarObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioners.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioners.Preconditioner;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization using {@link NonLinearConjugateGradientOptimizer} with the
     * Fletcher-Reeves update formula and {@link IdentityPreconditioner}.
     */
    @Test
    public void testFletcherReeves() {
        final ScalarObjectiveFunction function = new ScalarObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return 3 * Math.pow(point[0], 3) - 2 * Math.pow(point[0], 2) + Math.pow(point[0], 0.1) + 2;
            }

            @Override
            public double[] gradientValue(double[] point) {
                double[] gradient = new double[point.length];
                for (int i = 0; i < point.length; ++i) {
                    gradient[i] = 9 * Math.pow(point[i], 2) - 4 * point[i] + 0.1 * Math.pow(point[i], 0.9);
                }
                return gradient;
            }
        };

        final ScalarDifferentiableOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());

        optimizer.setInitialStep(0.1);

        PointValuePair result = optimizer.optimize(function, new PointValuePair(new double[] {2.0}, 6.8));
        Assert.assertEquals(0.001, result.getValue1(), 0.001);
        Assert.assertArrayEquals(new double[] {-1.0}, result.getValue2(), 0.001);
    }

    /**
     * Test optimization using {@link NonLinearConjugateGradientOptimizer} with the
     * Polak-Ribière update formula and {@link IdentityPreconditioner}.
     */
    @Test
    public void testPolakRibiere() {
        final ScalarObjectiveFunction function = new ScalarObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2) + Math.pow(point[1] - 1.0, 2);
            }

            @Override
            public double[] gradientValue(double[] point) {
                double[] gradient = new double[point.length];
                gradient[0] = 2 * (point[0] - 1.0);
                gradient[1] = 2 * (point[1] - 1.0);
                return gradient;
            }
        };

        final ScalarDifferentiableOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver());

        optimizer.setInitialStep(0.1);

        PointValuePair result = optimizer.optimize(function, new PointValuePair(new double[] {1.5, 1.5}, 3.5));
        Assert.assertEquals(0.0, result.getValue1(), 0.001);
        Assert.assertArrayEquals(new double[] {1.0, 1.0}, result.getValue2(), 0.001);
    }

    /**
     * Test optimization using {@link NonLinearConjugateGradientOptimizer} with the
     * Fletcher-Reeves update formula and {@link IdentityPreconditioner}.
     */
    @Test
    public void testFletcherReevesWithInitialGuess() {
        final ScalarObjectiveFunction function = new ScalarObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return 3 * Math.pow(point[0], 3) - 2 * Math.pow(point[0], 2) + Math.pow(point[0], 0.1) + 2;
            }

            @Override
            public double[] gradientValue(double[] point) {
                double[] gradient = new double[point.length];
                for (int i = 0; i < point.length; ++i) {
                    gradient[i] = 9 * Math.pow(point[i], 2) - 4 * point[i] + 0.1 * Math.pow(point[i], 0.9);
                }
                return gradient;
            }
        };

        final ScalarDifferentiableOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());

        optimizer.setInitialStep(0.1);

        InitialGuess initialGuess = new DifferenceInitialGuess(new double[] {2.0, 2.0}, new double[] {-1.0, -1.0});
        PointValuePair result = optimizer.optimize(function, initialGuess);
        Assert.assertEquals(0.001, result.getValue1(), 0.001);
        Assert.assertArrayEquals(new double[] {-1.0, -1.0}, result.getValue2(), 0.001);
    }

    /**
     * Test optimization using {@link NonLinearConjugateGradientOptimizer} with the
     * Fletcher-Reeves update formula, {@link IdentityPreconditioner}, and custom
     * solver and convergence checker.
     */
    @Test
    public void testFletcherReevesCustomSolverAndChecker() {
        final ScalarObjectiveFunction function = new ScalarObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2) + Math.pow(point[1] - 1.0, 2);
            }

            @Override
            public double[] gradientValue(double[] point) {
                double[] gradient = new double[point.length];
                gradient[0] = 2 * (point[0] - 1.0);
                gradient[1] = 2 * (point[1] - 1.0);
                return gradient;
            }
        };

        final ScalarDifferentiableOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new LevenbergMarquardtIterativeSolver(),
                new IterationNumber(100),
                new ConvergenceChecker() {
                    @Override
                    public boolean converged(int iterationNumber, PointValuePair current, PointValuePair previous) {
                        return (iterationNumber > 50) || (Math.abs(current.getValue1() - previous.getValue1()) < 0.001);
                    }
                });

        optimizer.setInitialStep(0.1);

        PointValuePair result = optimizer.optimize(function, new PointValuePair(new double[] {1.5, 1.5}, 3.5));
        Assert.assertEquals(0.0, result.getValue1(), 0.001);
        Assert.assertArrayEquals(new double[] {1.0, 1.0}, result.getValue2(), 0.001);
    }

    /**
     * Test optimization using {@link NonLinearConjugateGradientOptimizer} with the
     * Fletcher-Reeves update formula, {@link IdentityPreconditioner}, and custom
     * constraints.
     */
    @Test
    public void testFletcherReevesCustomConstraints() {
        final ScalarObjectiveFunction function = new ScalarObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2) + Math.pow(point[1] - 1.0, 2);
            }

            @Override
            public double[] gradientValue(double[] point) {
                double[] gradient = new double[point.length];
                gradient[0] = 2 * (point[0] - 1.0);
                gradient[1] = 2 * (point[1] - 1.0);
                return gradient;
            }
        };

        final ScalarDifferentiableOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());

        optimizer.setInitialStep(0.1);

        final LinearConstraint linearConstraint = new LinearConstraint(new double[] {-1.0}, new double[] {1.0}, 1.0);
        final NonlinearConstraint nonlinearConstraint = new NonlinearConstraint(point -> Math.pow(point[0], 2) + Math.pow(point[1], 2) - 1.0, 1e-6, 1e-6);

        PointValuePair result = optimizer.optimize(function, new PointValuePair(new double[] {1.5, 1.5}, 3.5), new GoalType[] {GoalType.MINIMIZE}, new Constraint[] {linearConstraint, nonlinearConstraint});
        Assert.assertEquals(0.0, result.getValue1(), 0.001);
        Assert.assertArrayEquals(new double[] {0.5, 0.5}, result.getValue2(), 0.001);
    }

    /**
     * Test optimization using {@link NonLinearConjugateGradientOptimizer} with the
     * Fletcher-Reeves update formula, {@link IdentityPreconditioner}, and error
     * handling.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testFletcherReevesErrorHandling() {
        final ScalarObjectiveFunction function = new ScalarObjectiveFunction() {
            @Override
            public double value(double[] point) {
                throw new UnsupportedOperationException("This method is not supported.");
            }

            @Override
            public double[] gradientValue(double[] point) {
                throw new UnsupportedOperationException("This method is not supported.");
            }
        };

        final ScalarDifferentiableOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());

        optimizer.optimize(function, new PointValuePair(new double[] {0.0}, 0.0));
    }
}
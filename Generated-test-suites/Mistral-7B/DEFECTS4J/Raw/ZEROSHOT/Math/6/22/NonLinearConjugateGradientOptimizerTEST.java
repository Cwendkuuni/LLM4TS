package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.convergence.ConvergenceChecker;
import org.apache.commons.math3.optimization.convergence.IterationChecker;
import org.apache.commons.math3.optimization.nonlinear.NonLinearConstraint;
import org.apache.commons.math3.optimization.nonlinear.NonLinearConstraintSet;
import org.apache.commons.math3.optimization.nonlinear.NonLinearObjectiveFunction;
import org.apache.commons.math3.optimization.nonlinear.NonLinearPreconditioner;
import org.apache.commons.math3.optimization.nonlinear.NonLinearSolver;
import org.apache.commons.math3.optimization.nonlinear.NonLinearSolverStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimizer() {
        NonLinearObjectiveFunction function = new NonLinearObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[0] + point[1] * point[1] - 1, 2);
            }

            @Override
            public double[] gradient(double[] point) {
                double[] gradient = new double[2];
                gradient[0] = 4 * point[0] * (point[0] * point[0] + point[1] * point[1] - 1);
                gradient[1] = 4 * point[1] * (point[0] * point[0] + point[1] * point[1] - 1);
                return gradient;
            }
        };

        NonLinearConstraint[] constraints = new NonLinearConstraint[] {};
        NonLinearConstraintSet constraintSet = new NonLinearConstraintSet(constraints);

        NonLinearSolver optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());

        PointValuePair solution = optimizer.optimize(function, new InitialGuess[] {new InitialGuess(0.1, 0.1)}, constraintSet);

        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] {-1.0, -1.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizerException() {
        NonLinearObjectiveFunction function = new NonLinearObjectiveFunction() {
            @Override
            public double value(double[] point) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] gradient(double[] point) {
                return new double[] {};
            }
        };

        NonLinearConstraint[] constraints = new NonLinearConstraint[] {};
        NonLinearConstraintSet constraintSet = new NonLinearConstraintSet(constraints);

        NonLinearSolver optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.optimize(function, new InitialGuess[] {}, constraintSet);
    }

    @Test
    public void testOptimizerWithPreconditioner() {
        NonLinearObjectiveFunction function = new NonLinearObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[0] + point[1] * point[1] - 1, 2) + Math.pow(point[0] * point[0], 0.5);
            }

            @Override
            public double[] gradient(double[] point) {
                double[] gradient = new double[2];
                gradient[0] = 4 * point[0] * (point[0] * point[0] + point[1] * point[1] - 1) + 0.5 * Math.pow(point[0], 0.5);
                gradient[1] = 4 * point[1] * (point[0] * point[0] + point[1] * point[1] - 1);
                return gradient;
            }
        };

        NonLinearConstraint[] constraints = new NonLinearConstraint[] {};
        NonLinearConstraintSet constraintSet = new NonLinearConstraintSet(constraints);

        NonLinearSolver optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        double[] preconditioned = new double[2];
                        preconditioned[0] = r[0] / (2 * variables[0]);
                        preconditioned[1] = r[1];
                        return preconditioned;
                    }
                });

        PointValuePair solution = optimizer.optimize(function, new InitialGuess[] {new InitialGuess(0.1, 0.1)}, constraintSet);

        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] {-0.5, -1.0}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizerIterations() {
        NonLinearObjectiveFunction function = new NonLinearObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[0] + point[1] * point[1] - 1, 2);
            }

            @Override
            public double[] gradient(double[] point) {
                double[] gradient = new double[2];
                gradient[0] = 4 * point[0] * (point[0] * point[0] + point[1] * point[1] - 1);
                gradient[1] = 4 * point[1] * (point[0] * point[0] + point[1] * point[1] - 1);
                return gradient;
            }
        };

        NonLinearConstraint[] constraints = new NonLinearConstraint[] {};
        NonLinearConstraintSet constraintSet = new NonLinearConstraintSet(constraints);

        ConvergenceChecker convergenceChecker = new IterationChecker(100);

        NonLinearSolver optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.setConvergenceChecker(convergenceChecker);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess[] {new InitialGuess(0.1, 0.1)}, constraintSet);

        Assert.assertEquals(NonLinearSolverStatus.OPTIMAL, optimizer.getStatus());
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] {-1.0, -1.0}, solution.getPoint(), 1e-6);

        Assert.assertEquals(10, convergenceChecker.getIterationNumber());
    }

    @Test
    public void testOptimizerMaxIterations() {
        NonLinearObjectiveFunction function = new NonLinearObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[0] + point[1] * point[1] - 1, 2);
            }

            @Override
            public double[] gradient(double[] point) {
                double[] gradient = new double[2];
                gradient[0] = 4 * point[0] * (point[0] * point[0] + point[1] * point[1] - 1);
                gradient[1] = 4 * point[1] * (point[0] * point[0] + point[1] * point[1] - 1);
                return gradient;
            }
        };

        NonLinearConstraint[] constraints = new NonLinearConstraint[] {};
        NonLinearConstraintSet constraintSet = new NonLinearConstraintSet(constraints);

        ConvergenceChecker convergenceChecker = new IterationChecker(5);

        NonLinearSolver optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.setConvergenceChecker(convergenceChecker);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess[] {new InitialGuess(0.1, 0.1)}, constraintSet);

        Assert.assertEquals(NonLinearSolverStatus.ITERATIONS_REACHED_MAX, optimizer.getStatus());
        Assert.assertNull(solution);
    }

    @Test
    public void testOptimizerMaxEvaluations() {
        NonLinearObjectiveFunction function = new NonLinearObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[0] + point[1] * point[1] - 1, 2);
            }

            @Override
            public double[] gradient(double[] point) {
                double[] gradient = new double[2];
                gradient[0] = 4 * point[0] * (point[0] * point[0] + point[1] * point[1] - 1);
                gradient[1] = 4 * point[1] * (point[0] * point[0] + point[1] * point[1] - 1);
                return gradient;
            }
        };

        NonLinearConstraint[] constraints = new NonLinearConstraint[] {};
        NonLinearConstraintSet constraintSet = new NonLinearConstraintSet(constraints);

        UnivariateSolver lineSearchSolver = new BrentSolver();
        lineSearchSolver.setMaxIterations(10);

        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                return Arrays.copyOf(r, r.length);
            }
        };

        NonLinearSolver optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                lineSearchSolver,
                preconditioner);

        optimizer.setMaxEvaluations(3);

        PointValuePair solution = optimizer.optimize(function, new InitialGuess[] {new InitialGuess(0.1, 0.1)}, constraintSet);

        Assert.assertEquals(NonLinearSolverStatus.MAX_EVALUATIONS_REACHED, optimizer.getStatus());
        Assert.assertNull(solution);
    }
}
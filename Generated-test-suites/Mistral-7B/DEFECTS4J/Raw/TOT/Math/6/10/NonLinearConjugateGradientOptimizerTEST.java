package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testMinimization() {
        UnivariateFunction function = new QuadraticFunction(1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver()
        ) {
            @Override
            public Preconditioner getPreconditioner() {
                return new IdentityPreconditioner();
            }
        };

        optimizer.setInitialStep(1.0);
        PointValuePair solution = optimizer.optimize(function, new PointValuePair(0, function.value(0)));
        assertEquals(0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testDivergence() {
        UnivariateFunction function = new QuadraticFunction(-1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver()
        ) {
            @Override
            public Preconditioner getPreconditioner() {
                return new IdentityPreconditioner();
            }
        };

        optimizer.setInitialStep(1.0);
        optimizer.optimize(function, new PointValuePair(0, function.value(0)));
    }

    @Test
    public void testMinimizationWithCustomPreconditioner() {
        UnivariateFunction function = new QuadraticFunction(1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver()
        ) {
            @Override
            public Preconditioner getPreconditioner() {
                return new CustomPreconditioner();
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        optimizer.setInitialStep(1.0);
        PointValuePair solution = optimizer.optimize(function, new PointValuePair(0, function.value(0)));
        assertEquals(0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testMaximization() {
        UnivariateFunction function = new QuadraticFunction(1);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver()
        ) {
            @Override
            public Preconditioner getPreconditioner() {
                return new IdentityPreconditioner();
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };

        optimizer.setInitialStep(-1.0);
        PointValuePair solution = optimizer.optimize(function, new PointValuePair(0, function.value(0)));
        assertEquals(0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, function.value(solution.getPoint()), 1e-6);
    }

    static class CustomPreconditioner implements Preconditioner {
        @Override
        public double[] precondition(double[] variables, double[] r) {
            double[] preconditioned = new double[variables.length];
            for (int i = 0; i < variables.length; ++i) {
                preconditioned[i] = 2 * r[i];
            }
            return preconditioned;
        }
    }

    static class QuadraticFunction implements UnivariateFunction {
        private double a;

        public QuadraticFunction(double a) {
            this.a = a;
        }

        @Override
        public double value(double x) {
            return a * Math.pow(x, 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * a * x;
        }
    }
}
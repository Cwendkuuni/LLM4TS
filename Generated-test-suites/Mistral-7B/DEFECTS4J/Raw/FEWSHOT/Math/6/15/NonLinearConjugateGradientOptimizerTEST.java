package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.GaussianInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentSolver;
import org.apache.commons.math3.optim.preconditioner.*;
import org.apache.commons.math3.optim.point.*;
import org.apache.commons.math3.optim.simple.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    @Test
    public void testConjugateGradientOptimizer() {
        NonLinearOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        OptimizationProblem problem = new AxisAlignedOptimizationProblem(
                new ObjectiveFunction() {
                    @Override
                    public double value(Point point) {
                        double[] x = (double[]) point.getPoint();
                        return x[0] * x[0] + x[1] * x[1] - 2 * x[0] * x[1]
                                + 2 * x[0] * x[0] - x[1] * x[1];
                    }

                    @Override
                    public PointValuePair gradient(Point point) {
                        double[] x = (double[]) point.getPoint();
                        double[] grad = new double[2];
                        grad[0] = 4 * x[0] - 2 * x[1];
                        grad[1] = -2 * x[0] + 2 * x[1] - 2 * x[1];
                        return new PointValuePair(point, grad);
                    }
                },
                new Point(1.0, 1.0));

        PointValuePair solution = optimizer.optimize(problem);
        assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-12);
        assertEquals(0.0, solution.getValue(), 1e-12);
    }

    @Test
    public void testConjugateGradientOptimizerGaussianInitializer() {
        NonLinearOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new GaussNewtonPreconditioner(2));

        OptimizationProblem problem = new AxisAlignedOptimizationProblem(
                new ObjectiveFunction() {
                    @Override
                    public double value(Point point) {
                        double[] x = (double[]) point.getPoint();
                        return Math.pow(x[0] * x[0] + x[1] * x[1], 2);
                    }

                    @Override
                    public PointValuePair gradient(Point point) {
                        double[] x = (double[]) point.getPoint();
                        double[] grad = new double[2];
                        grad[0] = 2 * x[0] * (2 * x[0] * x[0] + 2 * x[1] * x[1]);
                        grad[1] = 2 * x[1] * (2 * x[0] * x[0] + 2 * x[1] * x[1]);
                        return new PointValuePair(point, grad);
                    }
                },
                new Point(1.0, 1.0));

        PointValuePair solution = optimizer.optimize(problem,
                new AroundMinimumInitializer(new Point(0.5, 0.5), 0.1));

        assertArrayEquals(new double[]{-0.5, -0.5}, solution.getPoint(), 1e-6);
        // The exact solution is not known, but we expect a decrease in
        // the value function
        assertTrue(Precision.abs(problem.getValue(solution.getPoint())
                < problem.getValue(new Point(1.0, 1.0))));
    }
}
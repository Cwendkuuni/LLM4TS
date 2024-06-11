package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test with a simple problem with known solution
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public double[] getFunction(double[] x) {
                return new double[] {Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 3.0, 2.0)};
            }

            @Override
            public double[] getGradient(double[] x) {
                return new double[] {-4.0 * (x[0] - 2.0) + 0.0 * (x[1] - 3.0),
                        0.0 * (x[0] - 2.0) + -4.0 * (x[1] - 3.0)};
            }

            @Override
            public double getMinValue() {
                return 4.0;
            }

            @Override
            public PointValuePair getStartPoint() {
                return new PointValuePair(new double[]{4.0, 4.0}, 20.0);
            }
        };

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        PointValuePair solution = optimizer.optimize(problem);
        double[] solutionValues = solution.getPoint();
        assertEquals(new double[]{2.0, 3.0}, solutionValues, 1e-6);

        // Test with a problem where the Jacobian is not defined at the starting point
        UnconstrainedOptimizationProblem problem2 = new UnconstrainedOptimizationProblem() {
            @Override
            public double[] getFunction(double[] x) {
                return new double[] {Math.log(Math.abs(x[0]))};
            }

            @Override
            public double[] getGradient(double[] x) {
                if (x[0] <= 0.0) {
                    throw new RuntimeException("Jacobian not defined");
                }
                return new double[]{1.0 / x[0]};
            }

            @Override
            public double getMinValue() {
                return 0.0;
            }

            @Override
            public PointValuePair getStartPoint() {
                return new PointValuePair(new double[]{-1.0}, 0.0);
            }
        };

        try {
            optimizer.optimize(problem2);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with a problem with multiple solutions
        UnconstrainedOptimizationProblem problem3 = new UnconstrainedOptimizationProblem() {
            @Override
            public double[] getFunction(double[] x) {
                return new double[] {Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0)};
            }

            @Override
            public double[] getGradient(double[] x) {
                return new double[] {-2.0 * (x[0] - 1.0) + 0.0 * (x[1] - 1.0),
                        0.0 * (x[0] - 1.0) + -2.0 * (x[1] - 1.0)};
            }

            @Override
            public double getMinValue() {
                return 1.0;
            }

            @Override
            public PointValuePair getStartPoint() {
                return new PointValuePair(new double[]{1.5, 1.5}, 2.0);
            }
        };

        optimizer.setMaxIterations(10);
        PointValuePair solution3 = optimizer.optimize(problem3);
        double[] solutionValues3 = solution3.getPoint();
        // One of the solutions should be found
        assertEquals(new double[]{1.0, 1.0}, solutionValues3, 1e-6);
        // The other solution should not be found within the maximum number of iterations
        assertEquals(new double[]{1.0, 1.0}, problem3.getStartPoint().getPoint(), 1e-6);

        // Test with a problem where the Jacobian is singular
        UnconstrainedOptimizationProblem problem4 = new UnconstrainedOptimizationProblem() {
            @Override
            public double[] getFunction(double[] x) {
                return new double[] {Math.pow(x[0], 2.0) + Math.pow(x[1], 2.0)};
            }

            @Override
            public double[] getGradient(double[] x) {
                return new double[]{2.0 * x[0], 2.0 * x[1]};
            }

            @Override
            public double getMinValue() {
                return 0.0;
            }

            @Override
            public PointValuePair getStartPoint() {
                return new PointValuePair(new double[]{0.0, 0.0}, 0.0);
            }
        };

        try {
            optimizer.optimize(problem4);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

}
package org.apache.commons.math.optimization.general.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.VectorialConvergenceChecker;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math3.optim.MaxEvaluations;
import org.apache.commons.math3.optim.MaxIterations;
import org.apache.commons.math3.optim.PointValues;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.GoalTypeVector;
import org.apache.commons.math3.optim.nonlinear.vector.leastsquares.AbstractLeastSquaresOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.leastsquares.LevenbergMarquardtOptimizer;

/**
 * Tests for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest extends TestCase {

    /**
     * Tests the base class behavior when the optimization is successful.
     */
    public void testSuccessfulOptimization() {
        // Define sample function
        DifferentiableMultivariateVectorialFunction sampleFunction = new DifferentiableMultivariateVectorialFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            public double[] gradient(double[] x) {
                double[] gradient = new double[x.length];
                gradient[0] = 2 * x[0];
                gradient[1] = 2 * x[1];
                return gradient;
            }
        };

        // Set up problem data
        double[] target = new double[] {1.0, 1.0};
        double[] weights = new double[] {1.0, 1.0};
        double[] startPoint = new double[] {2.0, 2.0};

        // Set up optimizer
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(100);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0e-6, 1.0e-6));

        // Perform optimization
        PointValuePair result = optimizer.optimize(sampleFunction, target, weights, startPoint);

        // Check results
        double[] expected = new double[] {1.0, 1.0};
        PointValues expectedValues = new PointValues(GoalTypeVector.MINIMIZE, expected);
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);

        // Test error estimation
        try {
            optimizer.optimize(sampleFunction, target, weights, startPoint,
                new MaxIterations(1), new MaxEvaluations(1));
            fail("Expected MaxIterationsExceededException or MaxEvaluationsExceededException");
        } catch (MaxIterationsExceededException | MaxEvaluationsExceededException e) {
            // Expected
        }
    }

    /**
     * Tests the base class behavior when the optimization fails to converge.
     */
    public void testConvergenceFailure() {
        // Define sample function
        DifferentiableMultivariateVectorialFunction sampleFunction = new DifferentiableMultivariateVectorialFunction() {
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }

            public double[] gradient(double[] x) {
                double[] gradient = new double[x.length];
                gradient[0] = -Math.PI * Math.cos(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
                gradient[1] = -Math.PI * Math.sin(Math.PI * x[0]) * Math.cos(Math.PI * x[1]);
                return gradient;
            }
        };

        // Set up problem data
        double[] target = new double[] {0.0, 0.0};
        double[] weights = new double[] {1.0, 1.0};
        double[] startPoint = new double[] {1.0, 1.0};

        // Set up optimizer
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0e-6, 1.0e-6));

        // Test optimization
        try {
            optimizer.optimize(sampleFunction, target, weights, startPoint);
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }
    }

    /**
     * Tests the base class behavior when the optimization is interrupted by exceptions.
     */
    public void testExceptionDuringOptimization() {
        // Define sample function
        DifferentiableMultivariateVectorialFunction sampleFunction = new DifferentiableMultivariateVectorialFunction() {
            public double value(double[] x) {
                throw new FunctionEvaluationException("Function evaluation failed");
            }

            public double[] gradient(double[] x) {
                throw new FunctionEvaluationException("Function evaluation failed");
            }
        };

        // Set up problem data
        double[] target = new double[] {1.0, 1.0};
        double[] weights = new double[] {1.0, 1.0};
        double[] startPoint = new double[] {2.0, 2.0};

        // Set up optimizer
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(100);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0e-6, 1.0e-6));

        // Test optimization
        try {
            optimizer.optimize(sampleFunction, target, weights, startPoint);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    /**
     * Tests the base class behavior when the optimization is interrupted by the user.
     */
    public void testUserInterruption() {
        // Define sample function
        DifferentiableMultivariateVectorialFunction sampleFunction = new DifferentiableMultivariateVectorialFunction() {
            public double value(double[] x) {
                throw new InterruptedException();
            }

            public double[] gradient(double[] x) {
                throw new InterruptedException();
            }
        };

        // Set up problem data
        double[] target = new double[] {1.0, 1.0};
        double[] weights = new double[] {1.0, 1.0};
        double[] startPoint = new double[] {2.0, 2.0};

        // Set up optimizer
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(100);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0e-6, 1.0e-6));

        // Test optimization
        try {
            Thread thread = new Thread(() -> {
                optimizer.optimize(sampleFunction, target, weights, startPoint);
            });
            thread.start();
            thread.interrupt();
            thread.join();
        } catch (InterruptedException e) {
            // Expected
        } catch (OptimizationException e) {
            fail("Expected InterruptedException, not OptimizationException");
        }
    }

}
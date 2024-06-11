package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialFunction;
import org.apache.commons.math3.optim.SimpleVectorialGradient;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the AbstractLeastSquaresOptimizer class
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test optimization with a simple quadratic function
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testOptimizationQuadraticFunction() throws Exception {
        // Define a simple quadratic function
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorialFunction("f(x) = x[0]^2 + x[1]^2",
                new SimpleVectorialGradient("g(x) = 2*x[0] + 2*x[1]", 2));

        // Define the target values and weights
        double[] target = new double[] {1.0, 1.0};
        double[] weights = new double[] {1.0, 1.0};

        // Define the starting point
        double[] startPoint = new double[] {-1.0, -1.0};

        // Perform optimization
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        // Check results
        assertArrayEquals(new double[] {0.0, 0.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 1e-8);

        // Test the jacobian
        double[][] jacobian = optimizer.jacobian();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(0.0, jacobian[i][j], 1e-8);
            }
        }

        // Test the cost
        assertEquals(0.0, optimizer.getCost(), 1e-8);
    }

    /**
     * Test optimization with a more complex non-quadratic function
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testOptimizationComplexFunction() throws Exception {
        // Define a non-quadratic function
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorialFunction("f(x) = sin(x[0]) + exp(x[1])",
                new SimpleVectorialGradient("g(x) = cos(x[0]) + exp(x[1])", 2));

        // Define the target values and weights
        double[] target = new double[] {Math.PI/2, Math.E};
        double[] weights = new double[] {1.0, 1.0};

        // Define the starting point
        double[] startPoint = new double[] {-2.0, -2.0};

        // Perform optimization
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        PointValuePair result;

        try {
            result = optimizer.optimize(function, target, weights, startPoint);
        } catch (Exception e) {
            // Test error handling
            assertTrue(e instanceof MaxIterationsExceededException);
            assertEquals(100, e.getCause().getMessage().split(" ")[1]);
            return;
        }

        //Check results
        assertArrayEquals(new double[] {Math.PI/2, Math.E}, result.getPoint(), 1e-6);
        assertEquals(Math.PI + Math.E, result.getValue(), 1e-6);

        // Test the jacobian
        double[][] jacobian = optimizer.jacobian();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == 0 && j == 0) {
                    assertEquals(Math.cos(Math.PI/2), jacobian[i][j], 1e-6);
                } else if (i == 0 && j == 1) {
                    assertEquals(0.0, jacobian[i][j], 1e-6);
                } else if (i == 1 && j == 0) {
                    assertEquals(0.0, jacobian[i][j], 1e-6);
                } else if (i == 1 && j == 1) {
                    assertEquals(Math.E, jacobian[i][j], 1e-6);
                }
            }
        }

        // Test the cost
        assertEquals(Math.PI + Math.E, optimizer.getCost(), 1e-6);
    }

    /**
     * Test optimization with invalid input
     * @throws Exception if any error occurs during the test
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() throws Exception {
        // Define a function with a different number of dimensions
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorialFunction("f(x) = x[0]^2 + x[1]^2",
                new SimpleVectorialGradient("g(x) = 2*x[0] + 2*x[1]", 3));

        // Define the target values and weights
        double[] target = new double[] {1.0, 1.0};
        double[] weights = new double[] {1.0, 1.0};

        // Define the starting point
        double[] startPoint = new double[] {-1.0, -1.0, -1.0};

        // Perform optimization
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test optimization with an uneven number of dimensions in target and weights
     * @throws Exception if any error occurs during the test
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnevenDimensions() throws Exception {
        // Define a function with a different number of dimensions
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorialFunction("f(x) = x[0]^2 + x[1]^2",
                new SimpleVectorialGradient("g(x) = 2*x[0] + 2*x[1]", 2));

        // Define the target values and weights with a different number of dimensions
        double[] target = new double[] {1.0, 1.0, 1.0};
        double[] weights = new double[] {1.0};

        // Define the starting point
        double[] startPoint = new double[] {-1.0, -1.0};

        // Perform optimization
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test optimization with a function with a singular jacobian
     * @throws Exception if any error occurs during the test
     */
    @Test(expected = OptimizationException.class)
    public void testSingularJacobian() throws Exception {
        // Define a function with a singular jacobian
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorialFunction("f(x) = x[0]^2 + x[1]^2",
                new SimpleVectorialGradient("g(x) = new double[] {0.0, 0.0}", 2));

        // Define the target values and weights
        double[] target = new double[] {1.0, 1.0};
        double[] weights = new double[] {1.0, 1.0};

        // Define the starting point
        double[] startPoint = new double[] {-1.0, -1.0};

        // Perform optimization
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test optimization with a function that throws an exception during evaluation
     * @throws Exception if any error occurs during the test
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testExceptionDuringEvaluation() throws Exception {
        // Define a function that throws an exception during evaluation
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorialFunction("f(x) = throw new FunctionEvaluationException()",
                new SimpleVectorialGradient("g(x) = new double[] {0.0, 0.0}", 2));

        // Define the target values and weights
        double[] target = new double[] {1.0, 1.0};
        double[] weights = new double[] {1.0, 1.0};

        // Define the starting point
        double[] startPoint = new double[] {-1.0, -1.0};

        // Perform optimization
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test optimization with a function that throws an exception during jacobian evaluation
     * @throws Exception if any error occurs during the test
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testExceptionDuringJacobianEvaluation() throws Exception {
        // Define a function that throws an exception during jacobian evaluation
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorialFunction("f(x) = x[0]^2 + x[1]^2",
                new MultivariateMatrixFunction("g(x) = throw new FunctionEvaluationException()") {
                    @Override
                    public double[] value(double[] x) throws FunctionEvaluationException {
                        throw new FunctionEvaluationException();
                    }
                });

        // Define the target values and weights
        double[] target = new double[] {1.0, 1.0};
        double[] weights = new double[] {1.0, 1.0};

        // Define the starting point
        double[] startPoint = new double[] {-1.0, -1.0};

        // Perform optimization
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }
}
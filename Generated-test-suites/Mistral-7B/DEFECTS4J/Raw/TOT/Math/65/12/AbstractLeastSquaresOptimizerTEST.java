package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.VectorialConvergenceChecker;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testLinearRegressionOptimizer() {
        // Set up test data
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] initialGuess = {0.0, 0.0};

        // Define test optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // Implement your optimization algorithm here.
                // For this test case, we'll simply return initial guess as the optimized point
                return new VectorialPointValuePair(initialGuess, new double[0]);
            }
        };

        // Set up convergence checker and test settings
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6));
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);

        // Run optimization
        PointValuePair result = optimizer.optimize(new LinearRegressionFunction(x, y), y, weights, initialGuess);

        // Check optimization result
        double[] expectedCoefficients = {1.0, 1.0};
        double[] expectedCost = 0.0;
        Assert.assertArrayEquals(expectedCoefficients, result.getPoint(), 0.001);
        Assert.assertEquals(expectedCost, result.getValue(), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() {
        // Set up test data
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] initialGuess = {0.0, 0.0};

        // Define test optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // Intentionally not converging optimization algorithm
                return new VectorialPointValuePair(new double[2], new double[0]);
            }
        };

        // Set up convergence checker and test settings
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6));
        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);

        // Run optimization
        optimizer.optimize(new LinearRegressionFunction(x, y), y, weights, initialGuess);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceededException() {
        // Set up test data
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] initialGuess = {0.0, 0.0};

        // Define test optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // Optimization algorithm that evaluates the objective function excessively
                double[] point = new double[2];
                for (int i = 0; i < 10000; i++) {
                    point = optimizer.getPoint();
                    optimizer.updateResidualsAndCost();
                }
                return new VectorialPointValuePair(point, new double[0]);
            }
        };

        // Set up convergence checker and test settings
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6));
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(5);

        // Run optimization
        try {
            optimizer.optimize(new LinearRegressionFunction(x, y),
                    y, weights, initialGuess);
            Assert.fail("MaxEvaluationsExceededException not thrown");
        } catch (MaxEvaluationsExceededException mee) {
            // Expected exception
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        // Set up test data
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] initialGuess = {0.0, 0.0};

        // Define test optimizer with a function evaluation exception
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new FunctionEvaluationException(new Object[]{initialGuess},
                        "FunctionEvaluationException during optimization");
            }
        };

        // Set up convergence checker and test settings
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6));
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);

        // Run optimization
        try {
            optimizer.optimize(new LinearRegressionFunction(x, y), y, weights, initialGuess);
            Assert.fail("FunctionEvaluationException not thrown");
        } catch (FunctionEvaluationException fe) {
            // Expected exception
        }
    }
}
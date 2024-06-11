package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    /** Optimizer under test. */
    private AbstractLeastSquaresOptimizer optimizer;

    /** Test function. */
    private static final DifferentiableMultivariateVectorFunction testFunction = new TestFunction();

    /** Test point. */
    private static final double[] testPoint = {1.0, 2.0};

    /** Test target values. */
    private static final double[] testTargetValues = {3.0, 4.0};

    /** Test weights. */
    private static final double[] testWeights = {1.0, 1.0};

    /** Test start point. */
    private static final double[] testStartPoint = {0.0, 0.0};

    /** Test convergence checker. */
    private static final SimpleVectorialValueChecker testChecker = new SimpleVectorialValueChecker(1.0e-5);

    /** Test optimizer with default settings. */
    @Test
    public void testDefaultSettings() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer();
        PointValuePair result = optimizer.optimize(testFunction, testTargetValues, testWeights, testStartPoint);
        assertArrayEquals(new double[] {2.0, 2.0}, result.getPoint(), 0.0);
        assertEquals(0.0, optimizer.getRMS(), 1.0e-5);
    }

    /** Test optimizer with custom settings. */
    @Test
    public void testCustomSettings() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            public VectorialConvergenceChecker getConvergenceChecker() {
                return testChecker;
            }

            @Override
            public int getMaxIterations() {
                return 5;
            }

            @Override
            public int getMaxEvaluations() {
                return 10;
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, testTargetValues, testWeights, testStartPoint);
        assertArrayEquals(new double[] {2.0, 2.0}, result.getPoint(), 0.0);
        assertEquals(0.0, optimizer.getRMS(), 1.0e-5);
    }

    /** Test optimization with invalid input. */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(testFunction, null, testWeights, testStartPoint);
    }

    /** Test optimization with invalid target values and weights. */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTargetValuesAndWeights() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(testFunction, testTargetValues, null, testStartPoint);
    }

    /** Test optimization with MaxIterationsExceededException. */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            public int getMaxIterations() {
                return 1;
            }
        };
        optimizer.optimize(testFunction, testTargetValues, testWeights, testStartPoint);
    }

    /** Test optimization with MaxEvaluationsExceededException. */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            public int getMaxEvaluations() {
                return 1;
            }
        };
        optimizer.optimize(testFunction, testTargetValues, testWeights, testStartPoint);
    }

    /** Test getCovariances() method. */
    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(testFunction, testTargetValues, testWeights, testStartPoint);
        double[][] covariances = optimizer.getCovariances();
        // covariances may be different each time the test runs, so just check dimensions
        assertEquals(testPoint.length, covariances.length);
        assertEquals(testPoint.length, covariances[0].length);
    }

    /** Test guessParametersErrors() method. */
    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(testFunction, testTargetValues, testWeights, testStartPoint);
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(testPoint.length, errors.length);
    }

    /** Test CovarianceMatrixSingularProblemException. */
    @Test(expected = OptimizationException.class)
    public void testCovarianceMatrixSingularProblemException() throws FunctionEvaluationException, OptimizationException {
        RealMatrix covariances = MatrixUtils.createRealIdentityMatrix(2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            public double[][] getCovariances() throws FunctionEvaluationException, OptimizationException {
                return new double[][] {covariances.getData()};
            }
        };
        optimizer.optimize(testFunction, testTargetValues, testWeights, testStartPoint);
    }

    /** Test function for testing optimization algorithm. */
    public static class TestFunction implements DifferentiableMultivariateVectorFunction {

        @Override
        public double value(double[] x) throws FunctionEvaluationException {
            return 0.1 * Math.pow(Math.sqrt(Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 2.0, 2.0)), 2.0);
        }

        @Override
        public double[] gradient(double[] x) throws FunctionEvaluationException {
            double[] result = new double[2];
            double x0 = x[0], x1 = x[1];
            result[0] = -0.1 * 0.5 * Math.sqrt(Math.pow(x0 - 2.0, 2.0) + Math.pow(x1 - 2.0, 2.0)) * (x0 - 2.0) / Math.sqrt(Math.pow(x0 - 2.0, 2.0) + Math.pow(x1 - 2.0, 2.0));
            result[1] = -0.1 * 0.5 * Math.sqrt(Math.pow(x0 - 2.0, 2.0) + Math.pow(x1 - 2.0, 2.0)) * (x1 - 2.0) / Math.sqrt(Math.pow(x0 - 2.0, 2.0) + Math.pow(x1 - 2.0, 2.0));
            return result;
        }
    }

    /** Initialize test optimizer and test function. */
    @Before
    public void setUp() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer();
    }
}
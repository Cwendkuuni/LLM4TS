package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.univariate.DifferentiableUnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for the AbstractLeastSquaresOptimizer class.
 */
public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException("doOptimize not implemented");
            }
        };

        // You can set up the optimizer with your specific settings if needed
    }

    @Test
    public void testSetAndGetMaxIterations() {
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
    }

    @Test
    public void testSetAndGetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        Assert.assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testIncrementIterationsCounter() throws OptimizationException {
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
        Assert.assertEquals(1, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceeded() throws OptimizationException {
        optimizer.setMaxIterations(0);
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        // Set up a test function
        DifferentiableMultivariateVectorialFunction function = new UnivariateFunctionWrapper(new MyFunction());

        optimizer.updateJacobian();
        Assert.assertArrayEquals(new double[0][0], optimizer.jacobian, 0.0);

        // Test the function with some input
        double[] input = new double[]{1.0, 2.0};
        double[] output = function.value(input);

        optimizer.updateJacobian();
        Assert.assertEquals(2, optimizer.rows);
        Assert.assertEquals(2, optimizer.cols);
        Assert.assertArrayEquals(new double[][]{{-output[0]/2.0, -output[1]/2.0},
                                               {-output[0]/2.0, -output[1]/2.0}}, optimizer.jacobian, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException {
        DifferentiableMultivariateVectorialFunction function = new UnivariateFunctionWrapper(new MyFunctionThrowingException());

        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(1);

        optimizer.optimize(function, new double[]{}, new double[]{}, new double[]{0.0});
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws OptimizationException, FunctionEvaluationException {
        optimizer.setMaxIterations(0);
        optimizer.optimize(new MyFunction(), new double[]{0.0}, new double[]{}, new double[]{0.0});
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() throws OptimizationException, FunctionEvaluationException {
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(new MyFunction(), new double[]{0.0}, new double[]{}, new double[]{0.0});
    }

    @Test
    public void testGetRMS() {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        optimizer.optimize(new MyFunctionWithRMS(), target, weights, new double[]{0.0});
        Assert.assertEquals(Math.sqrt(2.0), optimizer.getRMS(), 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        optimizer.optimize(new MyFunctionWithChiSquare(), target, weights, new double[]{0.0});
        Assert.assertEquals(5.0, optimizer.getChiSquare(), 1e-6);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        optimizer.optimize(new MyFunctionWithRMS(), target, weights, new double[]{0.0});
        double[] errors = optimizer.guessParametersErrors();
        Assert.assertArrayEquals(new double[]{Math.sqrt(2.0/3.0), Math.sqrt(2.0/3.0)}, errors, 1e-6);
    }

    private static class MyFunction implements DifferentiableMultivariateVectorialFunction {
        @Override
        public double[] value(double[] x) {
            return new double[]{Math.pow(x[0]-1.0, 2.0) + Math.pow(x[1]-1.0, 2.0),
                               Math.pow(x[0]-1.0, 2.0)};
        }

        @Override
        public double[][] jacobian(double[] x) {
            double[][] jacobian = new double[2][2];
            jacobian[0][0] = -2.0 * (x[0] - 1.0);
            jacobian[0][1] = -2.0 * (x[1] - 1.0);
            jacobian[1][0] = -2.0 * (x[0] - 1.0);
            jacobian[1][1] = -2.0 * (x[1] - 1.0);
            return jacobian;
        }
    }

    private static class MyFunctionThrowingException implements DifferentiableMultivariateVectorialFunction {
        @Override
        public double[] value(double[] x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("Function evaluation failed");
        }

        @Override
        public double[][] jacobian(double[] x) {
            return new double[0][0];
        }
    }

    private static class MyFunctionWithRMS implements DifferentiableMultivariateVectorialFunction {
        @Override
        public double[] value(double[] x) {
            return new double[]{Math.pow(x[0]-1.0, 2.0) + Math.pow(x[1]-1.0, 2.0),
                               Math.pow(x[0]-1.0, 2.0)};
        }

        @Override
        public double[][] jacobian(double[] x) {
            double[][] jacobian = new double[2][2];
            jacobian[0][0] = -2.0 * (x[0] - 1.0);
            jacobian[0][1] = -2.0 * (x[1] - 1.0);
            jacobian[1][0] = -2.0 * (x[0] - 1.0);
            jacobian[1][1] = -2.0 * (x[1] - 1.0);
            return jacobian;
        }
    }

    private static class MyFunctionWithChiSquare implements DifferentiableMultivariateVectorialFunction {
        @Override
        public double[] value(double[] x) {
            return new double[]{Math.pow(x[0]-1.0, 2.0) + Math.pow(x[1]-1.0, 2.0),
                               Math.pow(x[0]-1.0, 2.0)};
        }

        @Override
        public double[][] jacobian(double[] x) {
            double[][] jacobian = new double[2][2];
            jacobian[0][0] = -2.0 * (x[0] - 1.0);
            jacobian[0][1] = -2.0 * (x[1] - 1.0);
            jacobian[1][0] = -2.0 * (x[0] - 1.0);
            jacobian[1][1] = -2.0 * (x[1] - 1.0);
            return jacobian;
        }
    }

    // Wrapper class for univariate functions to make them implement DifferentiableMultivariateVectorialFunction
    private static class UnivariateFunctionWrapper implements DifferentiableMultivariateVectorialFunction {
        private final UnivariateFunction function;

        public UnivariateFunctionWrapper(UnivariateFunction function) {
            this.function = function;
        }

        @Override
        public double[] value(double[] x) {
            return new double[]{function.value(x[0])};
        }

        @Override
        public double[][] jacobian(double[] x) {
            return new double[][]{{function.derivativeValue(x[0])}};
        }
    }
}
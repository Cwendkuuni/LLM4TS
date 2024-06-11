package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertDoubleEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.optim.PointValuePair;

public class AbstractLeastSquaresOptimizerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNullFunction() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(null, new double[]{}, new double[]{}, new double[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWrongTargetLength() {
        DifferentiableMultivariateVectorialFunction function = mockFunction();
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[]{}, new double[]{1}, new double[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWrongWeightsLength() {
        DifferentiableMultivariateVectorialFunction function = mockFunction();
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[]{}, new double[]{}, new double[]{1, 2});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWrongStartPointLength() {
        DifferentiableMultivariateVectorialFunction function = mockFunction();
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[]{}, new double[]{}, new double[]{1, 2, 3});
    }

    @Test
    public void testOptimize() {
        DifferentiableMultivariateVectorialFunction function = mockFunction();
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1, 2, 3};
        double[] weights = {1, 1, 1};
        double[] startPoint = {0, 0, 0};

        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        assertArrayEquals(new double[]{-0.5, -0.5, -0.5}, result.getPoint(), 0.001);
        assertDoubleEquals(0.25, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.5, 0.5, 0.5}, optimizer.getResiduals(), 0.001);
        assertArrayEquals(new double[]{0.25, 0.25, 0.25}, optimizer.getTargetValues(), 0.001);
        assertEquals(3, optimizer.getIterations());
        assertEquals(5, optimizer.getEvaluations());
        assertEquals(3, optimizer.getJacobianEvaluations());
        assertTrue(optimizer.isOptimized());
    }

    @Test
    public void testOptimizeMaxIterations() {
        DifferentiableMultivariateVectorialFunction function = mockFunction();
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        int maxIterations = 1;
        optimizer.setMaxIterations(maxIterations);
        double[] target = {1, 2, 3};
        double[] weights = {1, 1, 1};
        double[] startPoint = {0, 0, 0};

        try {
            optimizer.optimize(function, target, weights, startPoint);
            fail();
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testOptimizeMaxEvaluations() {
        DifferentiableMultivariateVectorialFunction function = mockFunction();
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        int maxEvaluations = 1;
        optimizer.setMaxEvaluations(maxEvaluations);
        double[] target = {1, 2, 3};
        double[] weights = {1, 1, 1};
        double[] startPoint = {0, 0, 0};

        try {
            optimizer.optimize(function, target, weights, startPoint);
            fail();
        } catch (MaxEvaluationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] residuals = {0.1, 0.2, 0.3};
        double[] targetValues = {1, 1, 1};
        double[] weights = {1, 1, 1};
        optimizer.setResiduals(residuals);
        optimizer.setTargetValues(targetValues);
        optimizer.setResidualsWeights(weights);

        double rms = optimizer.getRMS();

        assertEquals(0.31622776601683795, rms, 0.0001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] residuals = {0.1, 0.2, 0.3};
        double[] targetValues = {1, 1, 1};
        double[] weights = {1, 1, 1};
        optimizer.setResiduals(residuals);
        optimizer.setTargetValues(targetValues);
        optimizer.setResidualsWeights(weights);

        double chiSquare = optimizer.getChiSquare();

        assertEquals(1.2000000000000001, chiSquare, 0.0001);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        DifferentiableMultivariateVectorialFunction function = mockFunction();
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        int rows = 3;
        int cols = 3;
        optimizer.optimize(function, new double[rows], new double[rows], new double[cols]);

        double[] errors = optimizer.guessParametersErrors();

        assertEquals(1.5811388300841898, errors[0], 0.001);
        assertEquals(1.5811388300841898, errors[1], 0.001);
        assertEquals(1.5811388300841898, errors[2], 0.001);
    }

    private DifferentiableMultivariateVectorialFunction mockFunction() {
        class MockFunction implements DifferentiableMultivariateVectorialFunction {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                double[] result = new double[3];
                result[0] = x[0] * x[0] + x[1] * x[1] + x[2] * x[2];
                result[1] = Math.sin(x[0]) + Math.cos(x[1]) + Math.tan(x[2]);
                result[2] = x[0] * Math.exp(x[1]) * Math.sin(x[2]);
                return result;
            }

            @Override
            public MultivariateMatrixFunction jacobian() throws FunctionEvaluationException {
                return new MockJacobian();
            }

            private class MockJacobian implements MultivariateMatrixFunction {
                @Override
                public double[] value(double[] x) throws FunctionEvaluationException {
                    double[][] jacobian = new double[3][3];
                    jacobian[0][0] = 2 * x[0];
                    jacobian[0][1] = 0;
                    jacobian[0][2] = x[2] * Math.exp(x[1]);
                    jacobian[1][0] = Math.cos(x[0]);
                    jacobian[1][1] = -Math.cos(x[1]) * Math.cos(x[1]) - Math.sin(x[1]) * Math.sin(x[1]);
                    jacobian[1][2] = Math.sin(x[1]) * Math.cos(x[2]);
                    jacobian[2][0] = x[0] * Math.exp(x[1]);
                    jacobian[2][1] = -Math.sin(x[1]) * Math.cos(x[2]);
                    jacobian[2][2] = Math.exp(x[1]) * (Math.cos(x[2]) * Math.cos(x[2]) + Math.sin(x[2]) * Math.sin(x[2]));
                    return jacobian;
                }
            }
        }
        return new MockFunction();
    }
}
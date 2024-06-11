package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.optimization.VectorialPointValuePair;

public class AbstractLeastSquaresOptimizerTest {

    @Test(expected = MathRuntimeException.class)
    public void testOptimizeNullFunction() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(null, new double[0], new double[0], new double[0]);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testOptimizeWithIncorrectDimensions() {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 1);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[] {1.0}, new double[] {1.0}, new double[] {1.0, 1.0});
    }

    @Test
    public void testOptimize() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        VectorialPointValuePair result = optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0},
                new double[] {0.0, 0.0});
        double[] expected = new double[] {0.5, 0.5};
        assertArrayEquals(expected, result.getPoint(), 0.001);
        assertEquals(0.25, result.getValue(), 0.001);
    }

    @Test
    public void testGetRMS() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        assertEquals(0.5, optimizer.getRMS(), 0.001);
    }

    @Test
    public void testGetChiSquare() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        double[] residuals = optimizer.getResiduals();
        double[] weights = optimizer.getResidualsWeights();
        assertEquals(4.0, optimizer.getChiSquare(), 0.001);
        for (int i = 0; i < residuals.length; i++) {
            assertEquals(weights[i], residuals[i] * residuals[i], 0.001);
        }
    }

    @Test
    public void testGetCovariances() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        double[][] covariances = optimizer.getCovariances();
        double[][] expected = new double[][] {{0.25, -0.25}, {-0.25, 0.25}};
        assertArrayEquals(expected, covariances, 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        double[] errors = optimizer.guessParametersErrors();
        double[] expected = new double[] {0.5, 0.5};
        assertArrayEquals(expected, errors, 0.001);
    }

    @Test
    public void testGetMaxIterations() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        optimizer.setMaxIterations(10);
        assertEquals(10, optimizer.getMaxIterations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIterationsNegative() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(-1);
    }

    @Test
    public void testGetIterations() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(0, optimizer.getIterations());
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        assertEquals(1, optimizer.getIterations());
    }

    @Test
    public void testGetMaxEvaluations() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        optimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegative() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(-1);
    }

    @Test
    public void testGetEvaluations() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(0, optimizer.getEvaluations());
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(0, optimizer.getJacobianEvaluations());
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        assertEquals(1, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getConvergenceChecker());
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getConvergenceChecker());
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertEquals(SimpleVectorialValueChecker.class, optimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testIncrementIterationsCounter() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
        assertTrue(optimizer.getIterations() == 1);
        optimizer.incrementIterationsCounter();
        assertTrue(optimizer.getIterations() == 2);
    }

    @Test(expected = OptimizationException.class)
    public void testIncrementIterationsCounterMaxIterationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        optimizer.updateJacobian();
        double[][] expected = new double[][] {{Math.sqrt(0.5), -Math.sqrt(0.5)}, {Math.sqrt(0.5), Math.sqrt(0.5)}};
        assertArrayEquals(expected, optimizer.getJacobian(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianFunctionEvaluationException() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunctionThrowingException(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        optimizer.updateJacobian();
    }

    @Test(expected = OptimizationException.class)
    public void testUpdateJacobianMaxIterationsExceeded() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        optimizer.updateJacobian();
    }

    @Test(expected = OptimizationException.class)
    public void testUpdateJacobianMaxEvaluationsExceeded() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new MockFunction(2, 2);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {0.0, 0.0});
        optimizer.updateResidualsAndCost();
        double[] expectedResiduals = new double[] {0.5, 0.5};
        assertArrayEquals(expectedResiduals, optimizer.getResiduals(), 0.001);
        assertEquals(0.25, optimizer.getCost(), 0.001);
    }

    abstract static class MockFunction implements DifferentiableMultivariateVectorialFunction {
        int rows, cols;

        public MockFunction(int r, int c) {
            rows = r;
            cols = c;
        }

        @Override
        public double[] value(double[] x) {
            double[] result = new double[rows];
            for (int i = 0; i < rows; i++) {
                result[i] = x[i] * x[i];
            }
            return result;
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return new MockJacobian();
        }

        abstract static class MockJacobian implements MultivariateMatrixFunction {
            @Override
            public double[][] value(double[] x) {
                double[][] result = new double[rows][cols];
                for (int i = 0; i < rows; i++) {
                    result[i][i] = 2 * x[i];
                }
                return result;
            }
        }
    }

    static class MockFunctionThrowingException extends MockFunction {
        public MockFunctionThrowingException(int r, int c) {
            super(r, c);
        }

        @Override
        public double[] value(double[] x) {
            throw new FunctionEvaluationException(new Exception());
        }
    }
}
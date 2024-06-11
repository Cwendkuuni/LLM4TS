package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorFactory;
import org.apache.commons.math3.optim.PointValuePair;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(100, optimizer.getMaxIterations());
        optimizer.setMaxIterations(15);
        assertEquals(15, optimizer.getMaxIterations());
        assertEquals(0, optimizer.getIterations());
        optimizer.incrementIterationsCounter();
        assertEquals(1, optimizer.getIterations());
        try {
            optimizer.incrementIterationsCounter();
            fail();
        } catch (OptimizationException e) {
            assertEquals(new MaxIterationsExceededException(15), e.getCause());
        }

        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        optimizer.setMaxEvaluations(5);
        assertEquals(5, optimizer.getMaxEvaluations());
        assertEquals(0, optimizer.getEvaluations());

        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        assertEquals(target.length, result.getPoint().length);
        assertEquals(target.length, result.getValue().length);
        assertArrayEquals(startPoint, result.getPoint());
        assertArrayEquals(target, result.getValue());
        assertEquals(5, optimizer.getEvaluations());
        assertEquals(0, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testCostAndChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1.0, 2.0};
        double[] residualsWeights = {1.0, 1.0};
        double[] residuals = new double[target.length];
        double[] objective = new double[target.length];

        optimizer.optimize(new MockFunction(), target, residualsWeights, new double[target.length]);
        optimizer.updateResidualsAndCost();

        assertEquals(Math.sqrt(2.0), optimizer.getRMS(), 0.001);
        assertEquals(2.0, optimizer.getChiSquare(), 0.001);
        assertEquals(0.0, optimizer.getCost(), 0.001);
    }

    @Test
    public void testCovariances() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1.0, 2.0};
        double[] jacobian = new double[][]{{1.0, 1.0}, {1.0, 2.0}};
        double[] covariances = new double[][]{
            {1.1, 0.5},
            {0.5, 1.6}
        };

        optimizer.optimize(new MockFunction(), target, null, new double[target.length]);
        optimizer.updateJacobian();

        try {
            optimizer.getCovariances();
            fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }

        optimizer.jF = new MockMatrixFunction(jacobian);
        double[][] result = optimizer.getCovariances();

        assertArrayEquals(covariances, result);
    }

    @Test
    public void testGuessParametersErrors() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1.0, 2.0};
        double[] error = new double[]{1.5, 0.8};

        optimizer.optimize(new MockFunction(), target, null, new double[target.length]);
        double[] errors = optimizer.guessParametersErrors();

        assertArrayEquals(error, errors);
    }

    private static class MockFunction implements DifferentiableMultivariateVectorialFunction {
        public RealVector value(RealVector x) {
            return RealVectorFactory.getInstance().createVector(new double[]{Math.pow(x.getEntry(0), 2.0),
                                                                             Math.pow(x.getEntry(1), 2.0)});
        }

        public RealMatrix jacobian(RealVector x) {
            return new Array2DRowRealMatrix(new double[][]{
                {2.0 * x.getEntry(0), 0.0},
                {0.0, 2.0 * x.getEntry(1)}});
        }
    }

    private static class MockMatrixFunction implements MultivariateMatrixFunction {
        private final double[][] matrix;

        public MockMatrixFunction(double[][] matrix) {
            this.matrix = matrix;
        }

        public RealMatrix value(RealVector x) {
            return new Array2DRowRealMatrix(matrix);
        }
    }
}
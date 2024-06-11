package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private DifferentiableMultivariateVectorialFunction function;
    private double[] target;
    private double[] weights;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return new VectorialPointValuePair(new double[0], new double[0]);
            }
        };

        function = (f -> new VectorialValue(f.value(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}))));
        target = new double[]{1, 1};
        weights = new double[]{1, 1};
        startPoint = new double[]{0, 0};
    }

    @Test
    public void testOptimize_BasicFunctionality() throws Exception {
        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_FunctionEvaluationException() throws Exception {
        function = (f -> { throw new FunctionEvaluationException(new IllegalArgumentException("FunctionEvaluationException")); });
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimize_ConvergenceException() throws Exception {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new OptimizationException("ConvergenceException");
            }
        };
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_IllegalArgumentException() throws Exception {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new IllegalArgumentException("IllegalArgumentException");
            }
        };
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test
    public void testGetRMS() {
        double[] targetValues = {1, 1, 1, 1};
        double[] residualsWeights = {1, 1, 1, 1};
        double[] residuals = {1, 2, 3, 4};
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                updateResidualsAndCost();
                return new VectorialPointValuePair(point, objective);
            }
        };
        optimizer.setTargetValues(targetValues);
        optimizer.setResidualsWeights(residualsWeights);
        optimizer.setResiduals(residuals);
        assertEquals(Math.sqrt(4.5), optimizer.getRMS(), 0.01);
    }

    @Test
    public void testGetChiSquare() {
        double[] targetValues = {1, 1, 1, 1};
        double[] residualsWeights = {1, 1, 1, 1};
        double[] residuals = {1, 2, 3, 4};
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                updateResidualsAndCost();
                return new VectorialPointValuePair(point, objective);
            }
        };
        optimizer.setTargetValues(targetValues);
        optimizer.setResidualsWeights(residualsWeights);
        optimizer.setResiduals(residuals);
        assertEquals(14.5, optimizer.getChiSquare(), 0.01);
    }

    @Test
    public void testGetCovariances() throws Exception {
        double[][] targetValues = {{1, 1}, {1, 1}};
        double[] residualsWeights = {1, 1};
        double[] point = {1, 1};
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                updateJacobian();
                return new VectorialPointValuePair(point, objective);
            }
        };
        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, targetValues[0], residualsWeights, startPoint);
        RealMatrix covariances = optimizer.getCovariances();
        RealVector covarianceVector = covariances.getColumnVector(0);
        assertEquals(Math.sqrt(0.5), covarianceVector.getEntry(0), 0.01);
        assertEquals(Math.sqrt(0.5), covarianceVector.getEntry(1), 0.01);
        assertEquals(Math.sqrt(0.5), covarianceVector.getEntry(1), 0.01);
        assertEquals(Math.sqrt(0.5), covarianceVector.getEntry(0), 0.01);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        double[][] targetValues = {{1, 1}, {1, 1}};
        double[] residualsWeights = {1, 1};
        double[] point = {1, 1};
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                updateJacobian();
                return new VectorialPointValuePair(point, objective);
            }
        };
        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, targetValues[0], residualsWeights, startPoint);
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(Math.sqrt(2.0 / 2.0), errors[0], 0.01);
        assertEquals(Math.sqrt(2.0 / 2.0), errors[1], 0.01);
    }

}
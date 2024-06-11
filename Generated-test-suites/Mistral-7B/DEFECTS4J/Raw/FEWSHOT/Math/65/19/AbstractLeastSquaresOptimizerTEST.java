package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.simple.SquaredEuclideanDistanceFunction;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertNull(optimizer.getConvergenceChecker());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIterationsNegative() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIterationsZero() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(0);
    }

    @Test
    public void testSetMaxIterationsPositive() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(10);
        assertEquals(10, optimizer.getMaxIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testExceededMaxIterations() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(new SquaredEuclideanDistanceFunction(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}}, new double[]{1.0, 1.0, 1.0}), null, null, new double[]{0.0, 0.0});
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegative() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(-1);
    }

    @Test
    public void testSetMaxEvaluationsZero() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(0);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluationsPositive() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test(expected = OptimizationException.class)
    public void testExceededMaxEvaluations() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(new SquaredEuclideanDistanceFunction(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}}, new double[]{1.0, 1.0, 1.0}), null, null, new double[]{0.0, 0.0});
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testIncrementIterationsCounter() throws OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
        assertEquals(1, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testExceededMaxIterationsAndIncrement() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        SquaredEuclideanDistanceFunction function = new SquaredEuclideanDistanceFunction(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}}, new double[]{1.0, 1.0, 1.0});
        optimizer.optimize(function, null, null, new double[]{0.0, 0.0});
        assertEquals(3, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        SquaredEuclideanDistanceFunction function = new SquaredEuclideanDistanceFunction(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}}, new double[]{1.0, 1.0, 1.0});
        double[] startPoint = new double[]{1.0, 1.0};
        optimizer.optimize(function, null, null, startPoint);
        assertEquals(2.0, optimizer.getCost(), 1e-6);
        RealVector residuals = optimizer.getResiduals();
        for (int i = 0; i < residuals.getDimension(); i++) {
            assertEquals(-optimizer.getTargetValues()[i], residuals.getEntry(i), 1e-6);
        }
    }

    @Test
    public void testGetRMS() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        SquaredEuclideanDistanceFunction function = new SquaredEuclideanDistanceFunction(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}}, new double[]{1.0, 1.0, 1.0});
        double[] startPoint = new double[]{0.1, 0.1};
        optimizer.optimize(function, null, null, startPoint);
        double rms = optimizer.getRMS();
        double chiSquare = optimizer.getChiSquare();
        double[] target = optimizer.getTargetValues();
        double[] weights = optimizer.getResidualsWeights();
        double[] point = optimizer.getPoint();
        double[] residuals = optimizer.getResiduals();
        double[] objective = optimizer.getObjective();
        double[] jacobian = optimizer.getJacobian();
        double[] covariances = optimizer.getCovariances();
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(rms, Math.sqrt(chiSquare / (optimizer.getRows() - optimizer.getCols())), 1e-6);
        assertArrayEquals(target, point, 1e-6);
        assertArrayEquals(target, objective, 1e-6);
        assertArrayEquals(target, function.value(point), 1e-6);
        assertArrayEquals(jacobian, function.jacobian().value(point), 1e-6);
        for (int i = 0; i < optimizer.getCols(); i++) {
            for (int j = 0; j < optimizer.getCols(); j++) {
                assertEquals(covariances[i][j], covariances[j][i], 1e-6);
            }
            assertEquals(Math.sqrt(covariances[i][i]) * Math.sqrt(chiSquare / (optimizer.getRows() - optimizer.getCols())), errors[i], 1e-6);
        }
    }

    @Test
    public void testGetChiSquare() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        SquaredEuclideanDistanceFunction function = new SquaredEuclideanDistanceFunction(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}}, new double[]{1.0, 1.0, 1.0});
        double[] startPoint = new double[]{0.1, 0.1};
        optimizer.optimize(function, null, null, startPoint);
        double chiSquare = optimizer.getChiSquare();
        double[] target = optimizer.getTargetValues();
        double[] weights = optimizer.getResidualsWeights();
        double[] point = optimizer.getPoint();
        double[] residuals = optimizer.getResiduals();
        double[] objective = optimizer.getObjective();
        double[] jacobian = optimizer.getJacobian();
        double[] covariances = optimizer.getCovariances();
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(chiSquare, Math.sqrt(chiSquare / (optimizer.getRows() - optimizer.getCols()) * (optimizer.getRows() * optimizer.getRows())) - (optimizer.getRows() * optimizer.getCols()), 1e-6);
    }

    @Test
    public void testGetCovariances() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        SquaredEuclideanDistanceFunction function = new SquaredEuclideanDistanceFunction(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}}, new double[]{1.0, 1.0, 1.0});
        double[] startPoint = new double[]{0.1, 0.1};
        optimizer.optimize(function, null, null, startPoint);
        RealArray2D covariances = optimizer.getCovariances();
        double[] target = optimizer.getTargetValues();
        double[] weights = optimizer.getResidualsWeights();
        double[] point = optimizer.getPoint();
        double[] residuals = optimizer.getResiduals();
        double[] objective = optimizer.getObjective();
        double[] jacobian = optimizer.getJacobian();
        RealVector errors = optimizer.guessParametersErrors();
        Array2DRowRealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{{1.0 / (optimizer.getRows() - optimizer.getCols()), 0.0}, {0.0, 1.0 / (optimizer.getRows() - optimizer.getCols())}});
        for (int i = 0; i < optimizer.getCols(); i++) {
            for (int j = 0; j < optimizer.getCols(); j++) {
                assertEquals(expectedCovariances.getEntry(i, j), covariances.getEntry(i, j), 1e-6);
            }
        }
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        SquaredEuclideanDistanceFunction function = new SquaredEuclideanDistanceFunction(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}}, new double[]{1.0, 1.0, 1.0});
        double[] startPoint = new double[]{0.1, 0.1};
        optimizer.optimize(function, null, null, startPoint);
        double[] errors = optimizer.guessParametersErrors();
        double[] target = optimizer.getTargetValues();
        double[] point = optimizer.getPoint();
        double[] residuals = optimizer.getResiduals();
        double[] objective = optimizer.getObjective();
        assertEquals(Math.sqrt(1.0 / (optimizer.getRows() - optimizer.getCols())), errors[0], 1e-6);
        assertEquals(Math.sqrt(1.0 / (optimizer.getRows() - optimizer.getCols())), errors[1], 1e-6);
    }

}
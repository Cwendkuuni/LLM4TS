package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.GoalTypeVector;
import org.apache.commons.math3.optim.nonlinear.vector.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.points.PointValues;
import org.apache.commons.math3.optim.vector.RealVectorValueChecker;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        RealMatrix J = new Array2DRowRealMatrix(data);
        DifferentiableMultivariateVectorialFunction f = new AbstractMultivariateLinearRegressionFunction(J, target);
        PointValuePair optimalPointValuePair = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(3).getValue());
        optimizer = (AbstractLeastSquaresOptimizer) optimizer;
        optimizer.setPoint(optimalPointValuePair.getPoint());
        double rms = optimizer.getRMS();
        assertEquals(Math.sqrt(3.0), rms, 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        RealMatrix J = new Array2DRowRealMatrix(data);
        DifferentiableMultivariateVectorialFunction f = new AbstractMultivariateLinearRegressionFunction(J, target);
        PointValuePair optimalPointValuePair = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(3).getValue());
        optimizer = (AbstractLeastSquaresOptimizer) optimizer;
        optimizer.setPoint(optimalPointValuePair.getPoint());
        double chiSquare = optimizer.getChiSquare();
        assertEquals(5.551115123125783, chiSquare, 1e-6);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        RealMatrix J = new Array2DRowRealMatrix(data);
        DifferentiableMultivariateVectorialFunction f = new AbstractMultivariateLinearRegressionFunction(J, target);
        PointValuePair optimalPointValuePair = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(3).getValue());
        optimizer = (AbstractLeastSquaresOptimizer) optimizer;
        optimizer.setPoint(optimalPointValuePair.getPoint());
        double[][] covariances = optimizer.getCovariances();
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{{1.0/3.0, 0.0, 0.0}, {0.0, 1.0/3.0, 0.0}, {0.0, 0.0, 1.0/3.0}});
        assertEquals(expectedCovariances.getEntry(0, 0), covariances[0][0], 1e-6);
        assertEquals(expectedCovariances.getEntry(1, 1), covariances[1][1], 1e-6);
        assertEquals(expectedCovariances.getEntry(2, 2), covariances[2][2], 1e-6);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        RealMatrix J = new Array2DRowRealMatrix(data);
        DifferentiableMultivariateVectorialFunction f = new AbstractMultivariateLinearRegressionFunction(J, target);
        PointValuePair optimalPointValuePair = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(3).getValue());
        optimizer = (AbstractLeastSquaresOptimizer) optimizer;
        optimizer.setPoint(optimalPointValuePair.getPoint());
        double[] errors = optimizer.guessParametersErrors();
        double[] expectedErrors = new double[]{Math.sqrt(1.0/3.0), Math.sqrt(1.0/3.0), Math.sqrt(1.0/3.0)};
        assertArrayEquals(expectedErrors, errors, 1e-6);
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        RealMatrix J = new Array2DRowRealMatrix(data);
        DifferentiableMultivariateVectorialFunction f = new AbstractMultivariateLinearRegressionFunction(J, target);
        PointValuePair optimalPointValuePair = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(3).getValue());
        optimizer = (AbstractLeastSquaresOptimizer) optimizer;
        optimizer.setPoint(optimalPointValuePair.getPoint());
        double[] expectedPoint = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedPoint, optimizer.getPoint(), 1e-6);
        double[] expectedObjective = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedObjective, optimizer.getObjective(), 1e-6);
        double[] expectedResiduals = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedResiduals, optimizer.getResiduals(), 1e-6);
        double[] expectedJacobian = new double[][]{{-0.5, -0.5, -0.5}, {-0.5, -0.5, -0.5}, {-0.5, -0.5, -0.5}};
        RealMatrix expectedJacobianMatrix = new Array2DRowRealMatrix(expectedJacobian);
        assertEquals(expectedJacobianMatrix, optimizer.getJacobian(), 1e-6);
        double rms = optimizer.getRMS();
        assertEquals(Math.sqrt(3.0), rms, 1e-6);
    }

    @Test
    public void testOptimizeWithErrorChecking() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        RealMatrix J = new Array2DRowRealMatrix(data);
        DifferentiableMultivariateVectorialFunction f = new AbstractMultivariateLinearRegressionFunction(J, target);
        PointValuePair optimalPointValuePair = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(3).getValue());
        optimizer = (AbstractLeastSquaresOptimizer) optimizer;
        optimizer.setPoint(optimalPointValuePair.getPoint());
        double[] expectedPoint = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedPoint, optimizer.getPoint(), 1e-6);
        double[] expectedObjective = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedObjective, optimizer.getObjective(), 1e-6);
        double[] expectedResiduals = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedResiduals, optimizer.getResiduals(), 1e-6);
        double[] expectedJacobian = new double[][]{{-0.5, -0.5, -0.5}, {-0.5, -0.5, -0.5}, {-0.5, -0.5, -0.5}};
        RealMatrix expectedJacobianMatrix = new Array2DRowRealMatrix(expectedJacobian);
        assertEquals(expectedJacobianMatrix, optimizer.getJacobian(), 1e-6);
        SimpleVectorialValueChecker checker = new SimpleVectorialValueChecker(1e-6);
        optimizer.setConvergenceChecker(checker);
        optimizer.setMaxIterations(5);
        PointValues pointValues = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(3).getValue());
        assertEquals(GoalType.MINIMIZE, pointValues.getGoalTypeVector());
        assertEquals(1, pointValues.getNumberOfPoints());
        assertEquals(optimalPointValuePair.getValue(), pointValues.getPointValues().getVector()[0], 1e-6);
        assertEquals(0.0, pointValues.getFunctionValues()[0], 1e-6);
        assertEquals(0.0, pointValues.getGradientValues()[0].getEntry(0), 1e-6);
        assertEquals(0.0, pointValues.getGradientValues()[0].getEntry(1), 1e-6);
        assertEquals(0.0, pointValues.getGradientValues()[0].getEntry(2), 1e-6);
        assertEquals(GoalType.MINIMIZE, pointValues.getGoalTypeVector()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidWeight() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 0.0, 1.0};
        RealMatrix J = new Array2DRowRealMatrix(data);
        DifferentiableMultivariateVectorialFunction f = new AbstractMultivariateLinearRegressionFunction(J, target);
        PointValuePair optimalPointValuePair = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(3).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidTargetSize() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] target = new double[]{1.0, 1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        RealMatrix J = new Array2DRowRealMatrix(data);
        DifferentiableMultivariateVectorialFunction f = new AbstractMultivariateLinearRegressionFunction(J, target);
        PointValuePair optimalPointValuePair = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(3).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidStartPointSize() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        RealMatrix J = new Array2DRowRealMatrix(data);
        DifferentiableMultivariateVectorialFunction f = new AbstractMultivariateLinearRegressionFunction(J, target);
        PointValuePair optimalPointValuePair = optimizer.optimize(f, null, weights, new ArrayRealVectorInitializer(2).getValue());
    }
}

class AbstractMultivariateLinearRegressionFunction implements DifferentiableMultivariateVectorialFunction {

    private final RealMatrix J;
    private final double[] target;

    public AbstractMultivariateLinearRegressionFunction(RealMatrix J, double[] target) {
        this.J = J;
        this.target = target;
    }

    @Override
    public RealVectorValueChecker getValueChecker() {
        return new RealVectorValueChecker();
    }

    @Override
    public RealVectorValue getValue(RealVector x) {
        double[] xArray = x.toArray();
        double[] y = J.operate(new Array2DRowRealMatrix(new double[][]{{xArray[0], xArray[1], xArray[2]}})).toArray();
        return new ArrayRealVector(y, new int[]{3});
    }

    @Override
    public RealMatrix getJacobian() {
        return J;
    }
}
package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.VectorialConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.SimpleVectorialFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private SimpleVectorialFunction function;
    private VectorialConvergenceChecker convergenceChecker;

    @Before
    public void setup() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException,
                           OptimizationException,
                           IllegalArgumentException {
                return new PointValuePair(new double[]{1.0, 2.0}, new double[]{0.0, 0.0});
            }
        };
        function = new SimpleVectorialFunction("x^2 + y^2", 2) {
            @Override
            public double value(RealVector x) {
                double xValue = x.getEntry(0);
                double yValue = x.getEntry(1);
                return xValue * xValue + yValue * yValue;
            }

            @Override
            public RealMatrix jacobian(RealVector x) {
                double xValue = x.getEntry(0);
                double yValue = x.getEntry(1);
                double[][] jacobian = new double[2][2];
                jacobian[0][0] = 2 * xValue;
                jacobian[0][1] = 0;
                jacobian[1][0] = 0;
                jacobian[1][1] = 2 * yValue;
                return new Array2DRowRealMatrix(jacobian);
            }
        };
        convergenceChecker = new SimpleVectorialValueChecker(1e-6);
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.setMaxIterations(10);
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(5);
        Assert.assertEquals(5, optimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxIterations() {
        Assert.assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS,
                optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        Assert.assertEquals(0, optimizer.getIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(3);
        Assert.assertEquals(3, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        Assert.assertEquals(0, optimizer.getEvaluations());
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws Exception {
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceededException() throws Exception {
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        optimizer.updateJacobian();
        Assert.assertEquals(2, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws Exception {
        optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        optimizer.updateResidualsAndCost();
        Assert.assertEquals(1, optimizer.getObjectiveEvaluations());
    }

    @Test
    public void testGetRMS() {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        optimizer.optimize(function, target, weights, new double[]{0.0, 0.0});
        Assert.assertEquals(Math.sqrt(2.0), optimizer.getRMS(), 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        optimizer.optimize(function, target, weights, new double[]{0.0, 0.0});
        Assert.assertEquals(3.0, optimizer.getChiSquare(), 1e-6);
    }

    @Test
    public void testGetCovariances() throws Exception {
        optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        double[][] covarianceMatrix = optimizer.getCovariances();
        Assert.assertEquals(2, covarianceMatrix.length);
        Assert.assertEquals(2, covarianceMatrix[0].length);
        Assert.assertEquals(2, covarianceMatrix[1].length);
        Assert.assertEquals(1.0 / 3.0, covarianceMatrix[0][0], 1e-6);
        Assert.assertEquals(0.0, covarianceMatrix[0][1], 1e-6);
        Assert.assertEquals(0.0, covarianceMatrix[1][0], 1e-6);
        Assert.assertEquals(1.0 / 3.0, covarianceMatrix[1][1], 1e-6);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        double[] errors = optimizer.guessParametersErrors();
        Assert.assertEquals(2, errors.length);
        Assert.assertTrue(errors[0] > 0);
        Assert.assertTrue(errors[1] > 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testGuessParametersErrorsNumberOfDegreesOfFreedomNegative() throws Exception {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        optimizer.optimize(function, target, weights, new double[]{0.0, 0.0});
        optimizer.setMaxIterations(0);
        optimizer.guessParametersErrors();
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        function = new SimpleVectorialFunction("1 / x", 1) {
            @Override
            public double value(RealVector x) {
                throw new FunctionEvaluationException(x, LocalizedFormats.DIVISION_BY_ZERO);
            }
        };
        optimizer.optimize(function, new double[]{1.0}, new double[]{1.0}, new double[]{0.0});
    }
}
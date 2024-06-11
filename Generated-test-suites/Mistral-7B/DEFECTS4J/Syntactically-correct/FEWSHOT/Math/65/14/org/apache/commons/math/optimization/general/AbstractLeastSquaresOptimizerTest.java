package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DRowAccessor;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.VectorialConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalings.PowScaling;
import org.apache.commons.math3.optim.nonlinear.scalings.SimpleScaling;
import org.apache.commons.math3.optim.nonlinear.vector.levenbergmarquardt.LevenbergMarquardtOptimizer;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private VectorialConvergenceChecker checker;
    private DifferentiableMultivariateVectorialFunction function;
    private Double[][] initialJacobian;
    private Double[] initialPoint;
    private Double[] initialResiduals;
    private Double[] initialTargetValues;
    private Double[] initialWeights;
    private AbstractLeastSquaresOptimizer optimizer;
    private RealMatrix jF;

    @Before
    public void setUp() {
        checker = new SimpleVectorialValueChecker(0.001);
        initialJacobian = new Double[][] {{1.0, 2.0}, {3.0, 4.0}};
        initialPoint = new Double[] {1.0, 1.0};
        initialResiduals = new Double[] {1.0, 1.0};
        initialTargetValues = new Double[] {2.0, 2.0};
        initialWeights = new Double[] {1.0, 1.0};
        function = mock(DifferentiableMultivariateVectorialFunction.class);
        jF = new Array2DRowRealMatrix(2, 2);
        when(function.jacobian()).thenReturn(jF);
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setConvergenceChecker(checker);
    }

    @Test
    public void testMaxEvaluationsExceeded() {
        when(function.value(any(RealVector.class)))
                .thenThrow(new MaxEvaluationsExceededException(1));
        try {
            optimizer.optimize(function, initialTargetValues, initialWeights, initialPoint);
            fail("MaxEvaluationsExceededException expected");
        } catch (MaxEvaluationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testMaxIterationsExceeded() {
        when(function.value(any(RealVector.class)))
                .thenReturn(new Double[] {0.0, 0.0});
        when(function.jacobian()).thenThrow(new MaxIterationsExceededException(1));
        try {
            optimizer.optimize(function, initialTargetValues, initialWeights, initialPoint);
            fail("MaxIterationsExceededException expected");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testFunctionEvaluationException() {
        when(function.value(any(RealVector.class)))
                .thenThrow(new FunctionEvaluationException(initialPoint, new IllegalArgumentException()));
        try {
            optimizer.optimize(function, initialTargetValues, initialWeights, initialPoint);
            fail("FunctionEvaluationException expected");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testOptimize() {
        when(function.value(any(RealVector.class)))
                .thenReturn(initialTargetValues);
        when(function.jacobian()).thenReturn(initialJacobian);
        when(function.jacobian()).thenReturn(initialJacobian);

        PowScaling scaling = new PowScaling(0.1);
        optimizer.setScaling(scaling);

        PointValuePair result = optimizer.optimize(function, initialTargetValues, initialWeights, initialPoint);
        assertArrayEquals(initialPoint, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
        verify(function, times(2)).value(initialPoint);
        verify(function, atLeastOnce()).jacobian();
        assertEquals(optimizer.getIterations(), 1, 1);
        verifyNoMoreInteractionsWithRealMatrix();
    }

    @Test
    public void testGetRms() {
        double[] residuals = {1.0, 1.0};
        double[] targetValues = {2.0, 2.0};
        double[] weights = {1.0, 1.0};
        optimizer.optimize(function, targetValues, weights, initialPoint);
        double[] objective = optimizer.objective;
        assertEquals(0.0, optimizer.getRMS(), 0.0);
        double[] newResiduals = {Math.pow(1.0 - objective[0], 2.0) * weights[0],
                Math.pow(1.0 - objective[1], 2.0) * weights[1]};
        assertEquals(Math.sqrt(VectorialUtils.dotProduct(newResiduals, weights)), optimizer.getRMS(), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        double[] residuals = {1.0, 1.0};
        double[] targetValues = {2.0, 2.0};
        double[] weights = {1.0 / 0.5, 1.0 / 0.5};
        optimizer.optimize(function, targetValues, weights, initialPoint);
        double[] objective = optimizer.objective;
        double chiSquare = optimizer.getChiSquare();
        double expectedChiSquare = 0.0;
        for (int i = 0; i < residuals.length; i++) {
            expectedChiSquare += Math.pow(residuals[i], 2.0) / weights[i];
        }
        assertEquals(expectedChiSquare, chiSquare, 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[][] jacobian = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] covariances = optimizer.getCovariances();
        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(2, covariances[1].length);
        assertArrayEquals(jacobian[0], covariances[0]);
        assertArrayEquals(jacobian[1], covariances[1]);
    }

    @Test
    public void testGuessParametersErrors() {
        double[] residuals = {1.0, 1.0};
        double[] targetValues = {2.0, 2.0};
        double[] weights = {1.0, 1.0};
        optimizer.optimize(function, targetValues, weights, initialPoint);
        double[] objective = optimizer.objective;
        double[] errors = optimizer.guessParametersErrors();
        double[] jacobian = optimizer.jacobian[0];
        assertEquals(2, errors.length);
        assertEquals(Math.sqrt(0.5), errors[0], 0.0);
        assertEquals(Math.sqrt(0.5), errors[1], 0.0);
        double[] jacobianDiagonal = new double[2];
        System.arraycopy(jacobian, 0, jacobianDiagonal, 0, 2);
        for (int i = 0; i < errors.length; i++) {
            assertEquals(Math.sqrt(jacobianDiagonal[i] * weights[i] / (double) (optimizer.rows - optimizer.cols)),
                    errors[i], 0.0);
        }
    }

    @Test(expected = OptimizationException.class)
    public void testGetCovariancesSingularProblem() {
        double[][] jacobian = {{1.0, 2.0}, {1.0, 1.0}};
        optimizer.updateJacobian();
        try {
            optimizer.getCovariances();
            fail("Singular problem expected");
        } catch (OptimizationException e) {
            // expected
        }
    }

}
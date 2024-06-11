package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialFunction;
import org.apache.commons.math3.optim.UnconstrainedMinimizationProblem;
import org.apache.commons.math3.optim.initialization.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.nonlinear.SimpleVectorialFunctionValues;
import org.apache.commons.math3.optim.nonlinear.gradient.Gradient;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the AbstractLeastSquaresOptimizer class.
 */
public class TestAbstractLeastSquaresOptimizer {

    private AbstractLeastSquaresOptimizer optimizer;
    private DifferentiableMultivariateVectorialFunction f;
    private double[] target;
    private double[] weights;
    private double[] startPoint;

    @Before
    public void setup() {
        optimizer = new TestLeastSquaresOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(20);

        f = new SimpleVectorialFunction(2) {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public Gradient gradient(double[] x) {
                return new Gradient() {
                    @Override
                    public double[] values(double[] x) {
                        return new double[] {2 * x[0], 2 * x[1]};
                    }
                };
            }
        };

        target = new double[]{1.0, 1.0};
        weights = new double[]{1.0, 1.0};
        startPoint = new double[]{2.0, 2.0};
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(5);
        assertEquals(5, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(3);
        assertEquals(3, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetIterations() {
        optimizer.optimize(f, target, weights, startPoint);
        assertEquals(0, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceeded() {
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testMaxEvaluationsExceeded() {
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(f, target, weights, startPoint);
    }

    @Test
    public void testOptimize() {
        PointValuePair result = optimizer.optimize(f, target, weights, startPoint);
        assertEquals(1.0, result.getPoint()[0], 0.001);
        assertEquals(1.0, result.getPoint()[1], 0.001);
        assertEquals(2.0, result.getValue(), 0.001);
    }

    @Test
    public void testGetRMS() {
        double[] targetRMS = new double[]{1.1, 1.1};
        optimizer.optimize(f, targetRMS, weights, startPoint);
        assertEquals(Math.sqrt(2.0), optimizer.getRMS(), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        optimizer.optimize(f, target, weights, startPoint);
        assertEquals(4.0, optimizer.getChiSquare(), 0.001);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        optimizer.optimize(f, target, weights, startPoint);
        double[][] covariances = optimizer.getCovariances();
        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(2, covariances[1].length);
        assertEquals(1.0 / 3.0, covariances[0][0], 0.001);
        assertEquals(1.0 / 3.0, covariances[0][1], 0.001);
        assertEquals(1.0 / 3.0, covariances[1][0], 0.001);
        assertEquals(1.0 / 3.0, covariances[1][1], 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        optimizer.optimize(f, target, weights, startPoint);
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(2, errors.length);
        assertEquals(Math.sqrt(1.0 / 3.0), errors[0], 0.001);
        assertEquals(Math.sqrt(1.0 / 3.0), errors[1], 0.001);
    }
}

/**
 * A test implementation of the LeastSquaresOptimizer interface for testing purposes.
 */
class TestLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {

    @Override
    protected VectorialPointValuePair doOptimize()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

        optimize(f, targetValues, residualsWeights, point);

        incrementIterationsCounter();
        updateResidualsAndCost();

        if (checker.isConverged(point, objective, residuals)) {
            return new VectorialPointValuePair(point, objective);
        }

        updateJacobian();

        moveAlongJacobian();

        return doOptimize();
    }
}
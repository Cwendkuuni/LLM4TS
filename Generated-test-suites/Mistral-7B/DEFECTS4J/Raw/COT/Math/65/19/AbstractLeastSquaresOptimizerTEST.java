package org.apache.commons.math.optimization.general.test;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.MultivariateFunctions;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(10);
        assertEquals(10, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(5);
        assertEquals(5, optimizer.getMaxEvaluations());
    }

    @Test
    public void testIncrementIterationsCounter() throws OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.incrementIterationsCounter();
        assertEquals(1, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testIterationsExceeded() throws OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.incrementIterationsCounter();
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianThrowsException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected void updateJacobian() throws FunctionEvaluationException {
                throw new FunctionEvaluationException(new Object[]{"point"}, new FunctionEvaluationException("Test Exception"));
            }
        };
        optimizer.optimize(MultivariateFunctions.SIN, new double[]{0, 0}, new double[]{1}, new double[]{0, 0});
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1, 1, 1};
        double[] weights = {1, 1, 1};
        double[] startPoint = {1, 1, 1};
        try {
            PointValuePair result = optimizer.optimize(MultivariateFunctions.SIN, target, weights, startPoint);
            double rms = optimizer.getRMS();
            assertEquals(Math.sqrt(3.0 / 3.0), rms, 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1, 1, 1};
        double[] weights = {1, 1, 1};
        double[] startPoint = {1, 1, 1};
        try {
            PointValuePair result = optimizer.optimize(MultivariateFunctions.SIN, target, weights, startPoint);
            double chiSquare = optimizer.getChiSquare();
            assertEquals(3.0, chiSquare, 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetCovariances() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1, 1, 1};
        double[] weights = {1, 1, 1};
        double[] startPoint = {1, 1, 1};
        try {
            PointValuePair result = optimizer.optimize(MultivariateFunctions.SIN, target, weights, startPoint);
            double[][] covariances = optimizer.getCovariances();
            assertEquals(3, covariances.length);
            assertEquals(3, covariances[0].length);
            assertEquals(3, covariances[1].length);
            assertEquals(3, covariances[2].length);
            for (int i = 0; i < covariances.length; i++) {
                for (int j = 0; j < covariances[i].length; j++) {
                    assertEquals(0.0, covariances[i][j], 0.001);
                }
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = {1, 1, 1};
        double[] weights = {1, 1, 1};
        double[] startPoint = {1, 1, 1};
        try {
            PointValuePair result = optimizer.optimize(MultivariateFunctions.SIN, target, weights, startPoint);
            double[] errors = optimizer.guessParametersErrors();
            assertEquals(3, errors.length);
            assertEquals(Math.sqrt(3.0 / 3.0), errors[0], 0.001);
            assertEquals(Math.sqrt(3.0 / 3.0), errors[1], 0.001);
            assertEquals(Math.sqrt(3.0 / 3.0), errors[2], 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDoOptimizeWithSimpleLinearRegression() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new SimpleLeastSquaresOptimizer();
        double[] target = {1, 2, 3, 4, 5};
        double[] weights = {1, 1, 1, 1, 1};
        double[] startPoint = {0, 0};
        try {
            PointValuePair result = optimizer.optimize(new SimpleLinearRegressionFunction(3), target, weights, startPoint);
            double[] expected = {1.0 / 3.0, 1.0};
            assertEquals(expected[0], result.getPoint()[0], 0.001);
            assertEquals(expected[1], result.getPoint()[1], 0.001);
            assertEquals(0.0, result.getValue(), 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testDoOptimizeWithMaxIterationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new SimpleLeastSquaresOptimizer();
        optimizer.setMaxIterations(0);
        double[] target = {1, 2, 3, 4, 5};
        double[] weights = {1, 1, 1, 1, 1};
        double[] startPoint = {0, 0};
        try {
            optimizer.optimize(new SimpleLinearRegressionFunction(3), target, weights, startPoint);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testDoOptimizeWithMaxEvaluationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new SimpleLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(0);
        double[] target = {1, 2, 3, 4, 5};
        double[] weights = {1, 1, 1, 1, 1};
        double[] startPoint = {0, 0};
        try {
            optimizer.optimize(new SimpleLinearRegressionFunction(3), target, weights, startPoint);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}

class SimpleLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
    @Override
    protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Implement your optimization algorithm here
        return new VectorialPointValuePair(new double[]{1.0, 1.0}, 0.0);
    }
}

class SimpleLinearRegressionFunction implements DifferentiableMultivariateVectorFunction {
    private final int dim;

    public SimpleLinearRegressionFunction(int dim) {
        this.dim = dim;
    }

    @Override
    public double value(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < dim; i++) {
            sum += x[i] * (i + 1);
        }
        return sum;
    }

    @Override
    public double[] gradient(double[] x) {
        double[] gradient = new double[dim];
        for (int i = 0; i < dim; i++) {
            gradient[i] = i + 1;
        }
        return gradient;
    }
}

class SimpleBounds implements Bounds {
    private final double min[];
    private final double max[];

    public SimpleBounds(double[] min, double[] max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public double getMin(int dimension) {
        return min[dimension];
    }

    @Override
    public double getMax(int dimension) {
        return max[dimension];
    }
}

class SimpleGoalType implements GoalType {
    @Override
    public double value(double[] x, double[] fvec) {
        return fvec[0];
    }

    @Override
    public boolean equals(GoalType other) {
        return (other instanceof SimpleGoalType);
    }

    @Override
    public String toString() {
        return "SimpleGoalType";
    }
}
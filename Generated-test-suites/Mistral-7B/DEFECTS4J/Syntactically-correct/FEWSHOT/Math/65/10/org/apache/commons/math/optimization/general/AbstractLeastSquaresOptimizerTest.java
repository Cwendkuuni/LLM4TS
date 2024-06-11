package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize should not be called in tests.");
                return null;
            }
        };

        // Check default settings
        assertEquals(100, optimizer.getMaxIterations());
        assertEquals(SimpleVectorialValueChecker.class, optimizer.getConvergenceChecker().getClass());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());

        // Test setting max iterations
        optimizer.setMaxIterations(5);
        assertEquals(5, optimizer.getMaxIterations());

        // Test setting convergence checker
        optimizer.setConvergenceChecker(new MyVectorialConvergenceChecker());
        assertNotSame(SimpleVectorialValueChecker.class, optimizer.getConvergenceChecker().getClass());

        // Test setting max evaluations
        optimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());

        // Test getting iterations and evaluations
        optimizer.incrementIterationsCounter();
        optimizer.updateResidualsAndCost();
        optimizer.updateJacobian();
        assertEquals(1, optimizer.getIterations());
        assertEquals(3, optimizer.getEvaluations());
        assertEquals(3, optimizer.getJacobianEvaluations());

        // Test getting RMS and chi-square values
        double[] data = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 4.0, 9.0};
        double[] target = {1.5, 2.2, 3.1};
        optimizer.optimize(new PolynomialFunctions.LinearRegressionFunction(data), target, weights, new ArrayRealVector(3));
        assertEquals(1.295, optimizer.getRMS(), 0.001);
        assertEquals(10.231, optimizer.getChiSquare(), 0.001);

        // Test getting covariance matrix
        RealMatrix covariances = optimizer.getCovariances();
        assertEquals(3, covariances.getRowDimension());
        assertEquals(3, covariances.getColumnDimension());
        assertEquals(0.00536, covariances.getEntry(0, 0), 0.001);
        assertEquals(0.00216, covariances.getEntry(1, 1), 0.001);
        assertEquals(-0.00318, covariances.getEntry(2, 2), 0.001);
        assertEquals(0.00216, covariances.getEntry(0, 1), 0.001);
        assertEquals(-0.00318, covariances.getEntry(1, 0), 0.001);
        assertEquals(0.01152, covariances.getEntry(1, 2), 0.001);
        assertEquals(-0.01152, covariances.getEntry(2, 1), 0.001);
        assertEquals(0.01272, covariances.getEntry(2, 2), 0.001);

        // Test guessing parameters errors
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(0.0314, errors[0], 0.001);
        assertEquals(0.0156, errors[1], 0.001);
        assertEquals(0.0371, errors[2], 0.001);

    }

    private static class MyVectorialConvergenceChecker implements VectorialConvergenceChecker {
        @Override
        public boolean isConverged(RealVector x, RealVector fx, double tolerance) {
            return false;
        }
    }
}

class MyPolynomialFunction implements DifferentiableMultivariateVectorialFunction {

    @Override
    public VectorialPointValuePair value(VectorialPoint x) throws FunctionEvaluationException {
        double[] xValues = x.toArray();
        double[] yValues = new double[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            yValues[i] = Math.pow(xValues[i], 2);
        }
        return new VectorialPointValuePair(new ArrayRealVector(xValues), new Array2DRowRealMatrix(yValues));
    }

    @Override
    public MultivariateMatrixFunction jacobian() {
        return new MultivariateMatrixFunction() {
            @Override
            public RealMatrix value(RealVector x) throws FunctionEvaluationException {
                double[] xValues = x.toArray();
                RealMatrix jacobian = new Array2DRowRealMatrix(new double[xValues.length][xValues.length]);
                for (int i = 0; i < xValues.length; i++) {
                    for (int j = 0; j < xValues.length; j++) {
                        jacobian.setEntry(i, j, 2 * xValues[i]);
                    }
                }
                return jacobian;
            }
        };
    }
}

class LinearRegressionFunction implements DifferentiableMultivariateVectorialFunction {

    private final double[] data;

    public LinearRegressionFunction(double[] data) {
        this.data = data;
    }

    @Override
    public VectorialPointValuePair value(VectorialPoint x) throws FunctionEvaluationException {
        double[] xValues = x.toArray();
        double[] yValues = new double[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            yValues[i] = 0;
            for (int j = 0; j < xValues.length; j++) {
                yValues[i] += data[i] * xValues[j];
            }
        }
        return new VectorialPointValuePair(new ArrayRealVector(xValues), new Array2DRowRealMatrix(yValues));
    }

    @Override
    public MultivariateMatrixFunction jacobian() {
        return new MultivariateMatrixFunction() {
            @Override
            public RealMatrix value(RealVector x) throws FunctionEvaluationException {
                double[] xValues = x.toArray();
                RealMatrix jacobian = new Array2DRowRealMatrix(new double[xValues.length][xValues.length]);
                for (int i = 0; i < xValues.length; i++) {
                    for (int j = 0; j < xValues.length; j++) {
                        jacobian.setEntry(i, j, data[i]);
                    }
                }
                return jacobian;
            }
        };
    }
}
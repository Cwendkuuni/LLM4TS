package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testCovariances() throws Exception {
        double[][] coefficients = { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        UnivariateFunction function = new PolynomialFunctions.QuadraticFunction(coefficients);
        double[] target = {4.0, 16.0, 25.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {1.0, 1.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // This is an empty implementation, as we're only testing the covariance matrix
                return new PointValuePair(startPoint, 0.0);
            }
        };

        optimizer.optimize(function, target, weights, startPoint);

        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(new RealArray2D(2, 2) {{
            setEntry(0, 0, 1.0);
            setEntry(0, 1, 0.0);
            setEntry(1, 0, 0.0);
            setEntry(1, 1, 1.0);
        }});

        RealMatrix actualCovarianceMatrix = optimizer.getCovariances();

        assertArrayEquals(expectedCovarianceMatrix.getData(), actualCovarianceMatrix.getData(), 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        double[][] coefficients = { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        UnivariateFunction function = new PolynomialFunctions.QuadraticFunction(coefficients);
        double[] target = {4.0, 16.0, 25.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {1.0, 1.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // This is an empty implementation, as we're only testing the error guessing
                return new PointValuePair(startPoint, 0.0);
            }
        };

        optimizer.optimize(function, target, weights, startPoint);

        double[] expectedErrors = {Math.sqrt(1.0), Math.sqrt(1.0)};
        double[] actualErrors = optimizer.guessParametersErrors();

        assertArrayEquals(expectedErrors, actualErrors, 0.001);
    }

    @Test
    public void testChiSquare() throws Exception {
        double[][] coefficients = { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        UnivariateFunction function = new PolynomialFunctions.QuadraticFunction(coefficients);
        double[] target = {4.0, 16.0, 25.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {1.0, 1.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // This is an empty implementation, as we're only testing the chi-square value
                return new PointValuePair(startPoint, 0.0);
            }
        };

        optimizer.optimize(function, target, weights, startPoint);

        double expectedChiSquare = 24.0;
        double actualChiSquare = optimizer.getChiSquare();

        assertEquals(expectedChiSquare, actualChiSquare, 0.001);
    }

    @Test
    public void testRMS() throws Exception {
        double[][] coefficients = { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        UnivariateFunction function = new PolynomialFunctions.QuadraticFunction(coefficients);
        double[] target = {4.0, 16.0, 25.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {1.0, 1.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // This is an empty implementation, as we're only testing the RMS value
                return new PointValuePair(startPoint, 0.0);
            }
        };

        optimizer.optimize(function, target, weights, startPoint);

        double expectedRMS = 4.0;
        double actualRMS = optimizer.getRMS();

        assertEquals(expectedRMS, actualRMS, 0.001);
    }

    @Test
    public void testUpdateResidualsAndCost() throws Exception {
        double[][] coefficients = { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        UnivariateFunction function = new PolynomialFunctions.QuadraticFunction(coefficients);
        double[] target = {4.0, 16.0, 25.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {1.0, 1.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // This is an empty implementation, as we're only testing the residuals and cost update
                return new PointValuePair(startPoint, 0.0);
            }
        };

        optimizer.optimize(function, target, weights, startPoint);

        double[] expectedResiduals = {0.6, 12.0, 19.0};
        double expectedCost = Math.sqrt(324.0);

        double[] actualResiduals = optimizer.residuals;
        double actualCost = optimizer.cost;

        assertArrayEquals(expectedResiduals, actualResiduals, 0.001);
        assertEquals(expectedCost, actualCost, 0.001);
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        double[][] coefficients = { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        UnivariateFunction function = new PolynomialFunctions.QuadraticFunction(coefficients);
        double[] target = {4.0, 16.0, 25.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {1.0, 1.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // This is an empty implementation, as we're only testing the jacobian update
                return new PointValuePair(startPoint, 0.0);
            }
        };

        optimizer.optimize(function, target, weights, startPoint);

        double[][] expectedJacobian = { {3.0, 2.0}, {6.0, 4.0}, {9.0, 6.0} };

        double[][] actualJacobian = optimizer.jacobian;

        assertArrayEquals(expectedJacobian, actualJacobian, 0.001);
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceeded() throws Exception {
        double[][] coefficients = { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        UnivariateFunction function = new PolynomialFunctions.QuadraticFunction(coefficients);
        double[] target = {4.0, 16.0, 25.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {1.0, 1.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return new PointValuePair(startPoint, 0.0);
            }

            @Override
            public int getMaxIterations() {
                return 0;
            }
        };

        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() throws Exception {
        double[][] coefficients = { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        UnivariateFunction function = new PolynomialFunctions.QuadraticFunction(coefficients);
        double[] target = {4.0, 16.0, 25.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {1.0, 1.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return new PointValuePair(startPoint, 0.0);
            }

            @Override
            public int getMaxEvaluations() {
                return 0;
            }
        };

        optimizer.optimize(function, target, weights, startPoint);
    }
}
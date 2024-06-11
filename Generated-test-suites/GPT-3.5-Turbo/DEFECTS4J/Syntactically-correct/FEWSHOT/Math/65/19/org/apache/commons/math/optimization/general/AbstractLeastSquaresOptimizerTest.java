package org.apache.commons.math.optimization.general;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.OptimizationException;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.exception.LocalizedFormats;
import org.apache.commons.math.optimization.VectorialPointValuePair;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Test
    public void testGetRMS() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        optimizer.residuals = residuals;
        optimizer.residualsWeights = weights;

        assertEquals(Math.sqrt((1.0*1.0 + 2.0*2.0 + 3.0*3.0) / 3), optimizer.getRMS(), 1e-8);
    }

    @Test
    public void testGetChiSquare() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };

        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        optimizer.residuals = residuals;
        optimizer.residualsWeights = weights;

        assertEquals(1.0*1.0 + 2.0*2.0 + 3.0*3.0, optimizer.getChiSquare(), 1e-8);
    }

    @Test(expected = OptimizationException.class)
    public void testGetCovariancesInvalidMatrixException() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };
        double[][] jacobian = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        optimizer.jacobian = jacobian;

        optimizer.getCovariances();
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };
        double[] covariances = {1.0, 4.0, 9.0};
        optimizer.getCovariances = () -> covariances;

        double[] expectedErrors = {Math.sqrt(1.0) * Math.sqrt(1.0 + 2.0 + 3.0), Math.sqrt(4.0) * Math.sqrt(1.0 + 2.0 + 3.0), Math.sqrt(9.0) * Math.sqrt(1.0 + 2.0 + 3.0)};
        assertArrayEquals(expectedErrors, optimizer.guessParametersErrors(), 1e-8);
    }
}
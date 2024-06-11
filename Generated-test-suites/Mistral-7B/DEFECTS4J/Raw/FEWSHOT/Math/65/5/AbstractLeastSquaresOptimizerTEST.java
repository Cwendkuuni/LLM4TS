package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                // This test doesn't check the optimization result, just the RMS value
                return null;
            }
        };

        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[0] + x[1] * x[1] + x[2] * x[2],
                x -> x[0] * x[1],
                x -> x[1] * x[2]
        }, target, weights, startPoint);

        double rms = optimizer.getRMS();
        double expectedRms = Math.sqrt(5.0 / 3.0);

        assertEquals(expectedRms, rms, 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                // This test doesn't check the optimization result, just the chi-square value
                return null;
            }
        };

        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[0] + x[1] * x[1] + x[2] * x[2],
                x -> x[0] * x[1],
                x -> x[1] * x[2]
        }, target, weights, startPoint);

        double chiSquare = optimizer.getChiSquare();
        double expectedChiSquare = 5.0;

        assertEquals(expectedChiSquare, chiSquare, 1e-6);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                // This test doesn't check the optimization result, just the errors
                return null;
            }
        };

        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[0] + x[1] * x[1] + x[2] * x[2],
                x -> x[0] * x[1],
                x -> x[1] * x[2]
        }, target, weights, startPoint);

        double[] errors = optimizer.guessParametersErrors();
        double[] expectedErrors = new double[]{Math.sqrt(1.0 / 3.0), Math.sqrt(1.0 / 3.0), Math.sqrt(1.0 / 3.0)};

        assertArrayEquals(expectedErrors, errors);
    }

    @Test
    public void testCovariances() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                // This test doesn't check the optimization result, just the covariance matrix
                return null;
            }
        };

        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[0] + x[1] * x[1] + x[2] * x[2],
                x -> x[0] * x[1],
                x -> x[1] * x[2]
        }, target, weights, startPoint);

        double[][] covariances = optimizer.getCovariances();
        double[][] expectedCovariances = new double[][]{{1.0 / 3.0, 0.0, 0.0}, {0.0, 1.0 / 3.0, 0.0}, {0.0, 0.0, 1.0 / 3.0}};

        assertArrayEquals(expectedCovariances, covariances);
    }

    @Test(expected = OptimizationException.class)
    public void testSingularProblem() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                // This test should throw an OptimizationException due to a singular problem
                return null;
            }
        };

        double[] target = new double[]{1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[1],
                x -> x[1] * x[0]
        }, target, weights, startPoint);

        optimizer.getCovariances();
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                // This test doesn't check the optimization result, just the jacobian
                return null;
            }
        };

        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[0] + x[1] * x[1] + x[2] * x[2],
                x -> x[0] * x[1],
                x -> x[1] * x[2]
        }, target, weights, startPoint);

        double[][] expectedJacobian = new double[][]{{3.0 * startPoint[0], 1.0 * startPoint[1], 1.0 * startPoint[2]},
                {1.0 * startPoint[0], 1.0 * startPoint[1], 1.0 * startPoint[2]},
                {1.0 * startPoint[0], 1.0 * startPoint[1], 1.0 * startPoint[2]}};

        optimizer.updateJacobian();

        RealArray2D jacobianMatrix = new Array2DRowRealMatrix(3, 3).getData();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedJacobian[i][j], jacobianMatrix.getEntry(i, j), 1e-6);
            }
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                throw new FunctionEvaluationException("Test Error");
            }
        };

        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[0] + x[1] * x[1] + x[2] * x[2],
                x -> x[0] * x[1],
                x -> x[1] * x[2]
        }, target, weights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                return null;
            }
        };

        double[] target = new double[]{1.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[0] + x[1] * x[1],
                x -> x[0] * x[1]
        }, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceededException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                return null;
            }
        };

        optimizer.setMaxIterations(0);

        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[0] + x[1] * x[1] + x[2] * x[2],
                x -> x[0] * x[1],
                x -> x[1] * x[2]
        }, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testMaxEvaluationsExceededException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws Exception {
                return null;
            }
        };

        optimizer.setMaxEvaluations(0);

        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(new UnivariateFunction[] {
                x -> x[0] * x[0] + x[1] * x[1] + x[2] * x[2],
                x -> x[0] * x[1],
                x -> x[1] * x[2]
        }, target, weights, startPoint);
    }
}
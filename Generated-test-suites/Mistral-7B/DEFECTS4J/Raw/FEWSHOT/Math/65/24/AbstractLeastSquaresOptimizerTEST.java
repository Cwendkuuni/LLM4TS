package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.E;
import org.junit.Test;

import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.optimization.VectorialPointValuePair;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.5, 3.5, 4.5};
        double[] weights = {1.0, 1.0, 1.0};

        optimizer.optimize(new Function(x, y), y, weights, x);

        assertEquals(2.5155172413793105, optimizer.getRMS(), 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.5, 3.5, 4.5};
        double[] weights = {1.0, 1.0, 1.0};

        optimizer.optimize(new Function(x, y), y, weights, x);

        assertEquals(6.999999999999999, optimizer.getChiSquare(), 1e-6);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.5, 3.5, 4.5};
        double[] weights = {1.0, 1.0, 1.0};

        optimizer.optimize(new Function(x, y), y, weights, x);

        double[][] covariances = optimizer.getCovariances();

        assertEquals(3, covariances.length);
        assertEquals(3, covariances[0].length);
        assertEquals(3, covariances[1].length);
        assertEquals(3, covariances[2].length);

        // Check that the covariance matrix is symetric and positive-definite
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <= i; j++) {
                assertEquals(covariances[i][j], covariances[j][i], 1e-6);
                assertTrue(covariances[i][i] > 0);
            }
        }

        // Check that the diagonal elements are the standard deviations
        double stdDev[] = {E.EPSILON, E.SQRT_2_PI * Math.sqrt(2.0), 2.0 * E.SQRT_2_PI};
        for (int i = 0; i < 3; i++) {
            assertEquals(stdDev[i], Math.sqrt(covariances[i][i]), 1e-6);
        }
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.5, 3.5, 4.5};
        double[] weights = {1.0, 1.0, 1.0};

        optimizer.optimize(new Function(x, y), y, weights, x);

        double[] errors = optimizer.guessParametersErrors();

        assertArrayEquals(new double[]{E.EPSILON, E.SQRT_2_PI, 2.0 * E.SQRT_2_PI}, errors, 1e-6);
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.5, 3.5, 4.5};
        double[] weights = {1.0, 1.0, 1.0};
        double[] targetValues = y.clone();

        optimizer.optimize(new Function(x, y), y, weights, x);

        optimizer.updateResidualsAndCost();

        assertEquals(2.5155172413793105, optimizer.cost, 1e-6);
        assertArrayEquals(y, optimizer.residuals);
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.5, 3.5, 4.5};
        double[] weights = {1.0, 1.0, 1.0};
        double[] targetValues = y.clone();

        optimizer.optimize(new Function(x, y), y, weights, x);

        optimizer.updateJacobian();

        assertEquals(3, optimizer.jacobian.length);
        assertEquals(3, optimizer.jacobian[0].length);
        assertEquals(3, optimizer.jacobian[1].length);
        assertEquals(3, optimizer.jacobian[2].length);

        // Check that the Jacobian matrix has the correct values
        double[][] jacobianExpected = {
                {-0.043301271106526, 0.019843137418991, 0.011825783252232},
                {0.019843137418991, -0.043301271106526, 0.011825783252232},
                {0.011825783252232, 0.011825783252232, -0.023652540504536}
        };

        for (int i = 0; i < 3; i++) {
            assertArrayEquals(jacobianExpected[i], optimizer.jacobian[i], 1e-6);
        }
    }

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        optimizer.setMaxIterations(5);

        assertEquals(5, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        optimizer.setMaxEvaluations(5);

        assertEquals(5, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        VectorialConvergenceChecker checker = new VectorialConvergenceChecker() {
            @Override
            public boolean isConverged(RealVector x, RealVector f, double stepSize) {
                return false;
            }
        };

        optimizer.setConvergenceChecker(checker);

        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        optimizer.setMaxIterations(5);

        assertEquals(0, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceededException() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        optimizer.setMaxIterations(1);
        optimizer.optimize(new Function(new double[]{1.0, 2.0}, new double[]{1.0, 2.0}),
                new double[]{1.0, 2.0}, new double[]{1.0, 1.0}, new double[]{1.0, 2.0});

        optimizer.incrementIterationsCounter();
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceededException() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                try {
                    updateResidualsAndCost();
                } catch (FunctionEvaluationException e) {
                    throw e;
                }

                updateJacobian();
                return null;
            }
        };

        optimizer.setMaxEvaluations(1);
        optimizer.optimize(new Function(new double[]{1.0, 2.0}, new double[]{1.0, 2.0}),
                new double[]{1.0, 2.0}, new double[]{1.0, 1.0}, new double[]{1.0, 2.0});

        optimizer.updateResidualsAndCost();
    }

    @Test
    public void testSetStartPoint() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // not implemented here
                return null;
            }
        };

        double[] startPoint = {1.0, 2.0, 3.0};

        optimizer.setStartPoint(startPoint);

        assertEquals(startPoint, optimizer.point);
    }

    private static class Function implements DifferentiableMultivariateVectorialFunction {
        private final double[] x;
        private final double[] y;

        public Function(double[] x, double[] y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double value(double[] point) {
            // not implemented here
            return 0;
        }

        @Override
        public RealMatrix jacobian(double[] point) throws FunctionEvaluationException {
            throw new UnsupportedOperationException();
        }
    }
}

class FunctionEvaluationException extends Exception {
    private static final long serialVersionUID = 51044442801L;

    public FunctionEvaluationException(double[] point, String message, Object... args) {
        super(String.format(message, args), point);
    }
}

class MaxEvaluationsExceededException extends OptimizationException {
    private static final long serialVersionUID = 2312472498166944123L;

    public MaxEvaluationsExceededException(int maxEvaluations) {
        super(LocalizedFormats.MAX_EVALUATIONS_EXCEEDED, maxEvaluations);
    }
}

class MaxIterationsExceededException extends OptimizationException {
    private static final long serialVersionUID = -8005521891126834425L;

    public MaxIterationsExceededException(int maxIterations) {
        super(LocalizedFormats.MAX_ITERATIONS_EXCEEDED, maxIterations);
    }
}
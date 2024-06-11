package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNullArray;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVector;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testOptimizeWithInvalidFunction() {
        new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                // should not be called
                return null;
            }
        }.optimize(new Object() {}, new double[] {}, new double[] {}, new double[1]);
    }

    @Test
    public void testGettersAndSetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        optimizer.setMaxIterations(10);
        assertEquals(10, optimizer.getMaxIterations());
        assertEquals(0, optimizer.getIterations());
        optimizer.incrementIterationsCounter();
        assertEquals(1, optimizer.getIterations());
        optimizer.setMaxIterations(20);
        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
        assertEquals(3, optimizer.getIterations());
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.updateJacobian();
        assertNull(optimizer.jacobian);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianWithInvalidFunction() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.function = null;
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.updateResidualsAndCost();
        assertNull(optimizer.objective);
        assertNull(optimizer.residuals);
        assertNull(optimizer.cost);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateResidualsAndCostWithInvalidFunction() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.function = null;
        optimizer.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[] {1, 2, 3};
        double[] residuals = new double[] {1.5, 2.5, 3.5};
        double[] weights = new double[] {1, 1, 1};
        optimizer.targetValues = target.clone();
        optimizer.residualsWeights = weights.clone();
        optimizer.objective = new double[] {1, 2, 3};
        optimizer.residuals = residuals.clone();
        optimizer.cost = 10;
        assertEquals(Math.sqrt(10 / 3.0), optimizer.getRMS(), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[] {1, 2, 3};
        double[] residuals = new double[] {1.5, 2.5, 3.5};
        double[] weights = new double[] {1 / 3.0, 1 / 3.0, 1 / 3.0};
        optimizer.targetValues = target.clone();
        optimizer.residualsWeights = weights.clone();
        optimizer.objective = new double[] {1, 2, 3};
        optimizer.residuals = residuals.clone();
        optimizer.cost = 10;
        assertEquals(10, optimizer.getChiSquare(), 0.001);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[][] jacobian = new double[][] {
            {1, 2},
            {3, 4},
            {5, 6}
        };
        optimizer.jacobian = jacobian.clone();
        optimizer.cols = 2;
        optimizer.rows = 3;
        optimizer.updateJacobian();
        double[][] covar = optimizer.getCovariances();
        assertNotNullArray(covar);
        assertArrayEquals(new double[][] {{1.0 / 6.0, -1.0 / 6.0},
                                          {-1.0 / 6.0, 1.0 / 6.0}}, covar, 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testGetCovariancesWithInvalidJacobian() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[][] jacobian = new double[][] {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
        optimizer.jacobian = jacobian.clone();
        optimizer.cols = 2;
        optimizer.rows = 4;
        optimizer.updateJacobian();
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[] {1, 2, 3};
        double[] residuals = new double[] {1.5, 2.5, 3.5};
        double[] weights = new double[] {1 / 3.0, 1 / 3.0, 1 / 3.0};
        optimizer.targetValues = target.clone();
        optimizer.residualsWeights = weights.clone();
        optimizer.objective = new double[] {1, 2, 3};
        optimizer.residuals = residuals.clone();
        optimizer.cost = 10;
        double[] errors = optimizer.guessParametersErrors();
        assertNotNullArray(errors);
        assertEquals(1.0 / Math.sqrt(3.0), errors[0], 0.001);
        assertEquals(1.0 / Math.sqrt(3.0), errors[1], 0.001);
        assertEquals(1.0 / Math.sqrt(3.0), errors[2], 0.001);
    }

    @Test
    public void testOptimizeWithSimpleVectorialValueChecker() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException {
                return new PointValuePair(new SimpleVector(new double[] {1.0, 2.0}), Double.NaN);
            }
        };
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0e-6, 1.0e-6));
        optimizer.setMaxIterations(10);
        optimizer.optimize(new SimpleVectorialFunction() {
            @Override
            public double value(double[] x) {
                return 2.0 * (x[0] * x[0] + x[1] * x[1]);
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[] {4.0 * x[0], 4.0 * x[1]};
            }
        }, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {1.0, 1.0});
        assertEquals(new SimpleVector(new double[] {0.5, 0.5}), optimizer.point, 0.001);
        assertEquals(-1.0, optimizer.objective[0], 0.001);
        assertEquals(-1.0, optimizer.objective[1], 0.001);
    }

    @Test
    public void testOptimizeWithCustomVectorialValueChecker() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException {
                return new PointValuePair(new SimpleVector(new double[] {1.0, 2.0}), new SimpleVector(new double[] {3.0, 4.0}));
            }
        };
        optimizer.setConvergenceChecker(new CustomVectorialValueChecker(new double[] {1.0e-6, 1.0e-6}, 10));
        optimizer.setMaxIterations(10);
        optimizer.optimize(new SimpleVectorialFunction() {
            @Override
            public double value(double[] x) {
                return 2.0 * (x[0] * x[0] + x[1] * x[1]);
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[] {4.0 * x[0], 4.0 * x[1]};
            }
        }, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {1.0, 1.0});
        assertEquals(new SimpleVector(new double[] {0.5, 0.5}), optimizer.point, 0.001);
        assertEquals(-1.0, optimizer.objective[0], 0.001);
        assertEquals(-1.0, optimizer.objective[1], 0.001);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeWithMaxIterationsExceeded() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException {
                return new PointValuePair(new SimpleVector(new double[] {1.0, 2.0}), new SimpleVector(new double[] {3.0, 4.0}));
            }
        };
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0e-12, 1.0e-12));
        optimizer.setMaxIterations(1);
        optimizer.optimize(new SimpleVectorialFunction() {
            @Override
            public double value(double[] x) {
                return 2.0 * (x[0] * x[0] + x[1] * x[1]);
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[] {4.0 * x[0], 4.0 * x[1]};
            }
        }, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {1.0, 1.0});
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0e-12, 1.0e-12));
        optimizer.setMaxIterations(10);
        optimizer.optimize(new SimpleVectorialFunction() {
            @Override
            public double value(double[] x) {
                throw new FunctionEvaluationException("Test exception");
            }

            @Override
            public double[] gradient(double[] x) {
                throw new FunctionEvaluationException("Test exception");
            }
        }, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {1.0, 1.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidStartPoint() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0e-12, 1.0e-12));
        optimizer.setMaxIterations(10);
        optimizer.optimize(new SimpleVectorialFunction() {
            @Override
            public double value(double[] x) {
                return 2.0 * (x[0] * x[0] + x[1] * x[1]);
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[] {4.0 * x[0], 4.0 * x[1]};
            }
        }, new double[] {1.0, 1.0}, new double[] {1.0, 1.0}, new double[] {2.0, 2.0});
    }
}

class CustomVectorialValueChecker implements VectorialConvergenceChecker {
    private double[] tolerance;
    private int maxIterations;

    public CustomVectorialValueChecker(double[] tolerance, int maxIterations) {
        this.tolerance = tolerance;
        this.maxIterations = maxIterations;
    }

    @Override
    public boolean checkConvergence(double[] currentPoint, double[] objectiveValue, double[] functionValues,
                                    double[] gradientValues, int iteration) {
        if (iteration >= maxIterations) {
            return false;
        }
        for (int i = 0; i < gradientValues.length; i++) {
            if (Math.abs(gradientValues[i]) > tolerance[i]) {
                return false;
            }
        }
        return true;
    }
}

class SimpleVectorialFunction implements DifferentiableMultivariateVectorialFunction {
    @Override
    public double value(double[] x) {
        double value = 0;
        for (int i = 0; i < x.length; i++) {
            value += x[i] * x[i];
        }
        return value;
    }

    @Override
    public double[] gradient(double[] x) {
        double[] gradient = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            gradient[i] = 2.0 * x[i];
        }
        return gradient;
    }
}
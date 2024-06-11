package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.optim.AbstractVectorOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorOptimizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for AbstractLeastSquaresOptimizer class.
 */
public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };

        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };

        optimizer.setMaxEvaluations(100);
        Assert.assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };

        optimizer.setConvergenceChecker(new SimpleVectorOptimizer.SimpleVectorValueChecker());
        Assert.assertSame(optimizer.getConvergenceChecker(), optimizer.getConvergenceChecker());
    }

    @Test
    public void testIncrementIterationsCounter() throws OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.incrementIterationsCounter();
        Assert.assertEquals(1, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testIncrementIterationsCounter_maxIterationsExceeded()
        throws OptimizationException {

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.setMaxIterations(0);
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                return new AbstractVectorOptimizer() {
                    @Override
                    public double[] value(double[] x) {
                        return new double[]{Math.pow(x[0], 2) + Math.pow(x[1], 2)};
                    }

                    @Override
                    public double[] gradient(double[] x) {
                        return new double[]{2 * x[0], 2 * x[1]};
                    }
                };
            }
        };

        optimizer.updateJacobian();
        Assert.assertArrayEquals(new double[][]{
            {2.0, 2.0},
            {2.0, 2.0}
        }, optimizer.jacobian);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobian_functionEvaluationException()
        throws FunctionEvaluationException {

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                return new AbstractVectorOptimizer() {
                    @Override
                    public double[] value(double[] x) {
                        return new double[]{Math.pow(x[0], 2)};
                    }
                };
            }
        };

        double[] target = {1.0};
        double[] weights = {1.0};
        double[] startPoint = {0.0};

        optimizer.optimize(optimizer.function(), target, weights, startPoint);
        optimizer.updateResidualsAndCost();

        Assert.assertArrayEquals(new double[]{0.0}, optimizer.residuals);
        Assert.assertEquals(Math.sqrt(Math.pow(1.0, 2)), optimizer.cost, 1e-10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateResidualsAndCost_functionEvaluationException()
        throws FunctionEvaluationException {

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.optimize(optimizer.function(), new double[]{0.0}, new double[]{0.0}, new double[]{0.0});
        optimizer.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                return new AbstractVectorOptimizer() {
                    @Override
                    public double[] value(double[] x) {
                        return new double[]{Math.pow(x[0], 2), Math.pow(x[1], 2)};
                    }
                };
            }
        };

        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {1.0, 1.0};

        optimizer.optimize(optimizer.function(), target, weights, startPoint);

        Assert.assertEquals(Math.sqrt(2.0), optimizer.getRMS(), 1e-10);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                return new AbstractVectorOptimizer() {
                    @Override
                    public double[] value(double[] x) {
                        return new double[]{Math.pow(x[0], 2), Math.pow(x[1], 2)};
                    }
                };
            }
        };

        double[] target = {1.0, 1.0};
        double[] weights = {2.0, 2.0};
        double[] startPoint = {1.0, 1.0};

        optimizer.optimize(optimizer.function(), target, weights, startPoint);

        Assert.assertEquals(4.0, optimizer.getChiSquare(), 1e-10);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                return new AbstractVectorOptimizer() {
                    @Override
                    public double[] value(double[] x) {
                        return new double[]{Math.pow(x[0], 2), Math.pow(x[1], 2)};
                    }

                    @Override
                    public double[] gradient(double[] x) {
                        return new double[]{2 * x[0], 2 * x[1]};
                    }
                };
            }
        };

        double[] target = {1.0, 1.0};
        double[] weights = {2.0, 2.0};
        double[] startPoint = {1.0, 1.0};

        optimizer.optimize(optimizer.function(), target, weights, startPoint);

        double[][] covarianceMatrix = optimizer.getCovariances();

        Assert.assertEquals(2, covarianceMatrix.length);
        Assert.assertEquals(2, covarianceMatrix[0].length);
        Assert.assertEquals(2, covarianceMatrix[1].length);
        Assert.assertEquals(3.3333333333333335E-17, covarianceMatrix[0][0], 1e-10);
        Assert.assertEquals(3.3333333333333335E-17, covarianceMatrix[1][1], 1e-10);
        Assert.assertEquals(-1.3322676322676322E-16, covarianceMatrix[0][1], 1e-10);
        Assert.assertEquals(-1.3322676322676322E-16, covarianceMatrix[1][0], 1e-10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testGetCovariances_functionEvaluationException()
        throws FunctionEvaluationException, OptimizationException {

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                throw new UnsupportedOperationException();
            }
        };

        double[] target = {0.0, 0.0};
        double[] weights = {2.0, 2.0};
        double[] startPoint = {0.0, 0.0};

        optimizer.optimize(optimizer.function(), target, weights, startPoint);
        optimizer.getCovariances();
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                return new AbstractVectorOptimizer() {
                    @Override
                    public double[] value(double[] x) {
                        return new double[]{Math.pow(x[0], 2), Math.pow(x[1], 2)};
                    }

                    @Override
                    public double[] gradient(double[] x) {
                        return new double[]{2 * x[0], 2 * x[1]};
                    }
                };
            }
        };

        double[] target = {1.0, 1.0};
        double[] weights = {2.0, 2.0};
        double[] startPoint = {1.0, 1.0};

        optimizer.optimize(optimizer.function(), target, weights, startPoint);

        double[] errors = optimizer.guessParametersErrors();

        Assert.assertEquals(2, errors.length);
        Assert.assertEquals(1.5811388300841898, errors[0], 1e-10);
        Assert.assertEquals(1.5811388300841898, errors[1], 1e-10);
    }

    @Test(expected = OptimizationException.class)
    public void testGuessParametersErrors_noDegreesOfFreedom()
        throws FunctionEvaluationException, OptimizationException {

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected DifferentiableMultivariateVectorialFunction function() {
                return new AbstractVectorOptimizer() {
                    @Override
                    public double[] value(double[] x) {
                        return new double[]{Math.pow(x[0], 2), Math.pow(x[0], 2)};
                    }
                };
            }
        };

        double[] target = {1.0, 1.0};
        double[] weights = {2.0, 2.0};
        double[] startPoint = {1.0, 1.0};

        optimizer.optimize(optimizer.function(), target, weights, startPoint);
    }
}
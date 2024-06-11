package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer;
import org.junit.Test;

import static org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS;
import static org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer.SimpleConstructor;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testSimpleConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
    }

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(5);
        assertEquals(5, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxIterationsNegative() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        try {
            optimizer.setMaxIterations(-5);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker(1e-5);
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }

            @Override
            public double[] gradient(double x) {
                return new double[] {2 * x};
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{2 * x[0]};
            }
        };
        optimizer.optimize(function, new double[]{1, 1}, new double[]{1, 1}, new double[]{0, 0});
        assertEquals(2, optimizer.getJacobian().length);
        assertEquals(1, optimizer.getJacobian()[0].length);
        assertArrayEquals(
            new double[][] {{2 * 0.0, 2 * 0.0}},
            optimizer.getJacobian()
        );
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double[] value(double[] x) {
                double[] result = new double[2];
                result[0] = x[0] * x[0];
                result[1] = x[1] * x[1];
                return result;
            }
        };
        optimizer.optimize(function, new double[]{1, 1}, new double[]{1, 1}, new double[]{0, 0});
        assertEquals(2, optimizer.getResiduals().length);
        assertEquals(4, optimizer.getTargetValues().length);
        assertEquals(2, optimizer.getRows());
        assertEquals(2, optimizer.getCols());
        assertEquals(0, optimizer.getCost(), 1e-5);
    }

    @Test
    public void testGetRMS() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double[] value(double[] x) {
                double[] result = new double[2];
                result[0] = x[0] * x[0];
                result[1] = x[1] * x[1];
                return result;
            }
        };
        double[] target = new double[]{1, 1};
        double[] weights = new double[]{1, 1};
        double[] point = new double[]{0, 0};
        optimizer.optimize(function, target, weights, point);
        assertEquals(Math.sqrt(2), optimizer.getRMS(), 1e-5);
    }

    @Test
    public void testGetChiSquare() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double[] value(double[] x) {
                double[] result = new double[2];
                result[0] = x[0] * x[0];
                result[1] = x[1] * x[1];
                return result;
            }
        };
        double[] target = new double[]{1, 1};
        double[] weights = new double[]{1, 1};
        double[] point = new double[]{0, 0};
        optimizer.optimize(function, target, weights, point);
        assertEquals(2, optimizer.getChiSquare(), 1e-5);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double[] value(double[] x) {
                double[] result = new double[2];
                result[0] = x[0] * x[0];
                result[1] = x[1] * x[1];
                return result;
            }
        };
        double[] target = new double[]{1, 1};
        double[] weights = new double[]{1, 1};
        double[] point = new double[]{0, 0};
        optimizer.optimize(function, target, weights, point);
        double[] expectedErrors = new double[]{Math.sqrt(2), Math.sqrt(2)};
        assertArrayEquals(expectedErrors, optimizer.guessParametersErrors(), 1e-5);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double[] value(double[] x) {
                double[] result = new double[2];
                result[0] = x[0] * x[0];
                result[1] = x[1] * x[1];
                return result;
            }
        };
        double[] target = new double[]{1, 1};
        double[] weights = new double[]{1, 1};
        double[] point = new double[]{0, 0};
        optimizer.optimize(function, target, weights, point);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(
            new double[][]{
                {1, 0.5},
                {0.5, 1}
            }
        );
        RealMatrix actualCovariances = optimizer.getCovariances();
        assertEquals(expectedCovariances.getColumnDimension(), actualCovariances.getColumnDimension());
        assertEquals(expectedCovariances.getRowDimension(), actualCovariances.getRowDimension());
        assertEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testOptimizeLevenbergMarquardt() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                UnivariateFunction function = new UnivariateFunction() {
                    @Override
                    public double value(double x) {
                        return Math.pow(x - 2, 2);
                    }

                    @Override
                    public double[] gradient(double x) {
                        return new double[] {-2 * (x - 2)};
                    }
                };
                double[] target = new double[]{3};
                double[] weights = new double[]{1};
                double[] startPoint = new double[]{2};
                return new LevenbergMarquardtOptimizer(function, target, weights, startPoint, GoalType.MINIMIZE).optimize();
            }
        };
        PointValuePair expectedResult = new PointValuePair(new double[]{2.0, 0.0}, 0.0);
        PointValuePair actualResult = optimizer.optimize(function, target, weights, startPoint);
        assertEquals(expectedResult.getValue(), actualResult.getValue(), 1e-5);
        assertArrayEquals(expectedResult.getPoint(), actualResult.getPoint(), 1e-5);
    }

}
package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.Constants;
import org.junit.Test;

import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.optimization.SimpleVectorialValueChecker;
import org.apache.commons.math.optimization.VectorialPointValuePair;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testGettersAndSetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException {
                return new VectorialPointValuePair(new double[]{}, new double[]{});
            }
        };

        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        optimizer.setMaxIterations(2);
        assertEquals(2, optimizer.getMaxIterations());
        assertEquals(0, optimizer.getIterations());
        optimizer.incrementIterationsCounter();
        assertEquals(1, optimizer.getIterations());
        optimizer.setMaxEvaluations(1);
        assertEquals(1, optimizer.getMaxEvaluations());
        assertEquals(0, optimizer.getEvaluations());

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0, 2.0};
        double[] weights = {3.0, 4.0};
        double[] startPoint = {0.0, 0.0};

        VectorialPointValuePair optimizedValues = optimizer.optimize(function, target, weights, startPoint);

        assertEquals(optimizedValues.getValue().length, optimizer.getRows());
        assertEquals(optimizer.getColumns(), optimizedValues.getValue().length);
        assertEquals(optimizer.getRows(), optimizedValues.getPoint().length);
        assertEquals(optimizer.getColumns(), optimizedValues.getPoint().length);
        assertEquals(optimizer.getIterations(), optimizer.getEvaluations());
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] expectedJacobian = new double[][]{{-1.0, -1.0}, {-2.0, -2.0}};

        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        optimizer.setMaxIterations(2);
        optimizer.setMaxEvaluations(2);

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0, 2.0};
        double[] weights = {3.0, 4.0};
        double[] startPoint = {2.0, 3.0};

        optimizer.optimize(function, target, weights, startPoint);
        optimizer.updateJacobian();

        assertArrayEquals(expectedJacobian, optimizer.jacobian);
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[] expectedResiduals = {0.5, 1.0};
        double expectedCost = Math.sqrt(Constants.PI * Constants.PI / 2.0);

        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        optimizer.setMaxIterations(2);
        optimizer.setMaxEvaluations(2);

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0, 2.0};
        double[] weights = {3.0, 4.0};
        double[] startPoint = {1.5, 2.5};

        optimizer.optimize(function, target, weights, startPoint);
        optimizer.updateResidualsAndCost();

        assertArrayEquals(expectedResiduals, optimizer.residuals);
        assertEquals(expectedCost, optimizer.cost, 0.001);
    }

    @Test
    public void testGetRMS() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        optimizer.setMaxIterations(2);
        optimizer.setMaxEvaluations(2);

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0, 2.0};
        double[] weights = {3.0, 4.0};
        double[] startPoint = {1.5, 2.5};

        optimizer.optimize(function, target, weights, startPoint);
        optimizer.updateResidualsAndCost();

        assertEquals(optimizer.getRMS(), optimizer.cost, 0.001);
    }

    @Test
    public void testGetChiSquare() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        optimizer.setMaxIterations(2);
        optimizer.setMaxEvaluations(2);

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0, 2.0};
        double[] weights = {3.0, 4.0};
        double[] startPoint = {1.5, 2.5};

        optimizer.optimize(function, target, weights, startPoint);
        optimizer.updateResidualsAndCost();

        assertEquals(optimizer.getChiSquare(), optimizer.cost * optimizer.getRows(), 0.001);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        optimizer.setMaxIterations(2);
        optimizer.setMaxEvaluations(2);

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0, 2.0};
        double[] weights = {3.0, 4.0};
        double[] startPoint = {1.5, 2.5};

        optimizer.optimize(function, target, weights, startPoint);
        optimizer.updateJacobian();

        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{
                {0.4, -0.2},
                {-0.2, 0.6}});

        try {
            RealMatrix covariances = optimizer.getCovariances();
            assertEquals(expectedCovariances.getRowDimension(), covariances.getRowDimension());
            assertEquals(expectedCovariances.getColumnDimension(), covariances.getColumnDimension());
            assertArrayEquals(expectedCovariances.getData(), covariances.getData(), 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        optimizer.setMaxIterations(2);
        optimizer.setMaxEvaluations(2);

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0, 2.0};
        double[] weights = {3.0, 4.0};
        double[] startPoint = {1.5, 2.5};

        optimizer.optimize(function, target, weights, startPoint);
        optimizer.updateJacobian();

        double[] expectedErrors = {0.6, 0.6};
        double[] errors = optimizer.guessParametersErrors();

        assertArrayEquals(expectedErrors, errors, 0.001);
    }

    @Test
    public void testOptimizeThrowFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException {
                throw new FunctionEvaluationException(new RuntimeException("Test exception"));
            }
        };

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0};
        double[] weights = {};
        double[] startPoint = {0.0};

        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, target, weights, startPoint));
    }

    @Test
    public void testOptimizeThrowIllegalArgumentException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException {
                return new VectorialPointValuePair(new double[]{1.0}, new double[]{1.0});
            }

            @Override
            protected int getColumns() {
                return 2;
            }
        };

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0};
        double[] weights = {};
        double[] startPoint = {0.0, 0.0};

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, target, weights, startPoint));
    }

    @Test
    public void testOptimizeThrowMaxIterationsExceededException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException {
                return new VectorialPointValuePair(new double[]{1.0}, new double[]{1.0});
            }

            @Override
            public int getMaxIterations() {
                return 0;
            }
        };

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0};
        double[] weights = {};
        double[] startPoint = {0.0};

        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, target, weights, startPoint));
    }

    @Test
    public void testOptimizeThrowMaxEvaluationsExceededException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException {
                return new VectorialPointValuePair(new double[]{1.0}, new double[]{1.0});
            }

            @Override
            public int getMaxEvaluations() {
                return 0;
            }
        };

        DifferentiableMultivariateVectorialFunction function = mockFunction();
        double[] target = {1.0};
        double[] weights = {};
        double[] startPoint = {0.0};

        assertThrows(MaxEvaluationsExceededException.class, () -> optimizer.optimize(function, target, weights, startPoint));
    }

    private DifferentiableMultivariateVectorialFunction mockFunction() {
        return (f, x) -> new double[]{f.value(x)[0]};
    }

}
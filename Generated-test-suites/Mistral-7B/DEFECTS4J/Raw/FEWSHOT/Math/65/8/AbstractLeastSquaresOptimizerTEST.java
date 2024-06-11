package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.VectorialConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer.MaxIterationsExceededException;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer.FunctionEvaluationException;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testGettersAndSetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertNull(optimizer.getConvergenceChecker());
        assertNull(optimizer.getJacobian());
        assertNull(optimizer.getTargetValues());
        assertNull(optimizer.getResidualsWeights());
        assertNull(optimizer.getPoint());
        assertNull(optimizer.getObjective());
        assertNull(optimizer.getResiduals());
        assertNull(optimizer.getCost());
        assertNull(optimizer.getCovariances());
        assertNull(optimizer.getGuessParametersErrors());

        int maxIterations = 5;
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] residualsWeights = {1.0, 2.0, 3.0};
        double[] point = {0.1, 0.2, 0.3};
        double[] objective = {1.1, 2.1, 3.1};
        double[] residuals = {1.2, 2.2, 3.2};
        double cost = 5.0;
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        double[] guessErrors = {1.5, 0.75};

        optimizer.setMaxIterations(maxIterations);
        optimizer.setConvergenceChecker(checker);
        optimizer.setTargetValues(targetValues);
        optimizer.setResidualsWeights(residualsWeights);
        optimizer.setPoint(point);
        optimizer.setObjective(objective);
        optimizer.setResiduals(residuals);
        optimizer.setCost(cost);
        optimizer.setCovariances(covariances);
        optimizer.setGuessParametersErrors(guessErrors);

        assertEquals(maxIterations, optimizer.getMaxIterations());
        assertSame(checker, optimizer.getConvergenceChecker());
        assertArrayEquals(targetValues, optimizer.getTargetValues());
        assertArrayEquals(residualsWeights, optimizer.getResidualsWeights());
        assertSame(point, optimizer.getPoint());
        assertArrayEquals(objective, optimizer.getObjective());
        assertArrayEquals(residuals, optimizer.getResiduals());
        assertEquals(cost, optimizer.getCost(), 0.0);
        assertArrayEquals(covariances, optimizer.getCovariances());
        assertArrayEquals(guessErrors, optimizer.getGuessParametersErrors());
    }

    @Test
    public void testUpdateJacobianAndResiduals() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("doOptimize should not be called");
                return null;
            }
        };
        optimizer.setFunction(new TestFunction());
        double[] point = {1.0, 1.0};

        optimizer.updateJacobian();
        assertNotNull(optimizer.getJacobian());
        assertEquals(2, optimizer.getJacobian().length);
        assertEquals(2, optimizer.getJacobian()[0].length);
        assertEquals(2, optimizer.getJacobian()[1].length);

        optimizer.updateResidualsAndCost();
        assertNotNull(optimizer.getResiduals());
        assertEquals(2, optimizer.getResiduals().length);
        assertEquals(0.0, optimizer.getCost(), 0.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };
        optimizer.setMaxIterations(0);
        optimizer.setFunction(new TestFunction());
        double[] point = {1.0, 1.0};

        optimizer.optimize(optimizer.getFunction(), new double[2], new double[2], point);
        fail("MaxIterationsExceededException should be thrown");
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceededException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };
        optimizer.setMaxIterations(Integer.MAX_VALUE);
        optimizer.setMaxEvaluations(0);
        optimizer.setFunction(new TestFunction());
        double[] point = {1.0, 1.0};

        optimizer.optimize(optimizer.getFunction(), new double[2], new double[2], point);
        fail("MaxEvaluationsExceededException should be thrown");
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };
        optimizer.setFunction(new TestFunction());
        double[] point = {1.0, 1.0};

        try {
            optimizer.guessParametersErrors();
        } catch (FunctionEvaluationException e) {
            fail("GuessParametersErrors should not throw FunctionEvaluationException");
        } catch (OptimizationException e) {
            fail("GuessParametersErrors should not throw OptimizationException");
        }

        double[] errors = optimizer.guessParametersErrors();
        assertArrayEquals(new double[]{1.5, 0.75}, errors);
    }

    @Test
    public void testCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };
        optimizer.setFunction(new TestFunction());
        double[] point = {1.0, 1.0};

        double[][] covariances = optimizer.getCovariances();
        assertArrayEquals(new double[][]{
                {1.5, 0.5},
                {0.5, 1.5}
        }, covariances);
    }

    @Test
    public void testRMSAndChiSquare() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };
        optimizer.setFunction(new TestFunction());
        double[] point = {1.0, 1.0};

        optimizer.optimize(optimizer.getFunction(), new double[2], new double[2], point);

        double rms = optimizer.getRMS();
        assertEquals(Math.sqrt(2.0), rms, 0.0);

        double chiSquare = optimizer.getChiSquare();
        assertEquals(2.0, chiSquare, 0.0);
    }

    @Test
    public void testDoOptimizeWithLevenbergMarquardtOptimizer() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                LevenbergMarquardtOptimizer lmo = new LevenbergMarquardtOptimizer();
                lmo.setMaxIterations(100);
                lmo.setMaxEvaluations(Integer.MAX_VALUE);
                lmo.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6, GoalType.MINIMIZE));
                return lmo.optimize(new TestFunction(), new double[2], null, this.point);
            }
        };
        optimizer.setFunction(new TestFunction());
        double[] point = {1.0, 1.0};

        VectorialPointValuePair result = optimizer.optimize(optimizer.getFunction(), new double[2], new double[2], point);

        assertEquals(0.0, result.getValue()[0], 0.0);
        assertEquals(0.0, result.getValue()[1], 0.0);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.0);

        double[] covariances = optimizer.getCovariances();
        assertArrayEquals(new double[][]{
                {1.5, 0.5},
                {0.5, 1.5}
        }, covariances);

        double[] guessErrors = optimizer.getGuessParametersErrors();
        assertArrayEquals(new double[]{1.5, 0.75}, guessErrors);

        double rms = optimizer.getRMS();
        assertEquals(Math.sqrt(2.0), rms, 0.0);

        double chiSquare = optimizer.getChiSquare();
        assertEquals(2.0, chiSquare, 0.0);
    }
}

class TestFunction implements DifferentiableMultivariateVectorialFunction {

    @Override
    public double[] value(double[] x) {
        double[] result = new double[2];
        result[0] = x[0] * x[0] + x[1] * x[1];
        result[1] = Math.sin(x[0]) + Math.cos(x[1]);
        return result;
    }

    @Override
    public RealMatrix jacobian(double[] x)
            throws FunctionEvaluationException {
        double[][] jacobian = new double[2][2];
        jacobian[0][0] = 2.0 * x[0];
        jacobian[1][0] = 0.0;
        jacobian[1][1] = Math.cos(x[1]);
        return new Array2DRowRealMatrix(jacobian);
    }
}
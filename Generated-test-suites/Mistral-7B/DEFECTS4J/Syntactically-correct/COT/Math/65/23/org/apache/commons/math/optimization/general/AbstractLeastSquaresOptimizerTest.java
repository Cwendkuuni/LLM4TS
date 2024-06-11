package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.VectorialConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer.MaxIterationsExceededStatus;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer.MaxEvaluationsExceededStatus;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LevenbergMarquardtOptimizer.IterationStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class AbstractLeastSquaresOptimizerTest {

    private int maxIterations;
    private int maxEvaluations;
    private double[] startPoint;
    private double[] targetValues;
    private double[] residualsWeights;
    private Double[] functionValues;
    private AbstractLeastSquaresOptimizer optimizer;

    public AbstractLeastSquaresOptimizerTest(int maxIterations, int maxEvaluations, double[] startPoint,
                                            double[] targetValues, double[] residualsWeights, Double[] functionValues) {
        this.maxIterations = maxIterations;
        this.maxEvaluations = maxEvaluations;
        this.startPoint = startPoint;
        this.targetValues = targetValues;
        this.residualsWeights = residualsWeights;
        this.functionValues = functionValues;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {10, Integer.MAX_VALUE, new double[]{0.0, 0.0}, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new Double[]{1.0, 1.0}},
                {5, 10, new double[]{0.0, 0.0}, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new Double[]{1.0, 1.0}},
                {20, 5, new double[]{1.0, 1.0}, new double[]{2.0, 2.0}, new double[]{1.0, 1.0}, new Double[]{2.0, 2.0}},
                {15, 15, new double[]{2.0, 2.0}, new double[]{3.0, 3.0}, new double[]{1.0, 1.0}, new Double[]{3.0, 3.0}}
        });
    }

    @Test
    public void testOptimizeWithDefaultSettings() {
        optimizer = new LevenbergMarquardtOptimizer();

        try {
            PointValuePair result = optimizer.optimize(new QuadraticFunction(), targetValues, residualsWeights, startPoint);
            Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType());
            Assert.assertArrayEquals(targetValues, result.getPoint(), 0.001);
            Assert.assertEquals(functionValues[0], result.getValue(), 0.001);
        } catch (FunctionEvaluationException | OptimizationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithCustomSettings() {
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(maxIterations);
        optimizer.setMaxEvaluations(maxEvaluations);

        try {
            PointValuePair result = optimizer.optimize(new QuadraticFunction(), targetValues, residualsWeights, startPoint);
            Assert.assertEquals(GoalType.MINIMIZE, result.getGoalType());
            Assert.assertArrayEquals(targetValues, result.getPoint(), 0.001);
            Assert.assertEquals(functionValues[0], result.getValue(), 0.001);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(0);

        try {
            optimizer.optimize(new QuadraticFunction(), targetValues, residualsWeights, startPoint);
        } catch (MaxIterationsExceededException e) {
            Assert.assertEquals("Maximum number of iterations has been exceeded", e.getMessage());
        }
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() {
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxEvaluations(0);

        try {
            optimizer.optimize(new QuadraticFunction(), targetValues, residualsWeights, startPoint);
        } catch (MaxEvaluationsExceededException e) {
            Assert.assertEquals("Maximum number of function evaluations has been exceeded", e.getMessage());
        }
    }

    @Test(expected = UnconstrainedOptimizationException.class)
    public void testUnconstrainedOptimizationException() {
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(maxIterations);
        optimizer.setMaxEvaluations(maxEvaluations);

        try {
            optimizer.optimize(new CubicFunction(), targetValues, residualsWeights, startPoint);
        } catch (FunctionEvaluationException | OptimizationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGuessParametersErrors() {
        optimizer = new LevenbergMarquardtOptimizer();

        try {
            double[] errors = optimizer.guessParametersErrors();
            Assert.assertArrayEquals(new double[]{0.3, 0.3}, errors, 0.01);
        } catch (FunctionEvaluationException | OptimizationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGuessParametersErrorsWithInvalidStartPoint() {
        optimizer = new LevenbergMarquardtOptimizer();

        try {
            double[] errors = optimizer.guessParametersErrors();
            Assert.fail("Unexpected call to guessParametersErrors() with incorrect start point");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The number of measurements is lesser or equal to the number of parameters", e.getMessage());
        }
    }

    private static class QuadraticFunction implements DifferentiableMultivariateVectorialFunction {

        @Override
        public double[] value(double[] x) throws FunctionEvaluationException {
            double[] result = new double[x.length];
            for (int i = 0; i < x.length; ++i) {
                result[i] = Math.pow(x[i], 2);
            }
            return result;
        }

        @Override
        public double[] gradient(double[] x) throws FunctionEvaluationException {
            double[] result = new double[x.length];
            for (int i = 0; i < x.length; ++i) {
                result[i] = 2 * x[i];
            }
            return result;
        }

        @Override
        public MultivariateMatrixFunction jacobian() throws FunctionEvaluationException {
            return new DenseFunctionToMatrixFunction(this);
        }
    }

    private static class CubicFunction implements DifferentiableMultivariateVectorialFunction {

        @Override
        public double[] value(double[] x) throws FunctionEvaluationException {
            double[] result = new double[x.length];
            for (int i = 0; i < x.length; ++i) {
                result[i] = Math.pow(x[i], 3);
            }
            return result;
        }

        @Override
        public double[] gradient(double[] x) throws FunctionEvaluationException {
            double[] result = new double[x.length];
            for (int i = 0; i < x.length; ++i) {
                result[i] = 3 * Math.pow(x[i], 2);
            }
            return result;
        }

        @Override
        public MultivariateMatrixFunction jacobian() throws FunctionEvaluationException {
            return new DenseFunctionToMatrixFunction(this);
        }
    }

}
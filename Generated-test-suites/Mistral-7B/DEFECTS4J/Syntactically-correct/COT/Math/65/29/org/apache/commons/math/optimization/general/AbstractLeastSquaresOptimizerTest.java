package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialization.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.*;
import org.apache.commons.math3.optim.nonlinear.vector.linear.*;
import org.apache.commons.math3.optim.nonlinear.vector.linear.leastsquares.*;
import org.apache.commons.math3.optim.point.*;
import org.apache.commons.math3.optim.exception.*;
import org.junit.Test;

/**
 * Test the AbstractLeastSquaresOptimizer class
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test the constructor with default settings
     */
    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    /**
     * Test the setMaxIterations method
     */
    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(5);
        assertEquals(5, optimizer.getMaxIterations());
    }

    /**
     * Test the getIterations method
     */
    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getIterations());
    }

    /**
     * Test the setMaxEvaluations method
     */
    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(3);
        assertEquals(3, optimizer.getMaxEvaluations());
    }

    /**
     * Test the getMaxEvaluations method
     */
    @Test
    public void testGetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getMaxEvaluations());
    }

    /**
     * Test the getEvaluations method
     */
    @Test
    public void testGetEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getEvaluations());
    }

    /**
     * Test the getJacobianEvaluations method
     */
    @Test
    public void testGetJacobianEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getJacobianEvaluations());
    }

    /**
     * Test the setConvergenceChecker method
     */
    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
    }

    /**
     * Test the getConvergenceChecker method
     */
    @Test
    public void testGetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test the incrementIterationsCounter method
     */
    @Test(expected = OptimizationException.class)
    public void testIncrementIterationsCounterMaxIterationsExceeded() throws OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
    }

    /**
     * Test the updateJacobian method with valid input
     */
    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[][] jacobian = new double[2][3];
        optimizer.updateJacobian();
        assertEquals(2, optimizer.getRows());
        assertEquals(3, optimizer.getCols());
        assertArrayEquals(jacobian, optimizer.getJacobian());
    }

    /**
     * Test the updateJacobian method with invalid input
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianInvalidJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[][] jacobian = new double[2][4];
        optimizer.updateJacobian();
    }

    /**
     * Test the updateResidualsAndCost method with valid input
     */
    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] point = new double[]{0.0, 0.0};
        double[] objective = new double[]{3.0, 4.0};
        optimizer.updateResidualsAndCost();
        assertArrayEquals(objective, optimizer.getObjective());
        assertArrayEquals(target, optimizer.getTargetValues());
        assertArrayEquals(weights, optimizer.getResidualsWeights());
        assertEquals(Math.sqrt(5.0), optimizer.getCost(), 1e-6);
        assertEquals(Math.sqrt(5.0), optimizer.getRMS(), 1e-6);
        assertEquals(5.0, optimizer.getChiSquare(), 1e-6);
    }

    /**
     * Test the getCovariances method with valid input
     */
    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[][] covariances = new double[3][3];
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();
        optimizer.getCovariances();
        assertArrayEquals(covariances, optimizer.getCovariances());
    }

    /**
     * Test the guessParametersErrors method with valid input
     */
    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] errors = new double[3];
        optimizer.updateJacobian();
        optimizer.updateResidualsAndCost();
        optimizer.guessParametersErrors();
        assertArrayEquals(errors, optimizer.guessParametersErrors());
    }

    /**
     * Test the optimize method with valid input
     */
    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        DifferentiableMultivariateVectorFunction f = new LinearLeastSquaresProblemFunction(target, weights);
        VectorialPointValuePair result = optimizer.optimize(f, target, weights, startPoint);
        assertEquals(PointValuePair.FLAG_OPTIMAL, result.getFlag());
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-6);
        assertArrayEquals(new double[]{-0.5, -0.5}, optimizer.getResiduals(), 1e-6);
        assertEquals(Math.sqrt(2.0), optimizer.getCost(), 1e-6);
        assertEquals(Math.sqrt(2.0), optimizer.getRMS(), 1e-6);
        assertEquals(2.0, optimizer.getChiSquare(), 1e-6);
        double[][] covariances = optimizer.getCovariances();
        assertEquals(3, covariances.length);
        assertEquals(3, covariances[0].length);
        assertEquals(3, covariances[1].length);
        assertEquals(3, covariances[2].length);
    }

    /**
     * Test the optimize method with invalid input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidInput() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[]{1.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        DifferentiableMultivariateVectorFunction f = new LinearLeastSquaresProblemFunction(target, weights);
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test the optimize method with invalid convergence checker
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidConvergenceChecker() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        VectorialConvergenceChecker convergenceChecker = new ConvergenceChecker() {
            @Override
            public boolean isConverged(PointValuePair current, PointValuePair previous) {
                throw new RuntimeException();
            }
        };
        optimizer.setConvergenceChecker(convergenceChecker);
        DifferentiableMultivariateVectorFunction f = new LinearLeastSquaresProblemFunction(target, weights);
        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test the optimize method with non-linear objective function
     */
    @Test(expected = OptimizationException.class)
    public void testOptimizeNonLinearObjectiveFunction() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        DifferentiableMultivariateScalarFunction f = new ScalarFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * Math.sin(x[1]);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[]{Math.cos(x[1]), Math.sin(x[1])};
                grad[0] *= x[0];
                return grad;
            }
        };
        optimizer.optimize(new ScalarFunctionToDifferentiableMultivariateVectorFunction(f), target, weights, startPoint);
    }
}
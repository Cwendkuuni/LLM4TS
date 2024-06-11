package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialFunction;
import org.apache.commons.math3.optim.VectorOptimizer;
import org.apache.commons.math3.optim.nonlinear.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.optim.nonlinear.leastsquares.LevenbergMarquardtOptimizer;
import org.junit.Test;

/**
 * Tests for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test the optimization with a function that has a global minimum.
     * @throws Exception if an error occurs during the optimization
     */
    @Test
    public void testOptimizationWithMinimum() throws Exception {
        // prepare test data
        double[] coefficients = new double[] { 1.0, 2.0, 3.0 };
        double[] x = new double[] { 4.0, 5.0, 6.0 };
        double[] target = new double[] { 10.0, 21.0, 32.0 };
        LeastSquaresProblem problem = new LeastSquaresProblem(new SimpleVectorialFunction(x), target);

        // perform optimization
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        PointValuePair solution = optimizer.optimize(problem.getFunction(), problem.getParameters(), null, problem.getStartPoint());

        // check results
        assertArrayEquals(problem.getParameters(), solution.getPoint(), 1e-6);
        assertEquals(0.0, problem.getFunctionValue(solution.getPoint()), 1e-6);
    }

    /**
     * Test the optimization with a function that has no solution.
     * @throws Exception if an error occurs during the optimization
     */
    @Test(expected = OptimizationException.class)
    public void testOptimizationWithNoSolution() throws Exception {
        // prepare test data
        double[] coefficients = new double[] { 1.0, 2.0, 3.0 };
        double[] x = new double[] { 4.0, 5.0, 6.0 };
        double[] target = new double[] { 1.0, 1.0, 1.0 };
        LeastSquaresProblem problem = new LeastSquaresProblem(new SimpleVectorialFunction(x), target);

        // perform optimization
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(problem.getFunction(), problem.getParameters(), null, problem.getStartPoint());

        fail("The optimization should not find a solution for this problem");
    }

    /**
     * Test the optimization with a function that has multiple solutions.
     * @throws Exception if an error occurs during the optimization
     */
    @Test
    public void testOptimizationWithMultipleSolutions() throws Exception {
        // prepare test data
        double[] coefficients = new double[] { 1.0, 2.0, 3.0 };
        double[] x = new double[] { 4.0, 5.0, 6.0 };
        double[] target = new double[] { 1.0, 2.0, 3.0 };
        LeastSquaresProblem problem = new LeastSquaresProblem(new SimpleVectorialFunction(x), target);

        // perform optimization with different starting points
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        PointValuePair solution1 = optimizer.optimize(problem.getFunction(), problem.getParameters(), null, new double[] { 1.0, 1.0, 1.0 });
        PointValuePair solution2 = optimizer.optimize(problem.getFunction(), problem.getParameters(), null, new double[] { 7.0, 7.0, 7.0 });

        // check results
        assertArrayEquals(problem.getParameters(), solution1.getPoint(), 1e-6);
        assertEquals(0.0, problem.getFunctionValue(solution1.getPoint()), 1e-6);

        assertArrayEquals(problem.getParameters(), solution2.getPoint(), 1e-6);
        assertEquals(0.0, problem.getFunctionValue(solution2.getPoint()), 1e-6);
    }

    /**
     * Test the optimization with a non-differentiable function.
     * @throws Exception if an error occurs during the optimization
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizationWithNonDifferentiableFunction() throws Exception {
        // prepare test data
        double[] coefficients = new double[] { 1.0, 2.0 };
        double[] x = new double[] { 0.0, 0.0 };
        double[] target = new double[] { 1.0, 1.0 };
        LeastSquaresProblem problem = new LeastSquaresProblem(new SimpleVectorialFunction(x) {
            @Override
            public double value(double[] x) {
                if (x[0] == 0.0 && x[1] == 0.0) {
                    throw new ArithmeticException("Division by zero");
                }
                return x[0] * x[1];
            }
        }, target);

        // perform optimization
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(problem.getFunction(), problem.getParameters(), null, problem.getStartPoint());

        fail("The optimization should fail with a FunctionEvaluationException for this problem");
    }

    /**
     * Test the optimization with a function that throws an exception.
     * @throws Exception if an error occurs during the optimization
     */
    @Test(expected = OptimizationException.class)
    public void testOptimizationWithThrowingFunction() throws Exception {
        // prepare test data
        double[] coefficients = new double[] { 1.0, 2.0, 3.0 };
        double[] x = new double[] { 4.0, 5.0, 6.0 };
        double[] target = new double[] { 1.0, 2.0, 3.0 };
        LeastSquaresProblem problem = new LeastSquaresProblem(new SimpleVectorialFunction(x) {
            @Override
            public double value(double[] x) {
                throw new OptimizationException("An exception occurred");
            }
        }, target);

        // perform optimization
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(problem.getFunction(), problem.getParameters(), null, problem.getStartPoint());

        fail("The optimization should fail with an OptimizationException for this problem");
    }

    /**
     * Test the optimization with a function that has a large number of parameters.
     * @throws Exception if an error occurs during the optimization
     */
    @Test
    public void testOptimizationWithLargeNumberOfParameters() throws Exception {
        // prepare test data
        int numParameters = 1000;
        double[] coefficients = new double[numParameters];
        for (int i = 0; i < numParameters; i++) {
            coefficients[i] = 1.0;
        }
        double[] x = new double[numParameters];
        double[] target = new double[numParameters];
        for (int i = 0; i < numParameters; i++) {
            target[i] = Math.random();
        }
        LeastSquaresProblem problem = new LeastSquaresProblem(new SimpleVectorialFunction(x), target);

        // perform optimization
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        PointValuePair solution = optimizer.optimize(problem.getFunction(), problem.getParameters(), null, problem.getStartPoint());

        // check results
        assertArrayEquals(problem.getParameters(), solution.getPoint(), 1e-6);
        assertEquals(0.0, problem.getFunctionValue(solution.getPoint()), 1e-6);
    }

    /**
     * Test the optimization with a function that has a large number of measurements.
     * @throws Exception if an error occurs during the optimization
     */
    @Test
    public void testOptimizationWithLargeNumberOfMeasurements() throws Exception {
        // prepare test data
        int numMeasurements = 1000000;
        double[] coefficients = new double[1];
        double[] x = new double[1];
        double[] target = new double[numMeasurements];
        for (int i = 0; i < numMeasurements; i++) {
            target[i] = Math.random();
        }
        LeastSquaresProblem problem = new LeastSquaresProblem(new SimpleVectorialFunction(x), target);

        // perform optimization
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        PointValuePair solution = optimizer.optimize(problem.getFunction(), problem.getParameters(), null, problem.getStartPoint());

        // check results
        assertArrayEquals(problem.getParameters(), solution.getPoint(), 1e-6);
        assertEquals(0.0, problem.getFunctionValue(solution.getPoint()), 1e-6);
    }

}
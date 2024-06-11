package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.ScaledVectorialFunction;
import org.apache.commons.math3.optim.nonlinear.VectorFunction;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtOptimizer.MaxIterationsExceededException;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtOptimizer.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtOptimizer.FunctionEvaluationException;
import org.apache.commons.math3.optim.nonlinear.leastsq.LevenbergMarquardtOptimizer.ConvergenceChecker;
import org.junit.Test;

/**
 * Tests for {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test optimization of a simple polynomial function.
     * @throws FunctionEvaluationException if the optimization fails
     * @throws MaxIterationsExceededException if the optimization doesn't converge
     * @throws MaxEvaluationsExceededException if the optimization exceeds the maximum
     * number of allowed evaluations
     */
    @Test
    public void testPolynomialOptimization()
        throws FunctionEvaluationException, MaxIterationsExceededException,
            MaxEvaluationsExceededException {

        // create a simple test function
        UnivariateFunction function = new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5);

        // set up the optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException,
                    IllegalArgumentException {

                // set up the objective function
                VectorFunction vectorFunction = new ScaledVectorialFunction(function,
                        1.0 / 10.0);

                // set up the initial point
                double[] startPoint = new double[] {-3.0, -3.0};

                // perform the optimization
                LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer(vectorFunction, startPoint,
                        new AveragedStartingPointGenerator(5));
                optimizer.setMaxIterations(100);
                optimizer.setMaxEvaluations(10000);
                optimizer.setConvergenceChecker(new ConvergenceChecker() {
                    @Override
                    public boolean isConverged(RealVector current, RealVector previous, double fVal) {
                        double precision = 1e-8;
                        return current.normInf() < precision || previous.normInf() < precision;
                    }
                });
                PointValuePair solution = optimizer.optimize();

                // check the solution
                double[] expectedSolution = new double[] {-1.0, -1.0};
                assertArrayEquals("Unexpected solution", expectedSolution, solution.getPoint(), 1e-8);
                assertEquals("Unexpected objective function value", -1.0, solution.getValue(), 1e-8);

                // return the solution
                return new PointValuePair(solution.getPoint(), solution.getValue());
            }
        };

        // perform the test
        PointValuePair solution = optimizer.optimize(function, new double[] {1.0, 2.0, 3.0, 4.0}, null, null);

        // check the solution
        double[] expectedSolution = new double[] {-1.0, -1.0};
        assertArrayEquals("Unexpected solution", expectedSolution, solution.getPoint(), 1e-8);
        assertEquals("Unexpected objective function value", -1.0, solution.getValue(), 1e-8);

    }

    /**
     * Test optimization with invalid input parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputParameters() {

        // create an instance of the optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        // test invalid number of target values
        try {
            optimizer.optimize(null, null, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception message", "The target values array cannot be null", e.getMessage());
        }

        // test invalid number of weights
        try {
            optimizer.optimize(null, new double[] {1.0}, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception message", "The weights array must have the same length as the target values array", e.getMessage());
        }

        // test invalid start point dimensions
        try {
            optimizer.optimize(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5), new double[] {1.0, 2.0}, null,
                    new double[] {-3.0});
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception message", "The start point array must have the same length as the number of unknowns", e.getMessage());
        }

    }

    /**
     * Test optimization with a function that throws an exception.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {

        // create a test function that throws an exception
        class TestFunction implements UnivariateFunction {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Test exception", x);
            }
        }

        // create an instance of the optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException,
                    IllegalArgumentException {
                throw new FunctionEvaluationException("Test exception");
            }
        };

        // set up the test function
        UnivariateFunction function = new TestFunction();

        // test optimization with the exception-throwing function
        try {
            optimizer.optimize(function, new double[] {1.0}, null, null);
        } catch (FunctionEvaluationException e) {
            assertEquals("Unexpected exception message", "Test exception", e.getMessage());
        }

    }

    /**
     * Test optimization with invalid convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConvergenceChecker() {

        // create an instance of the optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException,
                    IllegalArgumentException {

                // set up the objective function
                VectorFunction vectorFunction = new ScaledVectorialFunction(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5),
                        1.0 / 10.0);

                // set up the initial point
                double[] startPoint = new double[] {-3.0, -3.0};

                // perform the optimization with an invalid convergence checker
                LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer(vectorFunction, startPoint,
                        new AveragedStartingPointGenerator(5));
                optimizer.setMaxIterations(100);
                optimizer.setMaxEvaluations(10000);
                optimizer.setConvergenceChecker(null);
                try {
                    optimizer.optimize();
                } catch (Exception e) {
                    // expected exception
                }

                // check that the optimization did not return a solution
                assertNull("Unexpected optimization result", optimizer.getLastIterate());
                assertEquals("Unexpected objective function value", Double.NaN, optimizer.getFunctionValue(), 0);

                // return the null solution
                return new PointValuePair(null, Double.NaN);
            }
        };

        // test optimization with the invalid convergence checker
        assertNull("Unexpected optimization result", optimizer.optimize(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5),
                new double[] {1.0, 2.0}, null, null).getPoint());
        assertEquals("Unexpected optimization result", Double.NaN, optimizer.optimize(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5),
                new double[] {1.0, 2.0}, null, null).getValue(), 0);

    }

    /**
     * Test optimization with a function that doesn't have a jacobian.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFunctionWithoutJacobian() {

        // create an instance of the optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException,
                    IllegalArgumentException {

                // set up the objective function without jacobian
                VectorFunction vectorFunction = new ScaledVectorialFunction(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5),
                        1.0 / 10.0) {
                    @Override
                    public MultivariateMatrixFunction jacobian() {
                        throw new UnsupportedOperationException("Test exception");
                    }
                };

                // perform the optimization
                LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer(vectorFunction, new double[] {0.0},
                        new AveragedStartingPointGenerator(5));
                optimizer.setMaxIterations(100);
                optimizer.setMaxEvaluations(10000);
                try {
                    optimizer.optimize();
                } catch (Exception e) {
                    // expected exception
                }

                // check that the optimization did not return a solution
                assertNull("Unexpected optimization result", optimizer.getLastIterate());
                assertEquals("Unexpected objective function value", Double.NaN, optimizer.getFunctionValue(), 0);

                // return the null solution
                return new PointValuePair(null, Double.NaN);
            }
        };

        // test optimization with the function without jacobian
        assertNull("Unexpected optimization result", optimizer.optimize(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5),
                new double[] {1.0, 2.0}, null, null).getPoint());
        assertEquals("Unexpected optimization result", Double.NaN, optimizer.optimize(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5),
                new double[] {1.0, 2.0}, null, null).getValue(), 0);

    }

    /**
     * Test optimization with invalid jacobian.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidJacobian() {

        // create an instance of the optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException,
                    IllegalArgumentException {

                // set up the objective function with an invalid jacobian
                VectorFunction vectorFunction = new ScaledVectorialFunction(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5),
                        1.0 / 10.0) {
                    @Override
                    public MultivariateMatrixFunction jacobian() {
                        throw new ArithmeticException("Test exception");
                    }
                };

                // perform the optimization
                LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer(vectorFunction, new double[] {0.0},
                        new AveragedStartingPointGenerator(5));
                optimizer.setMaxIterations(100);
                optimizer.setMaxEvaluations(10000);
                try {
                    optimizer.optimize();
                } catch (Exception e) {
                    // expected exception
                }

                // check that the optimization did not return a solution
                assertNull("Unexpected optimization result", optimizer.getLastIterate());
                assertEquals("Unexpected objective function value", Double.NaN, optimizer.getFunctionValue(), 0);

                // return the null solution
                return new PointValuePair(null, Double.NaN);
            }
        };

        // test optimization with the invalid jacobian
        assertNull("Unexpected optimization result", optimizer.optimize(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5),
                new double[] {1.0, 2.0}, null, null).getPoint());
        assertEquals("Unexpected optimization result", Double.NaN, optimizer.optimize(new Polynomials.Quartic(0.1, 0.3, -2.0, 0.4, 1.0, -0.5),
                new double[] {1.0, 2.0}, null, null).getValue(), 0);

    }

}
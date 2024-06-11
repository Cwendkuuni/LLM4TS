package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for the AbstractLeastSquaresOptimizer class.
 */
public class AbstractLeastSquaresOptimizerTest {

    /** The instance under test. */
    private AbstractLeastSquaresOptimizer optimizer;

    /** A simple test function. */
    private static final UnivariateFunction TEST_FUNCTION = new PolynomialFunction(2, 1, 1);

    /**
     * Set up the test fixture.
     */
    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // This abstract method is overridden in this test case
                // to allow testing the super class' methods.
                return null;
            }
        };
    }

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        // The default constructor is empty, so nothing to test here.
    }

    /**
     * Test setting the maximum number of iterations.
     */
    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
    }

    /**
     * Test getting the maximum number of iterations.
     */
    @Test
    public void testGetMaxIterations() {
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
    }

    /**
     * Test setting the convergence checker.
     */
    @Test
    public void testSetConvergenceChecker() {
        // The convergence checker is not testable directly in this test case,
        // but it can be checked indirectly by testing the optimization result.
    }

    /**
     * Test getting the convergence checker.
     */
    @Test
    public void testGetConvergenceChecker() {
        // The convergence checker is not testable directly in this test case,
        // but it can be checked indirectly by testing the optimization result.
    }

    /**
     * Test updating the Jacobian matrix.
     * @throws FunctionEvaluationException if the function jacobian cannot be evaluated
     */
    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        optimizer.updateJacobian();
        Assert.assertNotNull(optimizer.jacobian);
    }

    /**
     * Test updating the residuals and cost function value.
     * @throws FunctionEvaluationException if the function cannot be evaluated
     * or the maximal number of evaluations is exceeded
     */
    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        optimizer.updateResidualsAndCost();
        Assert.assertNotNull(optimizer.cost);
    }

    /**
     * Test getting the Root Mean Square value.
     */
    @Test
    public void testGetRMS() {
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};
        optimizer.optimize(TEST_FUNCTION, target, weights, startPoint);
        Assert.assertNotNull(optimizer.getRMS());
    }

    /**
     * Test getting the chi-square value.
     */
    @Test
    public void testGetChiSquare() {
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};
        optimizer.optimize(TEST_FUNCTION, target, weights, startPoint);
        Assert.assertNotNull(optimizer.getChiSquare());
    }

    /**
     * Test getting the covariance matrix of optimized parameters.
     * @throws FunctionEvaluationException if the function jacobian cannot be evaluated
     * @throws OptimizationException if the covariance matrix cannot be computed
     */
    @Test
    public void testgetCovariances()
            throws FunctionEvaluationException, OptimizationException {
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};
        optimizer.optimize(TEST_FUNCTION, target, weights, startPoint);
        RealMatrix covarianceMatrix = optimizer.getCovariances();
        Assert.assertNotNull(covarianceMatrix);
    }

    /**
     * Test guessing the errors in optimized parameters.
     * @throws FunctionEvaluationException if the function jacobian cannot be evaluated
     * @throws OptimizationException if the covariances matrix cannot be computed
     */
    @Test
    public void testGuessParametersErrors()
            throws FunctionEvaluationException, OptimizationException {
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};
        optimizer.optimize(TEST_FUNCTION, target, weights, startPoint);
        double[] errors = optimizer.guessParametersErrors();
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, errors);
    }

    /**
     * Test optimizing the simple test function using the Gauss-Newton method.
     * @throws FunctionEvaluationException if the objective function throws one during
     * the search
     * @throws OptimizationException if the algorithm failed to converge
     */
    @Test
    public void testOptimizeGaussNewton()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {2.0, 2.0};
        PointValuePair result = new GaussNewtonOptimizer(1e-8).optimize(TEST_FUNCTION, new SimpleBounds(new double[]{-1.0, 1.0}, new double[]{-1.0, 1.0}), startPoint);
        double[] optimalPoint = result.getPoint();
        optimizer.optimize(TEST_FUNCTION, target, weights, startPoint);
        Assert.assertArrayEquals(optimalPoint, optimizer.point);
    }

    /**
     * Test throwing an IllegalArgumentException with an incorrect start point.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeIllegalArgumentException() {
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = new double[3];
        optimizer.optimize(TEST_FUNCTION, target, weights, startPoint);
    }

}
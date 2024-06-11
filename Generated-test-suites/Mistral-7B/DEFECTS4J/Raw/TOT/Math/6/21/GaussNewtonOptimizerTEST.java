package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    /** The Gauss-Newton optimizer to be tested. */
    private GaussNewtonOptimizer optimizer;

    /** The test objective function, f(x) = sum((x[i] - target[i])^2) for i from 0 to n. */
    private double[] target;
    private double[] expectedValue;

    /**
     * Sets up the test by creating a test optimizer and computing the
     * expected value for various test points.
     */
    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        target = new double[]{1.0, 2.0, 3.0};
        expectedValue = new double[]{0.0, 1.0, 2.5};
    }

    /**
     * Testing the solver with a simple test case.
     * The expected value is known beforehand.
     * @throws ConvergenceException if the optimizer fails to converge.
     */
    @Test
    public void testSimple() throws ConvergenceException {
        double[] startPoint = new double[]{2.0, 2.0, 2.0};
        PointValuePair solution = optimizer.optimize(target, startPoint);
        double[] actualValue = solution.getValue();
        Assert.assertArrayEquals("The solution should match the expected value.", expectedValue, actualValue, 1e-12);
    }

    /**
     * Testing the solver with a test case where the starting point is far from the solution.
     * The optimizer should converge to the expected value.
     * @throws ConvergenceException if the optimizer fails to converge.
     */
    @Test
    public void testFarFromExpected() throws ConvergenceException {
        double[] startPoint = new double[]{5.0, 5.0, 5.0};
        PointValuePair solution = optimizer.optimize(target, startPoint);
        double[] actualValue = solution.getValue();
        Assert.assertArrayEquals("The solution should match the expected value.", expectedValue, actualValue, 1e-12);
    }

    /**
     * Testing the solver with a test case where the starting point is the expected value.
     * The optimizer should converge to the expected value with no change.
     * @throws ConvergenceException if the optimizer fails to converge.
     */
    @Test
    public void testStartingAtExpected() throws ConvergenceException {
        double[] startPoint = new double[]{expectedValue[0], expectedValue[1], expectedValue[2]};
        PointValuePair solution = optimizer.optimize(target, startPoint);
        double[] actualValue = solution.getValue();
        Assert.assertArrayEquals("The solution should match the expected value.", expectedValue, actualValue, 1e-12);
    }

    /**
     * Testing the solver with a test case where the weight matrix is not diagonal.
     * The optimizer should throw an exception.
     */
    @Test(expected = NullArgumentException.class)
    public void testNonDiagonalWeightMatrix() {
        double[][] weight = new double[][]{
            {1.0, 0.5},
            {0.5, 2.0}
        };
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker()) {
            @Override
            public RealMatrix getWeight() {
                return new BlockRealMatrix(weight);
            }
        };
        optimizer.optimize(target, new double[]{0.0, 0.0, 0.0});
    }

    /**
     * Testing the solver with a test case where the Jacobian is singular.
     * The optimizer should throw an exception.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularJacobian() throws SingularMatrixException, ConvergenceException {
        double[][] jacobian = new double[][]{
            {0.0, 1.0, 0.0},
            {1.0, 0.0, 1.0},
            {0.0, 0.0, 0.0}
        };
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{});
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker()) {
            @Override
            protected RealMatrix computeWeightedJacobian(double[] point) {
                return new BlockRealMatrix(jacobian);
            }

            @Override
            public RealMatrix getWeight() {
                return weightMatrix;
            }
        };
        optimizer.optimize(target, new double[]{0.0, 0.0, 0.0});
    }
}
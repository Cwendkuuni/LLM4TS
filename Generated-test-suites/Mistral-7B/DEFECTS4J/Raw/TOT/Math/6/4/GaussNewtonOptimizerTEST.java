package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private double[] targetValues;
    private double[] startPoint;
    private RealMatrix weightMatrix;

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        targetValues = new double[]{1.0, 2.0, 3.0};
        startPoint = new double[]{0.0, 0.0, 0.0};
        weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
    }

    /**
     * Test the optimization with the given initial point.
     * @throws Exception If the optimization fails.
     */
    @Test
    public void testOptimize() throws Exception {
        optimizer.optimize();

        // Check the convergence.
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, optimizer.getPoint(), 0.0);
        Assert.assertEquals(0.0, optimizer.getCost(), 1e-10);
    }

    /**
     * Test the optimization when the target values are null.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullTarget() {
        optimizer.setTarget(null);
        optimizer.optimize();
    }

    /**
     * Test the optimization when the start point is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullStartPoint() {
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    /**
     * Test the optimization when the weight matrix is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullWeight() {
        optimizer.setWeight(null);
        optimizer.optimize();
    }

    /**
     * Test the optimization when the convergence checker is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.optimize();
    }

    /**
     * Test the optimization when the optimization fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithoutConvergence() {
        optimizer.setTarget(new double[]{1.0, 1.0, 1.0});
        optimizer.setStartPoint(new double[]{0.0, 0.0, 0.0});
        optimizer.setWeight(new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0},
            {0.0, 0.0, 1.0}
        }));
        optimizer.optimize();
    }

    /**
     * Test the optimization when the optimization fails due to a singular
     * problem.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithSingularProblem() {
        optimizer.setTarget(new double[]{1.0, 1.0, 1.0, 1.0});
        optimizer.setStartPoint(new double[]{0.0, 0.0, 0.0, 0.0});
        optimizer.setWeight(new BlockRealMatrix(new double[][]{
            {0.0, 1.0, 0.0, 0.0},
            {1.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 1.0},
            {0.0, 0.0, 1.0, 0.0}
        }));
        optimizer.optimize();
    }

    /**
     * Test the optimization when the optimization fails due to an unknown error.
     */
    @Test(expected = MathInternalError.class)
    public void testOptimizeWithUnknownError() {
        optimizer.setTarget(new double[]{1.0, 1.0, 1.0});
        optimizer.setStartPoint(new double[]{0.0, 0.0, 0.0});
        optimizer.setWeight(new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0},
            {Double.NaN, 0.0, 0.0}
        }));
        optimizer.optimize();
    }

    /**
     * Test the optimization when the optimization fails due to a null argument
     * exception.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullArgumentException() {
        optimizer.setTarget(null);
        optimizer.optimize();
    }

    /**
     * Test the optimization when the optimization fails due to an unconstrained
     * optimization exception.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeWithUnconstrainedOptimizationException() {
        optimizer.setTarget(new double[]{1.0, 1.0, 1.0});
        optimizer.setStartPoint(new double[]{1.0, 1.0, 1.0});
        optimizer.setWeight(new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, -1.0}
        }));
        optimizer.optimize();
    }
}
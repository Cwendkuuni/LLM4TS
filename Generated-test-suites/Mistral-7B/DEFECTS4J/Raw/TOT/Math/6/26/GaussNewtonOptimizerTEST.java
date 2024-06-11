package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test optimization with simple data.
     */
    @Test
    public void testOptimization() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = new double[]{1.5, 1.5, 1.5};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1.0E-10));
        PointValuePair result = optimizer.optimize(new SimpleFunction(targetValues), weightMatrix, startPoint);
        RealVector expected = new ArrayRealVector(new double[]{2.0, 2.0, 2.0});
        Assert.assertArrayEquals(expected.toArray(), result.getValue(), 1.0E-10);
    }

    /**
     * Test optimization with singular weight matrix.
     */
    @Test(expected = ConvergenceException.class)
    public void testSingularWeightMatrix() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        double[] startPoint = new double[]{1.5, 1.5, 1.5};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1.0E-10));
        optimizer.optimize(new SimpleFunction(targetValues), weightMatrix, startPoint);
    }

    /**
     * Test optimization with null target.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1.0E-10));
        optimizer.optimize(null, new RealMatrix(3, 3), new double[]{0.0, 0.0, 0.0});
    }

    /**
     * Test optimization with null weight matrix.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullWeightMatrix() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1.0E-10));
        optimizer.optimize(new SimpleFunction(targetValues), null, new double[]{0.0, 0.0, 0.0});
    }

    /**
     * Test optimization with null start point.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1.0E-10));
        optimizer.optimize(new SimpleFunction(targetValues), weightMatrix, null);
    }

    /**
     * Test optimization with wrong convergence checker.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testWrongConvergenceChecker() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = new double[]{1.5, 1.5, 1.5};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.optimize(new SimpleFunction(targetValues), weightMatrix, startPoint);
    }

    /**
     * Test optimization with invalid convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConvergenceChecker() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = new double[]{1.5, 1.5, 1.5};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new Object());
        optimizer.optimize(new SimpleFunction(targetValues), weightMatrix, startPoint);
    }
}
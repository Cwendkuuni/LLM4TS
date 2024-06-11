package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test optimization of a simple function with identity weight matrix.
     */
    @Test
    public void testSimpleOptimization() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setFunction(new IdentityFunction());
        optimizer.setWeight(new IdentityMatrix(2));
        optimizer.optimize();
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, optimizer.getPoint(), 0.0);
    }

    /**
     * Test optimization of a simple function with non-identity weight matrix.
     */
    @Test
    public void testNonIdentityWeightOptimization() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setFunction(new IdentityFunction());
        optimizer.setWeight(new DiagonalMatrix(new double[]{1.1, 0.9}));
        optimizer.optimize();
        Assert.assertArrayEquals(new double[]{1.0, 0.9}, optimizer.getPoint(), 0.0);
    }

    /**
     * Test optimization of a non-linear function.
     */
    @Test
    public void testNonLinearOptimization() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setFunction(new SineFunction());
        optimizer.setStartPoint(new double[]{0.0});
        optimizer.optimize();
        Assert.assertEquals(Math.PI / 2.0, optimizer.getPoint()[0], 0.001);
    }

    /**
     * Test optimization with non-convergence.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergence() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setFunction(new SquareRootFunction());
        optimizer.optimize();
    }

    /**
     * Test optimization with null weight.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullWeight() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setWeight(null);
    }

    /**
     * Test optimization with null function.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFunction() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setFunction(null);
    }

    /**
     * Test optimization with null convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testNullConvergenceChecker() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setConvergenceChecker(null);
    }

    /**
     * Test optimization with incorrect convergence checker.
     */
    @Test(expected = ClassCastException.class)
    public void testIncorrectConvergenceChecker() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setConvergenceChecker(new Object());
    }

    /**
     * Test optimization with incorrect start point size.
     */
    @Test(expected = IllegalArgumentException. class)
    public void testIncorrectStartPointSize() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setStartPoint(new double[]{1.0, 1.0, 1.0});
    }

    /**
     * Function to be optimized: f(x) = x^2.
     */
    private static class IdentityFunction implements Function {

        @Override
        public double value(double[] x) {
            return x[0] * x[0] + x[1] * x[1];
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{2.0 * x[0], 2.0 * x[1]};
        }
    }

    /**
     * Matrix of dimensions n x n with all entries equal to i.
     */
    private static RealMatrix identityMatrix(int n) {
        RealMatrix identity = new BlockRealMatrix(n, n);
        for (int i = 0; i < n; ++i) {
            identity.setEntry(i, i, 1.0);
        }
        return identity;
    }

    /**
     * Matrix of dimensions n x n with diagonal entries equal to the given values.
     */
    private static RealMatrix diagonalMatrix(double[] values) {
        int n = values.length;
        RealMatrix matrix = new BlockRealMatrix(n, n);
        for (int i = 0; i < n; ++i) {
            matrix.setEntry(i, i, values[i]);
        }
        return matrix;
    }

    /**
     * Function to be optimized: f(x) = sin(x).
     */
    private static class SineFunction implements Function {

        @Override
        public double value(double[] x) {
            return Math.sin(x[0]);
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{Math.cos(x[0])};
        }
    }

    /**
     * Function to be optimized: f(x) = sqrt(x).
     */
    private static class SquareRootFunction implements Function {

        @Override
        public double value(double[] x) {
            return Math.sqrt(x[0]);
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{1.0 / (2.0 * Math.sqrt(x[0]))};
        }
    }
}
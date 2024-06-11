package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Simple test for GaussNewtonOptimizer with LU decomposition.
     */
    @Test
    public void testLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {

            @Override
            public RealVector getObjectiveFunction(RealVector x) {
                return new ArrayRealVector(new double[] { Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) });
            }

            @Override
            public RealVector getGradient(RealVector x) {
                return new ArrayRealVector(new double[] { 2 * x.getEntry(0) + 2 * x.getEntry(1) });
            }

            @Override
            public RealVector getStartPoint() {
                return new ArrayRealVector(new double[] { 1, 1 });
            }

            @Override
            public RealMatrix getWeight() {
                return new BlockRealMatrix(new double[][] { { 1.0, 0.0 }, { 0.0, 1.0 } });
            }
        };

        PointValuePair opt = optimizer.optimize(problem);
        RealVector optX = opt.getPoint();
        double optF = opt.getValue();

        RealVector expectedX = new ArrayRealVector(new double[] { -0.5, -0.5 });
        double expectedF = 0.25;

        Assert.assertEquals(expectedX, optX, 1e-10);
        Assert.assertEquals(expectedF, optF, 1e-10);
    }

    /**
     * Simple test for GaussNewtonOptimizer with QR decomposition.
     */
    @Test
    public void testQRDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {

            @Override
            public RealVector getObjectiveFunction(RealVector x) {
                return new ArrayRealVector(new double[] { Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) });
            }

            @Override
            public RealVector getGradient(RealVector x) {
                return new ArrayRealVector(new double[] { 2 * x.getEntry(0) + 2 * x.getEntry(1) });
            }

            @Override
            public RealVector getStartPoint() {
                return new ArrayRealVector(new double[] { 1, 1 });
            }

            @Override
            public RealMatrix getWeight() {
                return new BlockRealMatrix(new double[][] { { 1.0, 0.0 }, { 0.0, 1.0 } });
            }
        };

        PointValuePair opt = optimizer.optimize(problem);
        RealVector optX = opt.getPoint();
        double optF = opt.getValue();

        RealVector expectedX = new ArrayRealVector(new double[] { -0.5, -0.5 });
        double expectedF = 0.25;

        Assert.assertEquals(expectedX, optX, 1e-10);
        Assert.assertEquals(expectedF, optF, 1e-10);
    }

    /**
     * Test for GaussNewtonOptimizer with a problem with singular weight matrix.
     */
    @Test(expected = ConvergenceException.class)
    public void testSingularWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {

            @Override
            public RealVector getObjectiveFunction(RealVector x) {
                return new ArrayRealVector(new double[] { Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) });
            }

            @Override
            public RealVector getGradient(RealVector x) {
                return new ArrayRealVector(new double[] { 2 * x.getEntry(0) + 2 * x.getEntry(1) });
            }

            @Override
            public RealVector getStartPoint() {
                return new ArrayRealVector(new double[] { 1, 1 });
            }

            @Override
            public RealMatrix getWeight() {
                return new SingularValueDecomposition(new ArrayRealVector(new double[] { 0.0, 0.0 })).getMatrixU();
            }
        };

        optimizer.optimize(problem);
    }

    /**
     * Test for GaussNewtonOptimizer with a problem with null weight matrix.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {

            @Override
            public RealVector getObjectiveFunction(RealVector x) {
                return new ArrayRealVector(new double[] { Math.pow(x.getEntry(0), 2) + Math.pow(x.getEntry(1), 2) });
            }

            @Override
            public RealVector getGradient(RealVector x) {
                return new ArrayRealVector(new double[] { 2 * x.getEntry(0) + 2 * x.getEntry(1) });
            }

            @Override
            public RealVector getStartPoint() {
                return new ArrayRealVector(new double[] { 1, 1 });
            }

            @Override
            public RealMatrix getWeight() {
                return null;
            }
        };

        optimizer.optimize(problem);
    }
}
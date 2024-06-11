package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        UnconstrainedMinimizer minimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};

        PointValuePair result = minimizer.optimize(new RealVector[]{new ArrayRealVector(targetValues)}, new RealMatrix[]{weightMatrix}, startPoint);
        double[] actual = result.getPoint();

        double[] expected = {1.0, 1.0, 1.0};
        assertArrayEquals(expected, actual, 0.001);

        double cost = minimizer.getValue(result);
        assertTrue(cost > 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        UnconstrainedMinimizer minimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());

        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};

        PointValuePair result = minimizer.optimize(new RealVector[]{new ArrayRealVector(targetValues)}, new RealMatrix[]{weightMatrix}, startPoint);
        double[] actual = result.getPoint();

        double[] expected = {1.0, 1.0, 1.0};
        assertArrayEquals(expected, actual, 0.001);

        double cost = minimizer.getValue(result);
        assertTrue(cost > 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonSingularProblem() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        UnconstrainedMinimizer minimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] targetValues = {1.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{0.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 0.0}});
        double[] startPoint = {0.0, 0.0, 0.0};

        PointValuePair result = minimizer.optimize(new RealVector[]{new ArrayRealVector(targetValues)}, new RealMatrix[]{weightMatrix}, startPoint);
        minimizer.doOptimize(); // Throws ConvergenceException
    }
}

package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullTargetValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(null);
        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(null);
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(new double[]{1, 2, 3});
        optimizer.setTarget(new double[]{1, 2, 3});
        PointVectorValuePair result = optimizer.doOptimize();
        Assert.assertArrayEquals(new double[]{1, 2, 3}, result.getPoint(), 0);

        optimizer.setTarget(new double[]{4, 5, 6});
        result = optimizer.doOptimize();
        Assert.assertArrayEquals(new double[]{4, 5, 6}, result.getPoint(), 0);

        optimizer.setTarget(new double[]{7, 8, 9});
        result = optimizer.doOptimize();
        Assert.assertArrayEquals(new double[]{7, 8, 9}, result.getPoint(), 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeSingularMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(new double[]{1, 1, 1});
        optimizer.setTarget(new double[]{1, 1, 1});
        optimizer.doOptimize();
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeMathInternalError() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(new double[]{1, 2, 3});
        optimizer.setTarget(new double[]{0, 0, 0});
        optimizer.setWeight(new BlockRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        optimizer.doOptimize();
    }
}

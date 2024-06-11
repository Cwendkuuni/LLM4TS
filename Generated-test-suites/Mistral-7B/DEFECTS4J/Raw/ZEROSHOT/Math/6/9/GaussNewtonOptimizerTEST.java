import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test with default settings.
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });
        RealVector startPoint = new ArrayRealVector(new double[]{1.0, 1.0});
        RealVector target = new ArrayRealVector(new double[]{2.0, 3.0});
        optimizer.optimize(weightMatrix, target, startPoint);
        RealVector expectedPoint = new ArrayRealVector(new double[]{1.5, 2.0});
        assertArrayEquals(expectedPoint.toArray(), optimizer.getPoint().toArray(), 0.0001);
        assertEquals(1.25, optimizer.getCost(), 0.0001);

        // Test with QR decomposition.
        GaussNewtonOptimizer optimizerQR = new GaussNewtonOptimizer(false, null);
        optimizerQR.optimize(weightMatrix, target, startPoint);
        assertArrayEquals(expectedPoint.toArray(), optimizerQR.getPoint().toArray(), 0.0001);
        assertEquals(1.25, optimizerQR.getCost(), 0.0001);

        // Test with LU decomposition.
        GaussNewtonOptimizer optimizerLU = new GaussNewtonOptimizer(true, null);
        optimizerLU.optimize(weightMatrix, target, startPoint);
        assertArrayEquals(expectedPoint.toArray(), optimizerLU.getPoint().toArray(), 0.0001);
        assertEquals(1.25, optimizerLU.getCost(), 0.0001);

        // Test with custom convergence checker.
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1.0e-4, 1.0e-4);
        GaussNewtonOptimizer optimizerChecker = new GaussNewtonOptimizer(true, checker);
        optimizerChecker.optimize(weightMatrix, target, startPoint);
        assertArrayEquals(expectedPoint.toArray(), optimizerChecker.getPoint().toArray(), 0.0001);
        assertEquals(1.25, optimizerChecker.getCost(), 0.0001);
        assertTrue(optimizerChecker.isConverged());

        // Test with null target.
        GaussNewtonOptimizer optimizerNullTarget = new GaussNewtonOptimizer();
        try {
            optimizerNullTarget.optimize(null, target, startPoint);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        // Test with null weight matrix.
        GaussNewtonOptimizer optimizerNullWeight = new GaussNewtonOptimizer();
        try {
            optimizerNullWeight.optimize(weightMatrix, null, startPoint);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        // Test with null start point.
        GaussNewtonOptimizer optimizerNullStart = new GaussNewtonOptimizer();
        try {
            optimizerNullStart.optimize(weightMatrix, target, null);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        // Test with illegal convergence checker.
        GaussNewtonOptimizer optimizerIllegalChecker = new GaussNewtonOptimizer();
        try {
            optimizerIllegalChecker.optimize(weightMatrix, target, startPoint, new Object());
            Assert.fail();
        } catch (ClassCastException e) {
            // Expected.
        }

        // Test with non-convergence.
        RealVector startPointNonConvergence = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        GaussNewtonOptimizer optimizerNonConvergence = new GaussNewtonOptimizer();
        try {
            optimizerNonConvergence.optimize(weightMatrix, target, startPointNonConvergence);
            Assert.fail();
        } catch (ConvergenceException e) {
            // Expected.
        }
        assertNull(optimizerNonConvergence.getPoint());

        // Test with unconstrained optimization exception.
        RealVector targetException = new ArrayRealVector(new double[]{Double.NaN});
        try {
            optimizer.optimize(weightMatrix, targetException, startPoint);
            Assert.fail();
        } catch (UnconstrainedOptimizationException e) {
            // Expected.
        }
        assertNull(optimizer.getPoint());

        // Test with illegal weighted Jacobian.
        RealMatrix weightedJacobianException = new BlockRealMatrix(new double[][]{
                {Double.NaN, Double.NaN},
                {Double.NaN, Double.NaN}
        });
        try {
            optimizer.optimize(weightMatrix, target, startPoint, weightedJacobianException);
            Assert.fail();
        } catch (MathInternalError e) {
            // Expected.
        }
        assertNull(optimizer.getPoint());

        // Test with illegal residuals.
        double[] residualsException = new double[]{Double.NaN};
        try {
            optimizer.optimize(weightMatrix, target, startPoint, residualsException);
            Assert.fail();
        } catch (MathInternalError e) {
            // Expected.
        }
        assertNull(optimizer.getPoint());
    }
}
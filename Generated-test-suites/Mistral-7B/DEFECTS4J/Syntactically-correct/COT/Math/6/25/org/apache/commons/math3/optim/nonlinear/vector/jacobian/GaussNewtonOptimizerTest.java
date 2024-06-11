package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.point.PointVectorValuePair;

/**
 * Test for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    @org.junit.Test
    public void testGaussNewtonOptimizerConstruction() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(true, optimizer.isUseLU());
        assertTrue(optimizer.getConvergenceChecker() instanceof SimpleVectorValueChecker);

        GaussNewtonOptimizer optimizerLU = new GaussNewtonOptimizer(true);
        assertEquals(true, optimizerLU.isUseLU());
        assertTrue(optimizerLU.getConvergenceChecker() instanceof SimpleVectorValueChecker);

        GaussNewtonOptimizer optimizerQR = new GaussNewtonOptimizer(false);
        assertEquals(false, optimizerQR.isUseLU());
        assertTrue(optimizerQR.getConvergenceChecker() instanceof SimpleVectorValueChecker);

        GaussNewtonOptimizer optimizerChecker = new GaussNewtonOptimizer(true, null);
        assertNull(optimizerChecker.getConvergenceChecker());

        try {
            new GaussNewtonOptimizer(null);
            assertFalse(true);
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testGaussNewtonOptimizerEmpty() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[0][0]);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        try {
            optimizer.doOptimize();
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testGaussNewtonOptimizerFunctionalTest() {
        RealMatrix weightMatrix = MatrixUtils.identity(3);
        double[] startPoint = new double[]{1.0, 1.0, 1.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] targetValues = new double[]{2.0, 3.0, 4.0};

        PointVectorValuePair result = optimizer.doOptimize();
        assertArrayEquals(new double[]{1.5, 1.5, 1.5}, result.getPoint(), 0.001);
        assertArrayEquals(new double[]{0.5, 0.5, 0.5}, result.getVector(), 0.001);

        double[] currentPoint = new double[]{1.5, 1.5, 1.5};
        double[] currentObjective = new double[]{1.0, 1.0, 1.0};
        double[] currentResiduals = new double[]{0.5, 0.5, 0.5};
        double[] grad = new double[3];
        RealMatrix weightedJacobian = new BlockRealMatrix(new double[3][3]);

        for (int i = 0; i < 3; i++) {
            grad[i] = -(currentTargetValue(currentObjective[i]) - currentObjective[i]) / 0.1;
            for (int j = 0; j < 3; j++) {
                weightedJacobian.setEntry(i, j, weightedJacobian.getEntry(i, j) + grad[i] * grad[j]);
            }
        }

        for (int i = 0; i < 3; i++) {
            assertEquals(currentResiduals[i], weightedJacobian.getEntry(i, 0), 0.001);
            assertEquals(currentResiduals[i], weightedJacobian.getEntry(i, 1), 0.001);
            assertEquals(currentResiduals[i], weightedJacobian.getEntry(i, 2), 0.001);
        }

        // check normal equations
        RealMatrix a = new BlockRealMatrix(weightedJacobian.transpose().multiply(weightMatrix).multiply(weightedJacobian));
        RealMatrix b = new BlockRealMatrix(weightedJacobian.transpose().multiply(weightMatrix).multiply(new ArrayRealVector(currentResiduals, false)));

        try {
            DecompositionSolver solver = useLU ? new LUDecomposition(a).getSolver() : new QRDecomposition(a).getSolver();
            RealVector x = solver.solve(b);

            for (int i = 0; i < 3; i++) {
                assertEquals(currentPoint[i] + x.getEntry(i), result.getPoint()[i], 0.001);
            }
        } catch (SingularMatrixException e) {
            throw new RuntimeException("Unexpected error");
        }
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerSingularMatrixException() {
        RealMatrix weightMatrix = MatrixUtils.identity(3);
        double[] startPoint = new double[]{1.0, 1.0, 1.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] targetValues = new double[]{2.0, 0.0, 4.0};

        optimizer.doOptimize();
    }

    private double currentTargetValue(double value) {
        return Math.pow(value, 2) - 1;
    }

    private boolean useLU = true;
}
package org.apache.commons.math3.optimization.general.test;

import static org.junit.Assert.*;

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
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;

/**
 * Test class for GaussNewtonOptimizer.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test constructor with default settings.
     */
    @org.junit.Test
    public void testGaussNewtonOptimizerDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertSame(true, optimizer.isUseLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with LU decomposition.
     */
    @org.junit.Test
    public void testGaussNewtonOptimizerLUDecompositionConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertSame(true, optimizer.isUseLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with custom convergence checker.
     */
    @org.junit.Test
    public void testGaussNewtonOptimizerCustomConvergenceCheckerConstructor() {
        ConvergenceChecker<PointVectorValuePair> customChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iter, PointVectorValuePair previous, PointVectorValuePair current) {
                return current.getValue() < 0.01;
            }
        };
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(customChecker);
        assertFalse(optimizer.isUseLU());
        assertSame(customChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test doOptimize method with default settings.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(null);
        optimizer.doOptimize();
    }

    /**
     * Test doOptimize method with custom convergence checker.
     */
    @org.junit.Test
    public void testDoOptimizeWithCustomConvergenceChecker() {
        ConvergenceChecker<PointVectorValuePair> customChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iter, PointVectorValuePair previous, PointVectorValuePair current) {
                return current.getValue() < 0.01;
            }
        };
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(customChecker);
        double[] target = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        optimizer.doOptimize();
        double[] expected = {1.0 - 0.1, 2.0 - 0.1};
        assertArrayEquals(expected, optimizer.getPoint(), 0.001);
        assertEquals(0.0, optimizer.getValue(), 0.001);
    }

    /**
     * Test doOptimize method with LU decomposition.
     */
    @org.junit.Test
    public void testDoOptimizeWithLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] target = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        optimizer.doOptimize();
        double[] expected = {1.0 - 0.1, 2.0 - 0.1};
        assertArrayEquals(expected, optimizer.getPoint(), 0.001);
        assertEquals(0.0, optimizer.getValue(), 0.001);
    }

    /**
     * Test doOptimize method with QR decomposition.
     */
    @org.junit.Test
    public void testDoOptimizeWithQRDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        double[] target = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        optimizer.doOptimize();
        double[] expected = {1.0 - 0.1, 2.0 - 0.1};
        assertArrayEquals(expected, optimizer.getPoint(), 0.001);
        assertEquals(0.0, optimizer.getValue(), 0.001);
    }

    /**
     * Test computeObjectiveValue method.
     */
    @org.junit.Test
    public void testComputeObjectiveValue() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] target = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        double[] actual = optimizer.computeObjectiveValue(startPoint);
        double[] expected = {3.0, 5.0};
        assertArrayEquals(expected, actual, 0.001);
    }

    /**
     * Test computeResiduals method.
     */
    @org.junit.Test
    public void testComputeResiduals() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] target = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        double[] actual = optimizer.computeResiduals(optimizer.computeObjectiveValue(startPoint));
        double[] expected = {2.0, 4.0};
        assertArrayEquals(expected, actual, 0.001);
    }

    /**
     * Test computeWeightedJacobian method.
     */
    @org.junit.Test
    public void testComputeWeightedJacobian() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] target = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        double[] weights = {1.0, 1.0};
        optimizer.setTarget(target);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(new ArrayRealVector(weights));
        RealMatrix actual = optimizer.computeWeightedJacobian(startPoint);
        RealMatrix expected = new BlockRealMatrix(new double[][] {{1.0, 1.0}, {2.0, 2.0}});
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }
}
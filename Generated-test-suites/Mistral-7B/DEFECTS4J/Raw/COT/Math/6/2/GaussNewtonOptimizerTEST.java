package org.apache.commons.math3.optimization.general.test;

import static org.junit.Assert.*;

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
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

/**
 * JUnit test file for GaussNewtonOptimizer class.
 */
public class GaussNewtonOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        new GaussNewtonOptimizer(null);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        RealMatrix a = new BlockRealMatrix(new double[][] {{1.0, 0.0}, {1.0, 1.0}});
        DecompositionSolver solver = new LUDecomposition(a).getSolver();
        new GaussNewtonOptimizer(true, null).doOptimize();
    }

    @Test
    public void testDoOptimize_SimpleVectorValueChecker() {
        RealVector startPoint = new ArrayRealVector(new double[] {1.0, 2.0});
        ConvergenceChecker<PointValuePair> checker = new SimpleVectorValueChecker(new MaxEval(1000));
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix weightedJacobian = new BlockRealMatrix(new double[][] {{1.0, 1.0}, {1.0, 1.0}});
        double[] targetValues = new double[] {1.0, 1.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        double[] currentPoint = startPoint.toArray();
        PointVectorValuePair result = optimizer.doOptimize();
        double cost = optimizer.getCost();
        assertEquals(1.0, cost, 1e-9);
        assertArrayEquals(new double[] {1.0, 1.0}, result.getPoint(), 1e-9);
        assertArrayEquals(new double[] {0.0, 0.0}, result.getValue(), 1e-9);
    }

    @Test
    public void testDoOptimize_LUDecomposition() {
        RealVector startPoint = new ArrayRealVector(new double[] {1.0, 2.0});
        ConvergenceChecker<PointValuePair> checker = new SimpleVectorValueChecker(new MaxEval(1000));
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix weightedJacobian = new BlockRealMatrix(new double[][] {{1.0, 1.0}, {1.0, 1.0}});
        double[] targetValues = new double[] {1.0, 1.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        double[] currentPoint = startPoint.toArray();
        PointVectorValuePair result = optimizer.doOptimize();
        double cost = optimizer.getCost();
        assertEquals(1.0, cost, 1e-9);
        assertArrayEquals(new double[] {1.0, 1.0}, result.getPoint(), 1e-9);
        assertArrayEquals(new double[] {0.0, 0.0}, result.getValue(), 1e-9);

        RealMatrix a = new BlockRealMatrix(weightedJacobian.preMultiply(weightMatrix));
        DecompositionSolver solver = new LUDecomposition(a).getSolver();
        RealVector b = new ArrayRealVector(new double[] {0.0, 0.0});
        RealVector dX = solver.solve(b);
        double[] updatedPoint = new double[] {1.0, 2.0};
        for (int i = 0; i < 2; i++) {
            updatedPoint[i] += dX.getEntry(i);
        }
        assertArrayEquals(result.getPoint(), updatedPoint, 1e-9);
    }

    @Test
    public void testDoOptimize_QRDecomposition() {
        RealVector startPoint = new ArrayRealVector(new double[] {1.0, 2.0});
        ConvergenceChecker<PointValuePair> checker = new SimpleVectorValueChecker(new MaxEval(1000));
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix weightedJacobian = new BlockRealMatrix(new double[][] {{1.0, 1.0}, {1.0, 1.0}});
        double[] targetValues = new double[] {1.0, 1.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
        double[] currentPoint = startPoint.toArray();
        PointVectorValuePair result = optimizer.doOptimize();
        double cost = optimizer.getCost();
        assertEquals(1.0, cost, 1e-9);
        assertArrayEquals(new double[] {1.0, 1.0}, result.getPoint(), 1e-9);
        assertArrayEquals(new double[] {0.0, 0.0}, result.getValue(), 1e-9);

        RealMatrix a = new BlockRealMatrix(weightedJacobian.preMultiply(weightMatrix).transpose());
        QRDecomposition qr = new QRDecomposition(a);
        RealVector b = new ArrayRealVector(new double[] {0.0, 0.0});
        RealVector dX = qr.getSolver().solve(b);
        double[] updatedPoint = new double[] {1.0, 2.0};
        for (int i = 0; i < 2; i++) {
            updatedPoint[i] += dX.getEntry(i);
        }
        assertArrayEquals(result.getPoint(), updatedPoint, 1e-9);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        RealVector startPoint = new ArrayRealVector(new double[] {1.0, 2.0});
        ConvergenceChecker<PointValuePair> checker = new SimpleVectorValueChecker(new MaxEval(1));
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix weightedJacobian = new BlockRealMatrix(new double[][] {{1.0, 1.0}, {1.0, 1.0}});
        double[] targetValues = new double[] {1.0, 1.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        double[] currentPoint = startPoint.toArray();
        optimizer.doOptimize();
    }

}
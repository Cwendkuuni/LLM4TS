package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new GaussNewtonOptimizer(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUseLU() {
        new GaussNewtonOptimizer(null, new SimpleVectorValueChecker());
    }

    @Test(expected = NullPointerException.class)
    public void testNullChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    @Test
    public void testConstructorWithChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertEquals(true, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithCheckerAndLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertEquals(true, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithCheckerAndQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        assertEquals(false, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithCheckerAndCustomChecker() {
        ConvergenceChecker<PointVectorValuePair> customChecker = new CustomConvergenceChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, customChecker);
        assertEquals(true, optimizer.isUseLU());
        assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithNullConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        optimizer.optimize();
    }

    @Test
    public void testOptimizeWithSimpleChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
        RealVector point = optimizer.getPoint();
        assertArrayEquals(new double[]{2.0, 2.0}, point.toArray());
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithoutConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.optimize();
    }

    @Test
    public void testOptimizeWithCustomChecker() {
        ConvergenceChecker<PointVectorValuePair> customChecker = new CustomConvergenceChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, customChecker);
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setConvergenceChecker(customChecker);
        optimizer.optimize();
        assertTrue(customChecker.isConverged(0, null, optimizer.getLastIteration()));
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    @Test
    public void testOptimizeWithDifferentStartPoints() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        double[] startPoint1 = {1.0, 2.0};
        double[] startPoint2 = {2.0, 1.0};
        optimizer.setStartPoint(startPoint1);
        PointVectorValuePair result1 = optimizer.optimize();
        optimizer.setStartPoint(startPoint2);
        PointVectorValuePair result2 = optimizer.optimize();
        assertArrayEquals(new double[]{2.0, 2.0}, result1.getPoint().toArray());
        assertArrayEquals(new double[]{1.6666666666666666, 1.3333333333333336}, result2.getPoint().toArray());
    }

    @Test
    public void testOptimizeWithDifferentTargets() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 2.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        double[] targetValues1 = {3.0, 4.0};
        double[] targetValues2 = {4.0, 3.0};
        optimizer.setTarget(targetValues1);
        PointVectorValuePair result1 = optimizer.optimize();
        optimizer.setTarget(targetValues2);
        PointVectorValuePair result2 = optimizer.optimize();
        assertArrayEquals(new double[]{2.0, 2.0}, result1.getPoint().toArray());
        assertArrayEquals(new double[]{1.6666666666666666, 1.3333333333333336}, result2.getPoint().toArray());
    }

    @Test
    public void testOptimizeWithDifferentWeights() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix1 = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix weightMatrix2 = new BlockRealMatrix(new double[][]{{2.0, 0.0}, {0.0, 2.0}});
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix1);
        PointVectorValuePair result1 = optimizer.optimize();
        optimizer.setWeight(weightMatrix2);
        PointVectorValuePair result2 = optimizer.optimize();
        assertArrayEquals(new double[]{2.0, 2.0}, result1.getPoint().toArray());
        assertArrayEquals(new double[]{1.5, 1.5}, result2.getPoint().toArray());
    }

    @Test
    public void testOptimizeWithDifferentUseLU() {
        GaussNewtonOptimizer optimizer1 = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        GaussNewtonOptimizer optimizer2 = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        optimizer1.setStartPoint(startPoint);
        optimizer1.setTarget(targetValues);
        optimizer1.setWeight(weightMatrix);
        optimizer1.optimize();
        optimizer2.setStartPoint(startPoint);
        optimizer2.setTarget(targetValues);
        optimizer2.setWeight(weightMatrix);
        optimizer2.optimize();
        assertArrayEquals(new double[]{2.0, 2.0}, optimizer1.getPoint().toArray());
        assertArrayEquals(new double[]{1.6666666666666666, 1.3333333333333336}, optimizer2.getPoint().toArray());
    }

    static class CustomConvergenceChecker implements ConvergenceChecker<PointVectorValuePair> {
        @Override
        public boolean isConverged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
            return current.getValue() < 0.1;
        }
    }

}
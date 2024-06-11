package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MatrixDimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Test;

/**
 * Test class for GaussNewtonOptimizer.
 */
public class GaussNewtonOptimizerTest {
    /**
     * Test constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertNotNull(optimizer);
        assertTrue(optimizer.isUseLU());
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
        assertInstanceOf(SimpleVectorValueChecker.class, checker);
    }

    /**
     * Test constructor with LU decomposition.
     */
    @Test
    public void testLUConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertNotNull(optimizer);
        assertTrue(optimizer.isUseLU());
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
        assertInstanceOf(SimpleVectorValueChecker.class, checker);
    }

    /**
     * Test constructor with QR decomposition.
     */
    @Test
    public void testQRConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        assertNotNull(optimizer);
        assertFalse(optimizer.isUseLU());
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
        assertInstanceOf(SimpleVectorValueChecker.class, checker);
    }

    /**
     * Test constructor with custom convergence checker.
     */
    @Test
    public void testCustomConstructor() {
        ConvergenceChecker<PointVectorValuePair> customChecker = mock(ConvergenceChecker.class);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, customChecker);
        assertNotNull(optimizer);
        assertTrue(optimizer.isUseLU());
        assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test doOptimize with convergence.
     */
    @Test
    public void testDoOptimizeConvergence() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = MatrixUtils.identityMatrix(targetValues.length);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        PointVectorValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertArrayEquals(new double[] {1.0, 1.0, 1.0}, result.getPoint(), 0.0);
        assertEquals(0.0, optimizer.getCost(), 0.0);
    }

    /**
     * Test doOptimize with no convergence.
     */
    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeNoConvergence() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new SingularMatrixException(3, 3).getData();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.doOptimize();
    }

    /**
     * Test computeObjectiveValue.
     */
    @Test
    public void testComputeObjectiveValue() {
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {2.0, 3.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] result = optimizer.computeObjectiveValue(startPoint);
        assertNotNull(result);
        assertEquals(5.0, result[0], 0.0);
    }

    /**
     * Test computeResiduals.
     */
    @Test
    public void testComputeResiduals() {
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {2.0, 3.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] result = optimizer.computeResiduals(targetValues);
        assertNotNull(result);
        assertEquals(-1.0, result[0], 0.0);
        assertEquals(-1.0, result[1], 0.0);
    }

    /**
     * Test computeWeightedJacobian.
     */
    @Test
    public void testComputeWeightedJacobian() {
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {2.0, 3.0};
        RealMatrix weightMatrix = MatrixUtils.identityMatrix(targetValues.length);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        RealMatrix result = optimizer.computeWeightedJacobian(startPoint);
        assertNotNull(result);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        double[][] data = result.getData();
        assertEquals(0.0, data[0][0], 0.0);
        assertEquals(-1.0, data[0][1], 0.0);
        assertEquals(0.0, data[1][0], 0.0);
        assertEquals(-1.0, data[1][1], 0.0);
    }

    /**
     * Test solve normal equations with LU decomposition.
     */
    @Test
    public void testSolveNormalEquationsLU() {
        double[] b = {1.0, 2.0};
        double[][] a = {{1.0, 1.0}, {1.0, 2.0}};
        RealMatrix mA = new BlockRealMatrix(a);
        LUDecomposition luDecomposition = new LUDecomposition(mA).getDecompositionMatrix();
        DecompositionSolver solver = luDecomposition.getSolver();
        double[] dX = solver.solve(new ArrayRealVector(b, false)).toArray();
        double[] expected = {-0.5, -0.5};
        assertArrayEquals(expected, dX, 0.0);
    }

    /**
     * Test solve normal equations with QR decomposition.
     */
    @Test
    public void testSolveNormalEquationsQR() {
        double[] b = {1.0, 2.0};
        double[][] a = {{1.0, 1.0}, {1.0, 2.0}};
        RealMatrix mA = new BlockRealMatrix(a);
        QRDecomposition qrDecomposition = new QRDecomposition(mA).getDecompositionMatrix();
        DecompositionSolver solver = qrDecomposition.getSolver();
        double[] dX = solver.solve(new ArrayRealVector(b, false)).toArray();
        double[] expected = {-0.5, -0.5};
        assertArrayEquals(expected, dX, 0.0);
    }

    /**
     * Test solve normal equations with SVD decomposition.
     */
    @Test
    public void testSolveNormalEquationsSVD() {
        double[] b = {1.0, 2.0};
        double[][] a = {{1.0, 1.0}, {1.0, 2.0}};
        RealMatrix mA = new BlockRealMatrix(a);
        SingularValueDecomposition svdDecomposition = new SingularValueDecomposition(mA).getDecompositionMatrix();
        DecompositionSolver solver = svdDecomposition.getSolver();
        double[] dX = solver.solve(new ArrayRealVector(b, false)).toArray();
        double[] expected = {-0.5, -0.5};
        assertArrayEquals(expected, dX, 0.0);
    }

    /**
     * Test getCost.
     */
    @Test
    public void testGetCost() {
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {2.0, 3.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] currentObjective = optimizer.computeObjectiveValue(startPoint);
        double cost = optimizer.getCost(currentObjective);
        assertEquals(5.0, cost, 0.0);
    }
}
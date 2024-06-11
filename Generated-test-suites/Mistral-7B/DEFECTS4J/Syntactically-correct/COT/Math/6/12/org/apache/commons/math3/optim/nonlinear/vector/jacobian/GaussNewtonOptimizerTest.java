package org.apache.commons.math3.optimization.general.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
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
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer.LUDecompositionSolver;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer.QRDecompositionSolver;
import org.apache.commons.math3.optimization.test.AbstractDifferentiableTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest extends AbstractDifferentiableTest {

    private GaussNewtonOptimizer optimizer;
    private double[] startPoint;
    private double[] targetValues;
    private RealMatrix weightMatrix;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        startPoint = new double[] { 1.0, 2.0 };
        targetValues = new double[] { 3.0, 4.0 };
        weightMatrix = MatrixUtils.identity(2);
    }

    /**
     * Test constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertTrue(optimizer instanceof GaussNewtonOptimizer);
        assertTrue(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with specified settings.
     */
    @Test
    public void testConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        assertFalse(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with specified settings.
     */
    @Test
    public void testConstructorWithChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertTrue(optimizer instanceof GaussNewtonOptimizer);
        assertTrue(optimizer.isUseLU());
        assertSame(optimizer.getConvergenceChecker(), new SimpleVectorValueChecker());
    }

    /**
     * Test optimization with LU decomposition.
     */
    @Test
    public void testOptimizeWithLU() throws Exception {
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        RealVector expectedPoint = VectorUtils.createRealVector(new double[] { 3.0, 4.0 });
        PointValuePair result = optimizer.getResult();
        assertSame(expectedPoint, result.getPoint());
        assertArrayEquals(targetValues, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with QR decomposition.
     */
    @Test
    public void testOptimizeWithQR() throws Exception {
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        RealVector expectedPoint = VectorUtils.createRealVector(new double[] { 3.0, 4.0 });
        PointValuePair result = optimizer.getResult();
        assertSame(expectedPoint, result.getPoint());
        assertArrayEquals(targetValues, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with null weight matrix.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullWeightMatrix() {
        optimizer.optimize(targetValues, startPoint, null);
    }

    /**
     * Test optimization with incorrect weight matrix dimensions.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeWithIncorrectWeightMatrix() {
        RealMatrix weightMatrix = new SingularValueDecomposition(MatrixUtils.random(2, 3)).getMatrixB();
        optimizer.optimize(targetValues, startPoint, weightMatrix);
    }

    /**
     * Test optimization with null target values.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullTargetValues() {
        optimizer.optimize(null, startPoint, weightMatrix);
    }

    /**
     * Test optimization with null start point.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullStartPoint() {
        optimizer.optimize(targetValues, null, weightMatrix);
    }

    /**
     * Test optimization with incorrect start point dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithIncorrectStartPointDimensions() {
        optimizer.optimize(targetValues, new double[] { 1.0 }, weightMatrix);
    }

    /**
     * Test optimization with convergence checker.
     */
    @Test
    public void testOptimizeWithChecker() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setMaxIterations(1);
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        assertFalse(optimizer.isConverged());
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(0.001, 0.001));
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        assertTrue(optimizer.isConverged());
    }

    /**
     * Test optimization with non-converged result.
     */
    @Test
    public void testOptimizeWithNonConvergedResult() throws Exception {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(0.1, 0.1));
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        assertFalse(optimizer.isConverged());
    }

    /**
     * Test convergence checker.
     */
    @Test
    public void testConvergenceChecker() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(0.0, 0.0);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        RealVector expectedPoint = VectorUtils.createRealVector(new double[] { 3.0, 4.0 });
        double[] expectedValue = targetValues.clone();
        double tolerance = 1e-10;
        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);
        assertSame(expectedPoint, result.getPoint());
        assertArrayEquals(expectedValue, result.getValue(), tolerance);
        assertTrue(optimizer.isConverged());
    }

    /**
     * Test LU decomposition solver.
     */
    @Test
    public void testLUSolver() {
        LUDecompositionSolver solver = new LUDecompositionSolver();
        optimizer.setDecompositionSolver(solver);
        RealMatrix a = MatrixUtils.identity(2);
        RealVector b = VectorUtils.createRealVector(new double[] { 1.0, 2.0 });
        RealVector x = solver.solve(a, b);
        assertEquals(0.0, x.getEntry(0) - 1.0, 1e-12);
        assertEquals(0.0, x.getEntry(1) - 1.0, 1e-12);
    }

    /**
     * Test QR decomposition solver.
     */
    @Test
    public void testQRSolver() {
        QRDecompositionSolver solver = new QRDecompositionSolver();
        optimizer.setDecompositionSolver(solver);
        RealMatrix a = MatrixUtils.identity(2);
        RealVector b = VectorUtils.createRealVector(new double[] { 1.0, 2.0 });
        RealVector x = solver.solve(a, b);
        assertEquals(0.0, x.getEntry(0) - 1.0, 1e-12);
        assertEquals(0.0, x.getEntry(1) - 1.0, 1e-12);
    }

    /**
     * Test optimization with LU decomposition solver.
     */
    @Test
    public void testOptimizeWithLUSolver() throws Exception {
        LUDecompositionSolver solver = new LUDecompositionSolver();
        optimizer.setDecompositionSolver(solver);
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        RealVector expectedPoint = VectorUtils.createRealVector(new double[] { 3.0, 4.0 });
        PointValuePair result = optimizer.getResult();
        assertSame(expectedPoint, result.getPoint());
        assertArrayEquals(targetValues, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with QR decomposition solver.
     */
    @Test
    public void testOptimizeWithQRSolver() throws Exception {
        QRDecompositionSolver solver = new QRDecompositionSolver();
        optimizer.setDecompositionSolver(solver);
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        RealVector expectedPoint = VectorUtils.createRealVector(new double[] { 3.0, 4.0 });
        PointValuePair result = optimizer.getResult();
        assertSame(expectedPoint, result.getPoint());
        assertArrayEquals(targetValues, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with LU decomposition solver and non-converged result.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithLUSolverAndNonConvergedResult() throws Exception {
        LUDecompositionSolver solver = new LUDecompositionSolver();
        optimizer.setDecompositionSolver(solver);
        optimizer.setMaxIterations(1);
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        assertFalse(optimizer.isConverged());
    }

    /**
     * Test optimization with QR decomposition solver and non-converged result.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithQRSolverAndNonConvergedResult() throws Exception {
        QRDecompositionSolver solver = new QRDecompositionSolver();
        optimizer.setDecompositionSolver(solver);
        optimizer.setMaxIterations(1);
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        assertFalse(optimizer.isConverged());
    }
}
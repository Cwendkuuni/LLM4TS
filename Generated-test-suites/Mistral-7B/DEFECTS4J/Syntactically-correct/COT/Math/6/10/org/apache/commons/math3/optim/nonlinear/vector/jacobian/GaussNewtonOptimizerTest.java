package org.apache.commons.math3.optimization.general.test;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest extends UnconstrainedOptimizationTest {

    @Test
    public void testGaussNewtonOptimizer_DefaultConstructor() {
        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer();
        assertFalse(gaussNewtonOptimizer.isUseLU());
        assertEquals(SimpleVectorValueChecker.class, gaussNewtonOptimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testGaussNewtonOptimizer_SimpleConstructorWithDefaultSettings_LU() {
        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(true);
        assertTrue(gaussNewtonOptimizer.isUseLU());
        assertEquals(SimpleVectorValueChecker.class, gaussNewtonOptimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testGaussNewtonOptimizer_SimpleConstructorWithDefaultSettings_QR() {
        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(false);
        assertFalse(gaussNewtonOptimizer.isUseLU());
        assertEquals(SimpleVectorValueChecker.class, gaussNewtonOptimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testGaussNewtonOptimizer_ConstructorWithCustomCheckers() {
        SimpleVectorValueChecker customChecker = new SimpleVectorValueChecker(1e-15, 1e-15);
        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(true, customChecker);
        assertTrue(gaussNewtonOptimizer.isUseLU());
        assertEquals(customChecker.getClass(), gaussNewtonOptimizer.getConvergenceChecker().getClass());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException_NullConvergenceChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    @Test
    public void testDoOptimize_Convergence() throws ConvergenceException, SingularMatrixException {
        final double[] targetValues = {1.0, 1.0};
        final double[] startPoint = {0.5, 0.5};
        final RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(true);

        PointVectorValuePair result = gaussNewtonOptimizer.optimize(targetValues, startPoint, weightMatrix);

        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.001);
        assertEquals(0.0, gaussNewtonOptimizer.getCost(), 0.001);
    }

    @Test
    public void testDoOptimize_SingularMatrixException() throws ConvergenceException, SingularMatrixException {
        final double[] targetValues = {1.0, 1.0};
        final double[] startPoint = {0.5, 0.5};
        final RealMatrix weightMatrix = new SingularValueDecomposition(new RealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}})).getSolver().getInverse();

        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(true);

        try {
            gaussNewtonOptimizer.optimize(targetValues, startPoint, weightMatrix);
            assertFalse(true);
        } catch (SingularMatrixException e) {
            assertTrue(true);
        }

        assertEquals(Double.POSITIVE_INFINITY, gaussNewtonOptimizer.getCost(), 0.001);
        assertNull(gaussNewtonOptimizer.getOptimizationResult());
    }

    @Test
    public void testDoOptimize_ConvergenceCheckerException() throws ConvergenceException, SingularMatrixException {
        final double[] targetValues = {1.0, 1.0};
        final double[] startPoint = {0.5, 0.5};
        final RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SimpleVectorValueChecker customChecker = new SimpleVectorValueChecker(1e-9, 1e-9);
        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(true, customChecker);

        try {
            gaussNewtonOptimizer.optimize(targetValues, startPoint, weightMatrix);
            assertFalse(true);
        } catch (ConvergenceException e) {
            assertTrue(true);
        }

        assertEquals(Double.POSITIVE_INFINITY, gaussNewtonOptimizer.getCost(), 0.001);
        assertNull(gaussNewtonOptimizer.getOptimizationResult());
    }

    @Test
    public void testDoOptimize_NoConvergence() throws ConvergenceException, SingularMatrixException {
        final double[] targetValues = {1.0, 1.0};
        final double[] startPoint = {0.5, 0.5};
        final RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SimpleVectorValueChecker customChecker = new SimpleVectorValueChecker(1e-16, 1e-16);
        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(true, customChecker);

        PointVectorValuePair result = gaussNewtonOptimizer.optimize(targetValues, startPoint, weightMatrix);

        assertFalse(customChecker.isConverged(1, result, null));
        assertEquals(Double.POSITIVE_INFINITY, gaussNewtonOptimizer.getCost(), 0.001);
        assertNull(gaussNewtonOptimizer.getOptimizationResult());
    }

    @Test
    public void testDoOptimize_ConvergenceWithCustomChecker() throws ConvergenceException, SingularMatrixException {
        final double[] targetValues = {1.0, 1.0};
        final double[] startPoint = {0.5, 0.5};
        final RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SimpleVectorValueChecker customChecker = new SimpleVectorValueChecker(1e-15, 1e-15);
        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(true, customChecker);

        PointVectorValuePair result = gaussNewtonOptimizer.optimize(targetValues, startPoint, weightMatrix);

        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.001);
        assertEquals(0.0, gaussNewtonOptimizer.getCost(), 0.001);
        assertTrue(customChecker.isConverged(1, result, null));
    }

    @Test
    public void testDoOptimize_EmptyWeightMatrix() throws ConvergenceException, SingularMatrixException {
        final double[] targetValues = {1.0, 1.0};
        final double[] startPoint = {0.5, 0.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{0.0}});

        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(true);

        try {
            gaussNewtonOptimizer.optimize(targetValues, startPoint, weightMatrix);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        assertEquals(Double.POSITIVE_INFINITY, gaussNewtonOptimizer.getCost(), 0.001);
        assertNull(gaussNewtonOptimizer.getOptimizationResult());
    }

    @Test
    public void testDoOptimize_NullWeightMatrix() throws ConvergenceException, SingularMatrixException {
        final double[] targetValues = {1.0, 1.0};
        final double[] startPoint = {0.5, 0.5};
        RealMatrix weightMatrix = null;

        GaussNewtonOptimizer gaussNewtonOptimizer = new GaussNewtonOptimizer(true);

        try {
            gaussNewtonOptimizer.optimize(targetValues, startPoint, weightMatrix);
            assertFalse(true);
        } catch (NullArgumentException e) {
            assertTrue(true);
        }

        assertEquals(Double.POSITIVE_INFINITY, gaussNewtonOptimizer.getCost(), 0.001);
        assertNull(gaussNewtonOptimizer.getOptimizationResult());
    }

}
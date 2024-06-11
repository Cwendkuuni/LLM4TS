package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.VectorFormat;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Test;

/**
 * Test cases for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test constructor with default settings and LU decomposition.
     */
    @Test
    public void testDefaultConstructorWithLuDecomposition() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker, true);
        assertNull(optimizer.getStartPoint());
        assertEquals(true, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with default settings and QR decomposition.
     */
    @Test
    public void testDefaultConstructorWithQrDecomposition() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker, false);
        assertNull(optimizer.getStartPoint());
        assertEquals(false, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with custom settings.
     */
    @Test
    public void testCustomConstructor() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertNull(optimizer.getStartPoint());
        assertEquals(true, optimizer.isUseLU());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization with LU decomposition.
     */
    @Test
    public void testOptimizationWithLuDecomposition() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        PointVectorValuePair result = optimizer.optimize();
        double[] expectedResult = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(1.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization with QR decomposition.
     */
    @Test
    public void testOptimizationWithQrDecomposition() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        PointVectorValuePair result = optimizer.optimize();
        double[] expectedResult = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(1.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization with non-square weight matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithNonSquareWeightMatrix() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0},
            {0.0, 1.0},
            {1.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization with null convergence checker.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizationWithNullConvergenceChecker() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization with invalid convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidConvergenceChecker() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        ConvergenceChecker<Object> invalidChecker = new Object();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, invalidChecker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization with non-positive weight matrix.
     */
    @Test
    public void testOptimizationWithNonPositiveWeightMatrix() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {-1.0, 0.0, 0.0},
            {0.0, -1.0, 0.0},
            {0.0, 0.0, -1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        try {
            optimizer.optimize();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test optimization with singular weightedJacobian.
     */
    @Test(expected = SingularMatrixException.class)
    public void testOptimizationWithSingularWeightedJacobian() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization with null target.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizationWithNullTarget() {
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization with null startPoint.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizationWithNullStartPoint() {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization with null weight matrix.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizationWithNullWeightMatrix() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.optimize();
    }

    /**
     * Test optimization with non-finite objective function.
     */
    @Test(expected = MathInternalError.class)
    public void testOptimizationWithNonFiniteObjectiveFunction() {
        double[] targetValues = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization with non-square startPoint.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithNonSquareStartPoint() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization convergence checker.
     */
    @Test
    public void testOptimizationConvergenceChecker() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-5, 1e-5);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        PointVectorValuePair result = optimizer.optimize();
        double[] expectedResult = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(1.0, result.getValue(), 0.001);
    }
}
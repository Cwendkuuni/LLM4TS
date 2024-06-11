package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.junit.Test;

/**
 * Test cases for {@link GaussNewtonOptimizer} class
 */
public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertNotNull(optimizer);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullConvergenceChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setStartPoint(null);
        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullTargetValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(null);
        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setWeight(null);
        optimizer.doOptimize();
    }

    @Test
    public void testGaussNewtonOptimizerConvergence() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] weightValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightValues);
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.doOptimize();
        RealVector point = result.getPoint();
        double cost = result.getValue();

        double[] expectedPoint = new double[]{1.0, 1.0, 1.0};
        double expectedCost = 0.0;

        assertTrue(Arrays.equals(expectedPoint, point.toArray()));
        assertEquals(expectedCost, cost, 1e-10);
    }

    @Test
    public void testGaussNewtonOptimizerSingularMatrixException() {
        double[] targetValues = new double[]{1.0, 0.0, 0.0};
        double[] weightValues = new double[]{1.0, 0.0, 0.0};
        double[] startPoint = new double[]{1.0, 1.0, 1.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightValues);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        } catch (Exception e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testGaussNewtonOptimizerNullStartPointAndConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setStartPoint(null);
        try {
            optimizer.doOptimize();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            optimizer.setConvergenceChecker(null);
            optimizer.doOptimize();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGaussNewtonOptimizerLUDecomposition() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] weightValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightValues);
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.doOptimize();
        RealVector point = result.getPoint();
        double cost = result.getValue();

        double[] expectedPoint = new double[]{1.0, 1.0, 1.0};
        double expectedCost = 0.0;

        assertTrue(Arrays.equals(expectedPoint, point.toArray()));
        assertEquals(expectedCost, cost, 1e-10);

        RealMatrix weightMatrix = optimizer.getWeight();
        RealMatrix jacobian = optimizer.getJacobian();

        assertEquals(3, weightMatrix.getRowDimension());
        assertEquals(3, weightMatrix.getColumnDimension());
        assertEquals(3, jacobian.getRowDimension());
        assertEquals(3, jacobian.getColumnDimension());

        RealMatrix expectedWeightedJacobian = new SingularValueDecomposition(jacobian).getS();
        RealMatrix actualWeightedJacobian = optimizer.computeWeightedJacobian(point.toArray());

        assertTrue(Arrays.deepEquals(expectedWeightedJacobian.getData(), actualWeightedJacobian.getData(), 1e-10));
    }

    @Test
    public void testGaussNewtonOptimizerQRDecomposition() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] weightValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightValues);
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.doOptimize();
        RealVector point = result.getPoint();
        double cost = result.getValue();

        double[] expectedPoint = new double[]{1.0, 1.0, 1.0};
        double expectedCost = 0.0;

        assertTrue(Arrays.equals(expectedPoint, point.toArray()));
        assertEquals(expectedCost, cost, 1e-10);

        RealMatrix weightMatrix = optimizer.getWeight();
        RealMatrix jacobian = optimizer.getJacobian();

        assertEquals(3, weightMatrix.getRowDimension());
        assertEquals(3, weightMatrix.getColumnDimension());
        assertEquals(3, jacobian.getRowDimension());
        assertEquals(3, jacobian.getColumnDimension());

        RealMatrix expectedWeightedJacobian = new QRDecomposition(jacobian).getQ().transpose().multiply(jacobian);
        RealMatrix actualWeightedJacobian = optimizer.computeWeightedJacobian(point.toArray());

        assertTrue(Arrays.deepEquals(expectedWeightedJacobian.getData(), actualWeightedJacobian.getData(), 1e-10));
    }

    @Test
    public void testGaussNewtonOptimizerLUDecompositionSingularMatrixException() {
        double[] targetValues = new double[]{1.0, 0.0, 0.0};
        double[] weightValues = new double[]{1.0, 0.0, 0.0};
        double[] startPoint = new double[]{1.0, 1.0, 1.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightValues);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        } catch (Exception e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testGaussNewtonOptimizerQRDecompositionSingularMatrixException() {
        double[] targetValues = new double[]{1.0, 0.0, 0.0};
        double[] weightValues = new double[]{1.0, 0.0, 0.0};
        double[] startPoint = new double[]{1.0, 1.0, 1.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightValues);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        } catch (Exception e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testGaussNewtonOptimizerEmptyWeightMatrix() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] weightValues = new double[0];
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightValues);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGaussNewtonOptimizerEmptyTargetValues() {
        double[] targetValues = new double[0];
        double[] weightValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightValues);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGaussNewtonOptimizerNullStartPointAndConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setStartPoint(null);
        try {
            optimizer.doOptimize();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            optimizer.setConvergenceChecker(null);
            optimizer.doOptimize();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}
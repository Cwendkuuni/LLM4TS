package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointVectorValuePair;

/**
 * Test cases for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test constructor with default settings.
     */
    @Test
    public void testConstructorDefault() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertNull(optimizer.getConvergenceChecker());
        assertTrue(optimizer.isUseLU());
    }

    /**
     * Test constructor with custom settings.
     */
    @Test
    public void testConstructorCustom() {
        ConvergenceChecker<PointVectorValuePair> checker = new MaxEval(100);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertFalse(optimizer.isUseLU());
    }

    /**
     * Test doOptimize method with dummy data.
     */
    @Test
    public void testDoOptimizeDummy() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 1.0);
        weightMatrix.setEntry(2, 2, 1.0);
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail("Exception expected.");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    /**
     * Test doOptimize method with valid data.
     */
    @Test
    public void testDoOptimizeValid() {
        ConvergenceChecker<PointVectorValuePair> checker = new MaxEval(100);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 1.0);
        weightMatrix.setEntry(2, 2, 1.0);
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);

        PointVectorValuePair result = optimizer.doOptimize();
        double[] expected = new double[]{1.0, 1.0, 1.0};
        assertEquals(expected, result.getPoint());
        assertEquals(targetValues, result.getValue());
    }

    /**
     * Test doOptimize method with invalid data (singular matrix).
     */
    @Test(expected = SingularMatrixException.class)
    public void testDoOptimizeSingularMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 0.0);
        weightMatrix.setEntry(1, 1, 1.0);
        weightMatrix.setEntry(2, 2, 1.0);
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.doOptimize();
    }

    /**
     * Test doOptimize method with invalid data (convergence not reached).
     */
    @Test
    public void testDoOptimizeNoConvergence() {
        ConvergenceChecker<PointVectorValuePair> checker = new MaxEval(1);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 1.0);
        weightMatrix.setEntry(2, 2, 1.0);
        optimizer.setWeight(weightMatrix);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail("Exception expected.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}
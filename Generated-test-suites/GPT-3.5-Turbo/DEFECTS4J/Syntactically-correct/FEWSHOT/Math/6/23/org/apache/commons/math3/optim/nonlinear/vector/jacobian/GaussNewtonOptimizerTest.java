import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Setup
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] target = {1.0, 2.0, 3.0};
        RealMatrix weight = new BlockRealMatrix(3, 3);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        weight.setEntry(2, 2, 1.0);
        double[] startPoint = {0.0, 0.0, 0.0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(checker);

        // Run
        try {
            PointVectorValuePair result = optimizer.doOptimize();

            // Verify
            assertNotNull(result);
            assertEquals(3, result.getPoint().length);
            assertEquals(1, result.getPoint()[0], 0.0);
            assertEquals(2, result.getPoint()[1], 0.0);
            assertEquals(3, result.getPoint()[2], 0.0);
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not have been thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        // Setup
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] target = {1.0, 2.0, 3.0};
        RealMatrix weight = new BlockRealMatrix(3, 3);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        weight.setEntry(2, 2, 1.0);
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(null);

        // Run
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimizeConvergenceException() {
        // Setup
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] target = {1.0, 2.0, 3.0};
        RealMatrix weight = new BlockRealMatrix(3, 3);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        weight.setEntry(2, 2, 1.0);
        double[] startPoint = {0.0, 0.0, 0.0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(checker);

        // Run
        try {
            PointVectorValuePair result = optimizer.doOptimize();

            // Verify
            assertNull(result);
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not have been thrown");
        }
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeMathInternalError() {
        // Setup
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] target = {1.0, 2.0, 3.0};
        RealMatrix weight = new BlockRealMatrix(3, 3);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        weight.setEntry(2, 2, 1.0);
        double[] startPoint = {0.0, 0.0, 0.0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(checker);

        // Run
        PointVectorValuePair result = optimizer.doOptimize();
    }

    @Test
    public void testDoOptimizeWithLU() {
        // Setup
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] target = {1.0, 2.0, 3.0};
        RealMatrix weight = new BlockRealMatrix(3, 3);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        weight.setEntry(2, 2, 1.0);
        double[] startPoint = {0.0, 0.0, 0.0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(checker);

        // Run
        try {
            PointVectorValuePair result = optimizer.doOptimize();

            // Verify
            assertNotNull(result);
            assertEquals(3, result.getPoint().length);
            assertEquals(1, result.getPoint()[0], 0.0);
            assertEquals(2, result.getPoint()[1], 0.0);
            assertEquals(3, result.getPoint()[2], 0.0);
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not have been thrown");
        }
    }

    @Test
    public void testDoOptimizeWithQR() {
        // Setup
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        double[] target = {1.0, 2.0, 3.0};
        RealMatrix weight = new BlockRealMatrix(3, 3);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 1.0);
        weight.setEntry(2, 2, 1.0);
        double[] startPoint = {0.0, 0.0, 0.0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(checker);

        // Run
        try {
            PointVectorValuePair result = optimizer.doOptimize();

            // Verify
            assertNotNull(result);
            assertEquals(3, result.getPoint().length);
            assertEquals(1, result.getPoint()[0], 0.0);
            assertEquals(2, result.getPoint()[1], 0.0);
            assertEquals(3, result.getPoint()[2], 0.0);
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not have been thrown");
        }
    }
}
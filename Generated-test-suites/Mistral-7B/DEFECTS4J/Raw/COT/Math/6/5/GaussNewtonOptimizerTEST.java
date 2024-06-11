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
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerConstructorDefault() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertFalse(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizerConstructorUseLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertTrue(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizerConstructorCheckers() {
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(convergenceChecker);
        assertTrue(optimizer.isUseLU());
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimizeNullConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertNull(optimizer.getConvergenceChecker());
        try {
            optimizer.doOptimize();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimizeEmptyWeight() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        RealVector startPoint = new ArrayRealVector(new double[]{1.0, 2.0});
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{0.0}, {0.0}});
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint.toArray());
        try {
            optimizer.doOptimize();
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimizeNonSquareMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        RealVector startPoint = new ArrayRealVector(new double[]{1.0, 2.0});
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0},
                {1.0, 1.0}
        });
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint.toArray());
        try {
            optimizer.doOptimize();
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimizeConvergenceChecker() {
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1E-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(convergenceChecker);
        RealVector startPoint = new ArrayRealVector(new double[]{1.0, 2.0});
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0},
                {1.0, 1.0}
        });
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint.toArray());
        PointValuePair result = optimizer.doOptimize();
        double[] expectedPoint = new double[]{1.5, 1.5};
        assertEquals(expectedPoint, result.getPoint(), 1E-6);
    }

    @Test
    public void testDoOptimizeWithQRDecomposition() {
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new SimpleVectorValueChecker(1E-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(convergenceChecker, false);
        RealVector startPoint = new ArrayRealVector(new double[]{1.0, 2.0});
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0},
                {1.0, 1.0}
        });
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint.toArray());
        PointValuePair result = optimizer.doOptimize();
        double[] expectedPoint = new double[]{1.5, 1.5};
        assertEquals(expectedPoint, result.getPoint(), 1E-6);
    }
}
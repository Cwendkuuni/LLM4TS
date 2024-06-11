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
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(true, optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());

        GaussNewtonOptimizer optimizerWithChecker = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        assertEquals(true, optimizerWithChecker.isUseLU());
        assertNotNull(optimizerWithChecker.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizerConstructorWithCheckerAndUseLUFalse() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        assertEquals(false, optimizer.isUseLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeNullWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setWeight(null);
        try {
            optimizer.doOptimize();
            fail();
        } catch (NullArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setStartPoint(null);
        try {
            optimizer.doOptimize();
            fail();
        } catch (NullArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeNullTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(null);
        try {
            optimizer.doOptimize();
            fail();
        } catch (NullArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeEmptyData() {
        double[] targetValues = new double[0];
        double[] startPoint = new double[0];
        RealMatrix weightMatrix = new Array2DRowRealMatrix(0, 1);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        try {
            optimizer.doOptimize();
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeSimpleData() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 1, new double[][]{{1.0}, {2.0}, {3.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.001);
        assertEquals(0.0, optimizer.getCost(), 0.001);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeException() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 1, new double[][]{{1.0}, {2.0}, {SingularValueDecomposition.SINGULAR_MATRIX}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail();
        } catch (ConvergenceException e) {
            // Expected result
        }
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeSingularMatrix() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3, new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail();
        } catch (SingularMatrixException e) {
            // Expected result
        }
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeIllegalArgumentException() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 1, new double[][]{{1.0}, {2.0}, {3.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);

        try {
            optimizer.doOptimize();
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeConvergenceChecker() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 1, new double[][]{{1.0}, {2.0}, {3.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        SimpleVectorValueChecker checker = (SimpleVectorValueChecker) optimizer.getConvergenceChecker();
        checker.setMaxIterations(5);
        checker.setRelativeGradient(1.0E-6);
        checker.setAbsoluteGradient(1.0E-6);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.001);
        assertEquals(0.0, optimizer.getCost(), 0.001);
        assertEquals(5, optimizer.getIterations());
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeNoConvergence() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 1, new double[][]{{1.0}, {2.0}, {3.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        SimpleVectorValueChecker checker = (SimpleVectorValueChecker) optimizer.getConvergenceChecker();
        checker.setMaxIterations(1);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertFalse(checker.isConverged());
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeNoIterations() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 1, new double[][]{{1.0}, {2.0}, {3.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        SimpleVectorValueChecker checker = (SimpleVectorValueChecker) optimizer.getConvergenceChecker();
        checker.setMaxIterations(0);

        try {
            optimizer.doOptimize();
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }
}
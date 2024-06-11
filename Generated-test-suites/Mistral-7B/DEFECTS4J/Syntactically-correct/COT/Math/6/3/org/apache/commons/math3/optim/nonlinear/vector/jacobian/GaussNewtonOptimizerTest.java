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
        assertNotNull(optimizer);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullChecker() {
        new GaussNewtonOptimizer(null);
    }

    @Test
    public void testGaussNewtonOptimizerDefaultSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(true, optimizer.isUsingLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizerUseLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertEquals(true, optimizer.isUsingLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizerUseQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        assertEquals(false, optimizer.isUsingLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
    }

    @Test
    public void testGaussNewtonOptimizerCustomConvergenceChecker() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertEquals(true, optimizer.isUsingLU());
        assertNotNull(optimizer.getConvergenceChecker());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerNoConvergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0};
        double[] startPoint = {0.0};
        RealMatrix weightMatrix = new QRDecomposition(new BlockRealMatrix(new double[1][1])).getMatrixQ().transpose();
        optimizer.optimize(targetValues, startPoint, weightMatrix);
    }

    @Test
    public void testGaussNewtonOptimizerConvergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new LUDecomposition(new BlockRealMatrix(new double[2][2])).getMatrixL().getTranspose();
        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);
        RealVector point = result.getPoint();
        double[] expected = {1.0, 1.0};
        assertArrayEquals(expected, point.toArray(), 1e-12);
    }

    @Test(expected = SingularMatrixException.class)
    public void testGaussNewtonOptimizerSingularMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[2][2]);
        weightMatrix.setEntry(0, 0, 0.0);
        weightMatrix.setEntry(1, 1, 0.0);
        optimizer.optimize(targetValues, startPoint, weightMatrix);
    }

    @Test
    public void testGaussNewtonOptimizerIterations() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new LUDecomposition(new BlockRealMatrix(new double[2][2])).getMatrixL().getTranspose();
        int iterations = 10;
        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix, iterations);
        RealVector point = result.getPoint();
        double[] expected = {1.0, 1.0};
        assertArrayEquals(expected, point.toArray(), 1e-12);
        assertEquals(iterations, optimizer.getIterations());
    }

    @Test
    public void testGaussNewtonOptimizerEmptyTargetValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] startPoint = {0.0};
        RealMatrix weightMatrix = new LUDecomposition(new BlockRealMatrix(new double[1][1])).getMatrixL().getTranspose();
        optimizer.optimize(new double[0][], startPoint, weightMatrix);
        assertNull(optimizer.getOptimizedPoint());
    }

    @Test
    public void testGaussNewtonOptimizerEmptyStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0};
        RealMatrix weightMatrix = new LUDecomposition(new BlockRealMatrix(new double[1][1])).getMatrixL().getTranspose();
        optimizer.optimize(targetValues, new double[0][], weightMatrix);
        assertNull(optimizer.getOptimizedPoint());
    }

    @Test
    public void testGaussNewtonOptimizerEmptyWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0};
        double[] startPoint = {0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[1][1]);
        optimizer.optimize(targetValues, startPoint, weightMatrix);
        assertNull(optimizer.getOptimizedPoint());
    }

    @Test
    public void testGaussNewtonOptimizerFixedStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new LUDecomposition(new BlockRealMatrix(new double[2][2])).getMatrixL().getTranspose();
        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);
        RealVector point = result.getPoint();
        double[] expected = {1.0, 1.0};
        assertArrayEquals(expected, point.toArray(), 1e-12);
        assertFalse(optimizer.isStartPointUpdated());
    }

    @Test
    public void testGaussNewtonOptimizerFixedWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new LUDecomposition(new BlockRealMatrix(new double[2][2])).getMatrixL().getTranspose();
        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);
        RealVector point = result.getPoint();
        double[] expected = {1.0, 1.0};
        assertArrayEquals(expected, point.toArray(), 1e-12);
        assertFalse(optimizer.isWeightMatrixUpdated());
    }

    @Test
    public void testGaussNewtonOptimizerCustomConvergenceCheckerAndIterations() {
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-2, 1e-2);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new LUDecomposition(new BlockRealMatrix(new double[2][2])).getMatrixL().getTranspose();
        int iterations = 10;
        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix, checker, iterations);
        RealVector point = result.getPoint();
        double[] expected = {1.0, 1.0};
        assertArrayEquals(expected, point.toArray(), 1e-12);
        assertEquals(iterations, optimizer.getIterations());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
}
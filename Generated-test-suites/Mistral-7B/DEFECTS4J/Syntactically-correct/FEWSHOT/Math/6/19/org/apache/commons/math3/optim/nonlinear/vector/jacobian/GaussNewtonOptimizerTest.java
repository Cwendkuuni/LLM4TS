package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;

import java.util.Random;

public class GaussNewtonOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testNullConstructor() {
        new GaussNewtonOptimizer(null);
    }

    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer();
        assertTrue(gno.useLU());
        assertSame(gno.getConvergenceChecker(), SimpleVectorValueChecker.DEFAULT);
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6, 1e-6);
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(checker);
        assertSame(gno.getConvergenceChecker(), checker);
        assertFalse(gno.useLU());
    }

    @Test
    public void testConstructorWithLU() {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(true);
        assertTrue(gno.useLU());
        assertSame(gno.getConvergenceChecker(), SimpleVectorValueChecker.DEFAULT);
    }

    @Test
    public void testConstructorWithCheckerAndLU() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6, 1e-6);
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(true, checker);
        assertSame(gno.getConvergenceChecker(), checker);
        assertTrue(gno.useLU());
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithoutChecker() {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer();
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        gno.optimize(startPoint, targetValues, weightMatrix);
    }

    @Test
    public void testOptimizeWithChecker() throws ConvergenceException {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6, 1e-6);
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(checker);
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        PointVectorValuePair result = gno.optimize(startPoint, targetValues, weightMatrix);
        assertArrayEquals(new double[]{2.5, 2.5}, result.getPoint(), 0.001);
        assertEquals(0.0, gno.getCost(), 0.001);
    }

    @Test
    public void testOptimizeWithLUDecomposition() throws ConvergenceException, SingularMatrixException {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(true);
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        PointVectorValuePair result = gno.optimize(startPoint, targetValues, weightMatrix);
        assertArrayEquals(new double[]{2.5, 2.5}, result.getPoint(), 0.001);
        assertEquals(0.0, gno.getCost(), 0.001);
    }

    @Test
    public void testOptimizeWithQRDecomposition() throws ConvergenceException, SingularMatrixException {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(false);
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        PointVectorValuePair result = gno.optimize(startPoint, targetValues, weightMatrix);
        assertArrayEquals(new double[]{2.5, 2.5}, result.getPoint(), 0.001);
        assertEquals(0.0, gno.getCost(), 0.001);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularProblem() throws ConvergenceException, SingularMatrixException {
        double[] startPoint = new double[3];
        double[] targetValues = new double[3];
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(true);
        gno.optimize(startPoint, targetValues, weightMatrix);
    }
}
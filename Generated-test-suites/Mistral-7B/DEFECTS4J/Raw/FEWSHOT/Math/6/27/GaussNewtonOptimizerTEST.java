package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testNullConstructor() {
        new GaussNewtonOptimizer(null);
    }

    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertFalse(optimizer.isUseLU());
        assertEquals(SimpleVectorValueChecker.class, optimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        assertFalse(optimizer.isUseLU());
        assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testConstructorWithLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertTrue(optimizer.isUseLU());
        assertEquals(SimpleVectorValueChecker.class, optimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testConstructorWithLUAndChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertTrue(optimizer.isUseLU());
        assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceExceptionWithSingularMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        RealMatrix matrix = new QRDecomposition(new BlockRealMatrix(new double[1][1])).getMatrixA();
        try {
            optimizer.doOptimize();
        } catch (SingularMatrixException e) {
            optimizer.getConvergenceChecker().checkConvergence(0, null, null);
            fail();
        }
    }

    @Test
    public void testOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);

        optimizer.doOptimize();
        PointVectorValuePair result = optimizer.getLastIterate();
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimizeWithLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);

        optimizer.doOptimize();
        PointVectorValuePair result = optimizer.getLastIterate();
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimizeWithQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);

        optimizer.doOptimize();
        PointVectorValuePair result = optimizer.getLastIterate();
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.001);
    }
}
package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;

/**
 * Test class for GaussNewtonOptimizer.
 */
public class GaussNewtonOptimizerTest {
    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        new GaussNewtonOptimizer(null);
    }

    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        assertEquals(SimpleVectorValueChecker.class, checker.getClass());
        assertEquals(true, optimizer.isUseLU());
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1.0E-12, 1.0E-12);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertEquals(true, optimizer.isUseLU());
    }

    @Test
    public void testConstructorWithLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        assertEquals(SimpleVectorValueChecker.class, checker.getClass());
        assertEquals(true, optimizer.isUseLU());
    }

    @Test
    public void testConstructorWithLUAndChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1.0E-12, 1.0E-12);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertEquals(true, optimizer.isUseLU());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetCheckerAfterConstruction() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetUseLUAfterConstruction() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setUseLU(false);
    }

    @Test
    public void testDoOptimizeWithDefaults() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        PointVectorValuePair result = optimizer.doOptimize();
        assertNull(result);
    }

    @Test
    public void testDoOptimizeWithChecker() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {-1.0, -1.0, -1.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker(1.0E-12, 1.0E-12));
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        PointVectorValuePair result = optimizer.doOptimize();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), 1.0E-13);
        assertEquals(0.0, optimizer.getCost(), 1.0E-13);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeWithoutChecker() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {-1.0, -1.0, -1.0};
        GaussNewtOptimizer optimizer = new GaussNewtOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.doOptimize();
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] targetValues = {1.0, 0.0, 0.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.doOptimize();
    }
}
package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Test;

/**
 * JUnit 4 test cases for GaussNewtonOptimizer class.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test constructor with default settings.
     */
    @Test
    public void testGaussNewtonOptimizerDefaultConstructor() {
        GaussNewtonOptimizer gnOptimizer = new GaussNewtonOptimizer();
        assertTrue(gnOptimizer.isUseLU());
        ConvergenceChecker<PointVectorValuePair> checker = gnOptimizer.getConvergenceChecker();
        assertEquals(SimpleVectorValueChecker.class, checker.getClass());
    }

    /**
     * Test constructor with custom settings.
     */
    @Test
    public void testGaussNewtonOptimizerCustomConstructor() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1.0E-4, 1.0E-4);
        GaussNewtonOptimizer gnOptimizer = new GaussNewtonOptimizer(false, checker);
        assertFalse(gnOptimizer.isUseLU());
        assertEquals(checker.getClass(), gnOptimizer.getConvergenceChecker().getClass());
    }

    /**
     * Test doOptimize method with LU decomposition.
     */
    @Test
    public void testGaussNewtonOptimizerLUDecomposition() throws ConvergenceException {
        GaussNewtonOptimizer gnOptimizer = new GaussNewtonOptimizer();
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {1.5, 2.5, 3.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});

        PointVectorValuePair result = gnOptimizer.doOptimize(targetValues, weightMatrix, startPoint);

        double[] expectedPoint = {1.0, 2.0, 3.0};
        double[] expectedObjective = {0.0, 0.0, 0.0};
        assertEquals(expectedPoint, result.getPoint());
        assertArrayEquals(expectedObjective, result.getValue(), 1e-6);
    }

    /**
     * Test doOptimize method with QR decomposition.
     */
    @Test
    public void testGaussNewtonOptimizerQRDecomposition() throws ConvergenceException {
        GaussNewtonOptimizer gnOptimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1.0E-4, 1.0E-4));
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {1.5, 2.5, 3.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});

        PointVectorValuePair result = gnOptimizer.doOptimize(targetValues, weightMatrix, startPoint);

        double[] expectedPoint = {1.0, 2.0, 3.0};
        double[] expectedObjective = {0.0, 0.0, 0.0};
        assertEquals(expectedPoint, result.getPoint());
        assertArrayEquals(expectedObjective, result.getValue(), 1e-6);
    }

    /**
     * Test doOptimize method with non-converged result.
     */
    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerNonConverged() {
        GaussNewtonOptimizer gnOptimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1.0E-4, 1.0E-4));
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {1.5, 2.5, 3.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        gnOptimizer.setConvergenceChecker(null);
        gnOptimizer.doOptimize(targetValues, weightMatrix, startPoint);
    }
}
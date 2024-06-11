package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test default constructor
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertFalse(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());

        // Test constructor with default settings and SimpleVectorValueChecker
        optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        assertTrue(optimizer.isUseLU());
        assertSame(optimizer.getConvergenceChecker(), new SimpleVectorValueChecker());

        // Test constructor with false useLU and SimpleVectorValueChecker
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        assertFalse(optimizer.isUseLU());
        assertSame(optimizer.getConvergenceChecker(), new SimpleVectorValueChecker());

        // Test constructor with true useLU and null ConvergenceChecker
        optimizer = new GaussNewtonOptimizer(true, null);
        assertTrue(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());

        // Test constructor with false useLU and null ConvergenceChecker
        optimizer = new GaussNewtonOptimizer(false, null);
        assertFalse(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());

        // Test constructor with true useLU and custom ConvergenceChecker
        ConvergenceChecker<PointVectorValuePair> customChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
                // Custom convergence condition
                double error = current.getValue()[0] - previous.getValue()[0];
                return Math.abs(error) < 0.001;
            }
        };
        optimizer = new GaussNewtonOptimizer(true, customChecker);
        assertTrue(optimizer.isUseLU());
        assertSame(optimizer.getConvergenceChecker(), customChecker);

        // Test constructor with false useLU and custom ConvergenceChecker
        optimizer = new GaussNewtonOptimizer(false, customChecker);
        assertFalse(optimizer.isUseLU());
        assertSame(optimizer.getConvergenceChecker(), customChecker);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
    }

    @Test
    public void testOptimize() throws ConvergenceException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        PointVectorValuePair result = optimizer.optimize(startPoint, targetValues, weightMatrix);

        assertArrayEquals(new double[]{2.5, 2.5}, result.getPoint(), 0.001);
        assertEquals(1.1647681105254238, result.getValue()[0], 0.001);
        assertEquals(1.1647681105254238, result.getValue()[1], 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws ConvergenceException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        optimizer.setMaxIterations(1);

        optimizer.optimize(startPoint, targetValues, weightMatrix);
    }

    @Test
    public void testSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {3.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});

        try {
            optimizer.optimize(startPoint, targetValues, weightMatrix);
            fail();
        } catch (SingularMatrixException e) {
            // Expected
        }
    }
}
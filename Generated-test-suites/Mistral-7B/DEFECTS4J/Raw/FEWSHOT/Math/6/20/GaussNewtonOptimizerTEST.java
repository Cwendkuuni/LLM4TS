package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.goese.GoalType;
import org.apache.commons.math3.optimization.goese.PointValuePair;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6, 1e-6));
        assertEquals(GaussNewtonOptimizer.class, optimizer.getClass());
        assertFalse(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
        assertTrue(optimizer.canSetStartPoint(new double[0].length));
        assertTrue(optimizer.canSetTarget(new double[0].length));
        assertTrue(optimizer.canSetWeight(new double[0][0].length));
        assertNull(optimizer.getStartPoint());
        assertNull(optimizer.getTarget());
        assertNull(optimizer.getWeight());
        assertNull(optimizer.getCost());
    }

    @Test
    public void testGaussNewtonOptimizer_useLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-6, 1e-6));
        assertTrue(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());

        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double[] w = {1.0, 1.0};

        optimizer.setStartPoint(x);
        optimizer.setTarget(y);
        optimizer.setWeight(new BlockRealMatrix(w));

        PointVectorValuePair result = optimizer.optimize();
        assertArrayEquals(new double[]{1.5, 1.5}, result.getPoint(), 0.001);
        assertEquals(1.0, optimizer.getCost(), 0.001);
        assertTrue(optimizer.isConverged());

        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {3.0, 4.0, 5.0};
        double[] w2 = {1.0, 1.0, 1.0};

        optimizer.setStartPoint(x2);
        optimizer.setTarget(y2);
        optimizer.setWeight(new BlockRealMatrix(w2));

        try {
            optimizer.optimize();
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        optimizer.setConvergenceChecker(new SimpleVectorValueChecker(1e-12, 1e-12));

        result = optimizer.optimize();
        assertArrayEquals(new double[]{1.3333333333333333, 1.666666666666667, 2.0}, result.getPoint(), 0.001);
        assertEquals(0.6666666666666666, optimizer.getCost(), 0.001);
        assertTrue(optimizer.isConverged());
    }

    @Test
    public void testGaussNewtonOptimizer_useQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6, 1e-6));
        assertFalse(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());

        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double[] w = {1.0, 1.0};

        optimizer.setStartPoint(x);
        optimizer.setTarget(y);
        optimizer.setWeight(new BlockRealMatrix(w));

        PointVectorValuePair result = optimizer.optimize();
        assertArrayEquals(new double[]{1.5, 1.5}, result.getPoint(), 0.001);
        assertEquals(1.0, optimizer.getCost(), 0.001);
        assertTrue(optimizer.isConverged());

        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {3.0, 4.0, 5.0};
        double[] w2 = {1.0, 1.0, 1.0};

        optimizer.setStartPoint(x2);
        optimizer.setTarget(y2);
        optimizer.setWeight(new BlockRealMatrix(w2));

        try {
            optimizer.optimize();
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        optimizer.setConvergenceChecker(new SimpleVectorValueChecker(1e-12, 1e-12));

        result = optimizer.optimize();
        assertArrayEquals(new double[]{1.3333333333333333, 1.666666666666667, 2.0}, result.getPoint(), 0.001);
        assertEquals(0.6666666666666666, optimizer.getCost(), 0.001);
        assertTrue(optimizer.isConverged());
    }

    @Test
    public void testGaussNewtonOptimizer_singularMatrixException() {
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double[] w = {1.0, 1.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6, 1e-6));
        optimizer.setStartPoint(x);
        optimizer.setTarget(y);
        optimizer.setWeight(new BlockRealMatrix(w));

        try {
            optimizer.setWeight(new SingularValueDecomposition(new BlockRealMatrix(new double[2][2]))
                    .getSolver().getInverse());
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }

        try {
            optimizer.optimize();
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}
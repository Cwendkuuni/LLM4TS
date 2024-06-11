package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Vector3D;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testNullConstructor() {
        new GaussNewtonOptimizer(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.setWeight(null);
        optimizer.optimize();
    }

    @Test
    public void testEmptyWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        RealMatrix weight = new BlockRealMatrix(new double[0][0]);
        optimizer.setWeight(weight);
        optimizer.optimize();
        assertNull(optimizer.getPoint());
        assertEquals(Double.NaN, optimizer.getCost(), 0.0);
    }

    @Test
    public void testEmptyArrayStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        double[] startPoint = {};
        optimizer.setStartPoint(startPoint);
        optimizer.optimize();
        assertNull(optimizer.getPoint());
        assertEquals(Double.NaN, optimizer.getCost(), 0.0);
    }

    @Test
    public void testZeroTargetValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        double[] targetValues = {};
        optimizer.setTarget(targetValues);
        optimizer.optimize();
        assertNull(optimizer.getPoint());
        assertEquals(Double.NaN, optimizer.getCost(), 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] targetValues = new double[]{-1.0, 1.0, 2.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        double[] expectedPoint = new double[]{1.0 / 3.0, 1.0 / 3.0, 1.0};
        double[] expectedObjective = new double[]{0.0, 0.0, 0.0};
        RealMatrix weight = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0},
        });

        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weight);

        optimizer.optimize();

        assertEquals(expectedPoint.length, optimizer.getPoint().length);
        assertArrayEquals(expectedPoint, optimizer.getPoint());

        assertEquals(expectedObjective.length, optimizer.getCost().length);
        assertArrayEquals(expectedObjective, optimizer.getCost());
    }

    @Test
    public void testGaussNewtonOptimizerQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());

        double[] targetValues = new double[]{-1.0, 1.0, 2.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        double[] expectedPoint = new double[]{1.0 / 3.0, 1.0 / 3.0, 1.0};
        double[] expectedObjective = new double[]{0.0, 0.0, 0.0};
        RealMatrix weight = new BlockRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0},
        });

        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weight);

        optimizer.optimize();

        assertEquals(expectedPoint.length, optimizer.getPoint().length);
        assertArrayEquals(expectedPoint, optimizer.getPoint());

        assertEquals(expectedObjective.length, optimizer.getCost().length);
        assertArrayEquals(expectedObjective, optimizer.getCost());
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerSingularProblem() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] targetValues = new double[]{1.0, 0.0};
        double[] startPoint = new double[]{0.0, 0.0};
        double[] expectedPoint = new double[]{1.0, 0.0};
        double[] expectedObjective = new double[]{0.0, 0.0};
        RealMatrix weight = new SingularValueDecomposition(new BlockRealMatrix(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
        })).getS();

        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weight);

        optimizer.optimize();
    }
}
package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testEmptyConstructor() {
        new GaussNewtonOptimizer();
    }

    @Test
    public void testSimpleConstructorWithDefaultSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertFalse(optimizer.isUseLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSimpleConstructorWithLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertTrue(optimizer.isUseLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizerConstructorWithChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertTrue(optimizer.isUseLU());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeWithoutChecker() {
        new GaussNewtonOptimizer();
        throw new UnsupportedOperationException("A ConvergenceChecker is required.");
    }

    @Test
    public void testOptimizeWithSimpleValueChecker() {
        final double[] targetValues = {1.0, 2.0};
        final RealMatrix weightMatrix = MatrixUtils.denseMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(new FunctionWithJacobian() {
            @Override
            public double value(double[] point) {
                return 0.5 * Math.pow(Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 2.0, 2.0), 0.5);
            }

            @Override
            public double[] gradient(double[] point) {
                double[] grad = new double[2];
                grad[0] = (point[0] - 1.0) * point[0];
                grad[1] = (point[1] - 2.0) * point[1];
                return grad;
            }
        });
        optimizer.optimize();

        double[] point = optimizer.getPoint();
        double cost = optimizer.getCost();
        assertEquals(new double[]{1.0, 1.0}, point, 1e-12);
        assertEquals(0.0, cost, 1e-12);
    }

    @Test
    public void testOptimizeWithLUDecomposition() {
        final double[] targetValues = {1.0, 2.0};
        final RealMatrix weightMatrix = MatrixUtils.denseMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(new FunctionWithJacobian() {
            @Override
            public double value(double[] point) {
                return 0.5 * Math.pow(Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 2.0, 2.0), 0.5);
            }

            @Override
            public double[] gradient(double[] point) {
                double[] grad = new double[2];
                grad[0] = (point[0] - 1.0) * point[0];
                grad[1] = (point[1] - 2.0) * point[1];
                return grad;
            }
        });
        optimizer.optimize();

        double[] point = optimizer.getPoint();
        double cost = optimizer.getCost();
        assertEquals(new double[]{1.0, 1.0}, point, 1e-12);
        assertEquals(0.0, cost, 1e-12);
    }

    @Test
    public void testOptimizeWithQRDecomposition() {
        final double[] targetValues = {1.0, 2.0};
        final RealMatrix weightMatrix = MatrixUtils.denseMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setFunction(new FunctionWithJacobian() {
            @Override
            public double value(double[] point) {
                return 0.5 * Math.pow(Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 2.0, 2.0), 0.5);
            }

            @Override
            public double[] gradient(double[] point) {
                double[] grad = new double[2];
                grad[0] = (point[0] - 1.0) * point[0];
                grad[1] = (point[1] - 2.0) * point[1];
                return grad;
            }
        });
        optimizer.optimize();

        double[] point = optimizer.getPoint();
        double cost = optimizer.getCost();
        assertEquals(new double[]{1.0, 1.0}, point, 1e-12);
        assertEquals(0.0, cost, 1e-12);
    }

    @Test
    public void testSingularProblem() {
        final double[] targetValues = {1.0, 2.0};
        final RealMatrix weightMatrix = MatrixUtils.singularValueDecomposition(MatrixUtils.eye(2)).getMatrixX();
        final GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(new FunctionWithJacobian() {
            @Override
            public double value(double[] point) {
                return 0.5 * Math.pow(Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 2.0, 2.0), 0.5);
            }

            @Override
            public double[] gradient(double[] point) {
                double[] grad = new double[2];
                grad[0] = (point[0] - 1.0) * point[0];
                grad[1] = (point[1] - 2.0) * point[1];
                return grad;
            }
        });
        try {
            optimizer.optimize();
            fail("A ConvergenceException should have been thrown.");
        } catch (ConvergenceException e) {
            // pass
        }

        assertNull(optimizer.getPoint());
        assertNull(optimizer.getCost());
    }

}

class FunctionWithJacobian {
    abstract double value(double[] point);

    abstract double[] gradient(double[] point);
}
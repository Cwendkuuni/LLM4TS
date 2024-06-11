package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.TestObjectiveFunction;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        assertNull(optimizer.getPoint());
        optimizer.setFunction(new TestObjectiveFunction(new double[]{-1.0, -1.0}, 2.0));
        optimizer.optimize();
        RealVector point = optimizer.getPoint();
        assertEquals(2, point.getDimension());
        assertArrayEquals(new double[]{0.5, 0.5}, point.toArray(), 1e-10);
    }

    @Test
    public void testGaussNewtonOptimizerLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertNull(optimizer.getPoint());
        optimizer.setFunction(new TestObjectiveFunction(new double[]{-1.0, -1.0}, 2.0));
        optimizer.optimize();
        RealVector point = optimizer.getPoint();
        assertEquals(2, point.getDimension());
        assertArrayEquals(new double[]{0.5, 0.5}, point.toArray(), 1e-10);
    }

    @Test
    public void testGaussNewtonOptimizerConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        assertNull(optimizer.getPoint());
        optimizer.setFunction(new TestObjectiveFunction(new double[]{-1.0, -1.0}, 0.01));
        optimizer.setMaxIterations(10);
        try {
            optimizer.optimize();
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
        assertNull(optimizer.getPoint());

        optimizer.setMaxIterations(100);
        optimizer.optimize();
        RealVector point = optimizer.getPoint();
        assertEquals(2, point.getDimension());
        assertArrayEquals(new double[]{0.5, 0.5}, point.toArray(), 1e-10);
    }

    @Test
    public void testGaussNewtonOptimizerSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        assertNull(optimizer.getPoint());
        optimizer.setFunction(new TestObjectiveFunctionWithSingularMatrix(new double[]{-1.0, -1.0}, 2.0));
        try {
            optimizer.optimize();
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }
        assertNull(optimizer.getPoint());
    }
}

class TestObjectiveFunction implements org.apache.commons.math3.optimization.ObjectiveFunction {

    private final double[] target;
    private final double cost;

    public TestObjectiveFunction(double[] startPoint, double cost) {
        this.target = startPoint;
        this.cost = cost;
    }

    @Override
    public double value(RealVector x) {
        double sum = 0.0;
        for (int i = 0; i < x.getDimension(); ++i) {
            sum += Math.pow(Math.pow(x.getEntry(i), 2.0) - Math.pow(target[i], 2.0), 2.0);
        }
        return sum;
    }

    @Override
    public RealVector gradient(RealVector x) {
        RealVector gradient = new ArrayRealVector(new double[x.getDimension()]);
        for (int i = 0; i < x.getDimension(); ++i) {
            gradient.setEntry(i, 2.0 * (Math.pow(target[i], 2.0) - Math.pow(x.getEntry(i), 2.0)) * x.getEntry(i));
        }
        return gradient;
    }
}

class TestObjectiveFunctionWithSingularMatrix implements org.apache.commons.math3.optimization.ObjectiveFunction {

    private final double[] target;
    private final double cost;

    public TestObjectiveFunctionWithSingularMatrix(double[] startPoint, double cost) {
        this.target = startPoint;
        this.cost = cost;
    }

    @Override
    public double value(RealVector x) {
        double sum = 0.0;
        for (int i = 0; i < x.getDimension(); ++i) {
            sum += Math.pow(Math.pow(x.getEntry(i), 2.0) - Math.pow(target[i], 2.0), 2.0);
        }
        if (Math.abs(x.getEntry(0) - x.getEntry(1)) < 1e-15) {
            throw new SingularMatrixException("Singular matrix");
        }
        return sum;
    }

    @Override
    public RealVector gradient(RealVector x) {
        RealVector gradient = new ArrayRealVector(new double[x.getDimension()]);
        for (int i = 0; i < x.getDimension(); ++i) {
            gradient.setEntry(i, 2.0 * (Math.pow(target[i], 2.0) - Math.pow(x.getEntry(i), 2.0)) * x.getEntry(i));
        }
        return gradient;
    }
}
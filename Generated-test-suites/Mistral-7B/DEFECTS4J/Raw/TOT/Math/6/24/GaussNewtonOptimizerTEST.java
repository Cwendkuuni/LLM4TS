package org.apache.commons.math3.optimization.general;

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
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AverageInitializer;
import org.apache.commons.math3.optim.initializer.GaussianRandomInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.point.PointValuePairMaker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private ObjectiveFunction function;
    private PointVectorValuePair initialPoint;
    private RealVector target;
    private RealMatrix weight;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        function = new MyObjectiveFunction();
        initialPoint = new PointValuePairMaker().makePointValuePair(new AverageInitializer(3).getInitialPoint(function));
        target = new ArrayRealVector(new double[] {0.5, 1.0, 2.0});
        weight = new BlockRealMatrix(new double[][] {
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}});
    }

    @Test
    public void testOptimize() {
        optimizer.setObjectiveFunction(function);
        optimizer.setStartPoint(initialPoint.getPoint());
        optimizer.setWeight(weight);
        PointVectorValuePair result = optimizer.optimize();
        Assert.assertArrayEquals(new double[] {0.5, 1.0, 2.0}, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullObjectiveFunction() {
        optimizer.setObjectiveFunction(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullWeight() {
        optimizer.setWeight(null);
        optimizer.optimize();
    }

    @Test
    public void testOptimizeWithGaussianRandomInitializer() {
        optimizer.setObjectiveFunction(function);
        optimizer.setStartPoint(new GaussianRandomInitializer(3).getInitialPoint(function));
        optimizer.setWeight(weight);
        PointVectorValuePair result = optimizer.optimize();
        Assert.assertArrayEquals(new double[] {0.5, 1.0, 2.0}, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithSingularWeightMatrix() {
        RealMatrix singularWeight = new SingularValueDecomposition(new BlockRealMatrix(new double[][] {
            {0.0, 1.0, 0.0},
            {1.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}})).getSolver().getInverse();
        optimizer.setObjectiveFunction(function);
        optimizer.setStartPoint(initialPoint.getPoint());
        optimizer.setWeight(singularWeight);
        try {
            optimizer.optimize();
            Assert.fail();
        } catch (SingularMatrixException e) {
            // expected
        }
    }

    @Test
    public void testIterationCount() {
        optimizer.setIterationLimit(1);
        optimizer.setObjectiveFunction(function);
        optimizer.setStartPoint(initialPoint.getPoint());
        optimizer.setWeight(weight);
        optimizer.optimize();
        Assert.assertEquals(1, optimizer.getIterationCount());
    }

    @Test
    public void testConvergenceChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new ConvergenceChecker<PointVectorValuePair>() {
            private final double tolerance = 1e-6;

            @Override
            public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
                double error = current.getValue() - previous.getValue();
                return Math.abs(error) < tolerance;
            }
        };
        optimizer = new GaussNewtonOptimizer(false, checker);
        optimizer.setObjectiveFunction(function);
        optimizer.setStartPoint(initialPoint.getPoint());
        optimizer.setWeight(weight);
        optimizer.optimize();
        Assert.assertTrue("Convergence checker should have detected convergence", checker.isConverged());
    }

    @Test
    public void testUseQRDecomposition() {
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setObjectiveFunction(function);
        optimizer.setStartPoint(initialPoint.getPoint());
        optimizer.setWeight(weight);
        PointVectorValuePair result = optimizer.optimize();
        Assert.assertArrayEquals(new double[] {0.5, 1.0, 2.0}, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);

        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setUseLU(false);
        optimizer.setObjectiveFunction(function);
        optimizer.setStartPoint(initialPoint.getPoint());
        optimizer.setWeight(weight);
        result = optimizer.optimize();
        Assert.assertArrayEquals(new double[] {0.5, 1.0, 2.0}, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Objective function for testing.
     */
    private static class MyObjectiveFunction implements ObjectiveFunction {

        @Override
        public double value(RealVector x) {
            return (x.getEntry(0) - 0.5) * (x.getEntry(0) - 0.5)
                 + (x.getEntry(1) - 1.0) * (x.getEntry(1) - 1.0)
                 + (x.getEntry(2) - 2.0) * (x.getEntry(2) - 2.0);
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }
}
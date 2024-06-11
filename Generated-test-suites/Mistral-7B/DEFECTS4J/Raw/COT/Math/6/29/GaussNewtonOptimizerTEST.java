package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
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
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.simple.SimplePointValuePair;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.general.GoalType;
import org.apache.commons.math3.optimization.general.StoppingCriteria;
import org.junit.Test;

/**
 * Tests for the {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertTrue(optimizer.isUseLU());
        assertFalse(optimizer.hasConvergenceChecker());
    }

    /**
     * Test constructor with custom settings.
     */
    @Test
    public void testCustomConstructor() {
        ConvergenceChecker<PointValuePair> checker = new SimplePointValuePairChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);
        assertFalse(optimizer.isUseLU());
        assertTrue(optimizer.hasConvergenceChecker());
    }

    /**
     * Test doOptimize method with default settings.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testDoOptimizeDefaultSettings() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        RealMatrix weightMatrix = MatrixUtils.eye(3);
        RealVector startPoint = VectorUtils.createRealVector(3, 1.0);
        RealVector targetValues = VectorUtils.createRealVector(3, new double[]{1.0, 2.0, 3.0});
        optimizer.setInitialGuess(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStoppingCriteria(new StoppingCriteria(100, 1.0e-6));
        optimizer.optimize(targetValues, weightMatrix);

        PointValuePair pointValuePair = optimizer.getPointValuePair();
        RealVector point = pointValuePair.getPoint();
        RealVector value = pointValuePair.getValue();

        RealVector expectedPoint = VectorUtils.createRealVector(3, new double[]{2.475638440768312E-2, -1.5713341598058447E-2, 1.853108145649223E-3});
        RealVector expectedValue = VectorUtils.createRealVector(3, new double[]{1.0000000000000002E-6, 1.1102230246251565E-5, 6.328137868756647E-9});

        assertArrayEquals(expectedPoint.toArray(), point.toArray(), 1e-8);
        assertArrayEquals(expectedValue.toArray(), value.toArray(), 1e-8);
    }

    /**
     * Test doOptimize method with custom settings.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testDoOptimizeCustomSettings() throws Exception {
        ConvergenceChecker<PointValuePair> checker = new SimplePointValuePairChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);

        RealMatrix weightMatrix = MatrixUtils.eye(3);
        RealVector startPoint = VectorUtils.createRealVector(3, 1.0);
        RealVector targetValues = VectorUtils.createRealVector(3, new double[]{1.0, 2.0, 3.0});
        optimizer.setInitialGuess(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStoppingCriteria(new StoppingCriteria(100, 1.0e-6));
        optimizer.setUseLU(false);
        optimizer.optimize(targetValues, weightMatrix);

        PointValuePair pointValuePair = optimizer.getPointValuePair();
        RealVector point = pointValuePair.getPoint();
        RealVector value = pointValuePair.getValue();

        RealVector expectedPoint = SingularValueDecomposition.compute(MatrixUtils.identity(3)).getSolution().toRealVector();
        RealVector expectedValue = VectorUtils.createRealVector(3, new double[]{0.0, 0.0, 0.0});

        assertArrayEquals(expectedPoint.toArray(), point.toArray(), 1e-8);
        assertArrayEquals(expectedValue.toArray(), value.toArray(), 1e-8);
    }

    /**
     * Test that the optimizer throws an exception if the convergence checker is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(new double[0], new RealMatrix(0, 0));
    }

    /**
     * Test that the optimizer throws an exception if the target values or
     * weight matrix is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullTargetOrWeight() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(null, new RealMatrix(0, 0));
    }

    /**
     * Test that the optimizer throws an exception if the weight matrix is not square.
     */
    @Test(expected = SingularMatrixException.class)
    public void testNonSquareWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(new RealVector(3), new RealMatrix(3, 2));
    }

    /**
     * Test that the optimizer throws an exception if the weight matrix is singular.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(new RealVector(3), SingularValueDecomposition.compute(MatrixUtils.identity(3)).getMatrixU());
    }

    /**
     * Test that the optimizer throws an exception if the weighted jacobian cannot
     * be computed.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedComputeWeightedJacobian() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(new RealVector(3), new RealMatrix(3, 3).mapMultiply(new RealMatrix(3, 3).mapMultiply(new RealMatrix[3])));
    }
}

class SimplePointValuePairChecker implements ConvergenceChecker<PointValuePair> {

    @Override
    public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
        return previous == null || (current.getValue().getMaxValue() <= 1e-6 && previous.getValue().getMaxValue() > 1e-6);
    }
}
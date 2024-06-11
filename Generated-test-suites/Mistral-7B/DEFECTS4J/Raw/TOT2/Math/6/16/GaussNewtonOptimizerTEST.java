package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;

import org.junit.Test;

/**
 * Test cases for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(true, optimizer.isUseLU());
        assertEquals(new SimpleValueChecker(), optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with LU decomposition and custom convergence checker.
     */
    @Test
    public void testLUConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker());
        assertEquals(true, optimizer.isUseLU());
        assertEquals(new SimpleValueChecker(), optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with QR decomposition and custom convergence checker.
     */
    @Test
    public void testQRConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleValueChecker());
        assertEquals(false, optimizer.isUseLU());
        assertEquals(new SimpleValueChecker(), optimizer.getConvergenceChecker());
    }

    /**
     * Test {@link GaussNewtonOptimizer#GaussNewtonOptimizer(ConvergenceChecker, boolean)}
     * constructor with invalid arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChecker() {
        new GaussNewtonOptimizer(null, true);
    }

    /**
     * Test optimize method with valid input.
     */
    @Test
    public void testOptimizeWithValidInput() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(
                new SimpleFunction(), new ArrayRealVector(new double[] { 1.0, 1.0 }));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        PointValuePair solution = optimizer.optimize(problem);
        double[] expectedSolution = new double[] { 1.0, 1.0 };
        assertArrayEquals(expectedSolution, solution.getPoint(), 0.001);
        assertEquals(1.0, solution.getValue(), 0.001);
    }

    /**
     * Test optimize method with {@link SingularMatrixException} when solving normal equations.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithSingularMatrixException() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(
                new SingularFunction(), new ArrayRealVector(new double[] { 1.0, 1.0 }));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(problem);
    }

    /**
     * Test optimize method with invalid input (null weight matrix).
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullWeightMatrix() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(
                new SimpleFunction(), new ArrayRealVector(new double[] { 1.0, 1.0 }));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setWeight(null);
        optimizer.optimize(problem);
    }

    /**
     * Test optimize method with invalid input (non-square weight matrix).
     */
    @Test(expected = IllegalStateException.class)
    public void testOptimizeWithNonSquareWeightMatrix() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(
                new SimpleFunction(), new ArrayRealVector(new double[] { 1.0, 1.0 }));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][] { { 1.0 }, { 1.0, 2.0 } });
        optimizer.setWeight(weightMatrix);
        optimizer.optimize(problem);
    }

    /**
     * Test optimize method with invalid input (null start point).
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullStartPoint() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(
                new SimpleFunction(), null);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(problem);
    }

    /**
     * Test optimize method with invalid input (non-matching dimensions between the target and weighted jacobian).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithMismatchedDimensions() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(
                new SimpleFunction(), new ArrayRealVector(new double[] { 1.0, 1.0, 1.0 }));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(problem);
    }

    /**
     * Test {@link GaussNewtonOptimizer#computeObjectiveValue(double[])} method with valid input.
     */
    @Test
    public void testComputeObjectiveValueWithValidInput() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(
                new SimpleFunction(), new ArrayRealVector(new double[] { 1.0, 1.0 }));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] currentPoint = new double[] { 1.0, 1.0 };
        double[] expectedObjectiveValue = new double[] { 2.0, 3.0 };
        double[] actualObjectiveValue = optimizer.computeObjectiveValue(currentPoint);
        assertArrayEquals(expectedObjectiveValue, actualObjectiveValue, 0.001);
    }

    /**
     * Test {@link GaussNewtonOptimizer#computeResiduals(double[])} method with valid input.
     */
    @Test
    public void testComputeResidualsWithValidInput() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(
                new SimpleFunction(), new ArrayRealVector(new double[] { 1.0, 1.0 }));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] currentPoint = new double[] { 1.0, 1.0 };
        double[] expectedResiduals = new double[] { 1.0, 2.0 };
        double[] actualResiduals = optimizer.computeResiduals(currentPoint);
        assertArrayEquals(expectedResiduals, actualResiduals, 0.001);
    }

    /**
     * Test {@link GaussNewtonOptimizer#computeWeightedJacobian(double[])} method with valid input.
     */
    @Test
    public void testComputeWeightedJacobianWithValidInput() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(
                new SimpleFunction(), new ArrayRealVector(new double[] { 1.0, 1.0 }));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] currentPoint = new double[] { 1.0, 1.0 };
        RealMatrix expectedWeightedJacobian = new SingularValueDecomposition(
                new BlockRealMatrix(new double[][] { { 1.0, 1.0 }, { 2.0, 3.0 } })).getTranspose();
        RealMatrix actualWeightedJacobian = optimizer.computeWeightedJacobian(currentPoint);
        assertEquals(expectedWeightedJacobian.getRowDimension(), actualWeightedJacobian.getRowDimension());
        assertEquals(expectedWeightedJacobian.getColumnDimension(), actualWeightedJacobian.getColumnDimension());
        assertArrayEquals(expectedWeightedJacobian.getData(), actualWeightedJacobian.getData(), 0.001);
    }
}

class SimpleFunction implements org.apache.commons.math3.optim.UnivariateFunction {
    @Override
    public double value(double x) {
        return x[0] * x[0] + x[1] * x[1];
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }
}
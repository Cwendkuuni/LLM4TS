package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
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
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest extends UnconstrainedOptimizationTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        double[] targetValues = {1.0, 2.0};
        RealMatrix weightMatrix = MatrixUtils.dense2DIdentity(targetValues.length);
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setObjectiveFunction(this::simpleObjectiveFunction);
        optimizer.setStartPoint(new ArrayRealVector(new double[] {-1.0, -1.0}));
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
    }

    /**
     * Test constructor with default settings.
     */
    @Test
    public void testDefault() {
        optimizer = new GaussNewtonOptimizer();
        assertFalse(optimizer.isUseLU());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with LU decomposition.
     */
    @Test
    public void testLU() {
        optimizer = new GaussNewtonOptimizer(true);
        assertTrue(optimizer.isUseLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with QR decomposition.
     */
    @Test
    public void testQR() {
        optimizer = new GaussNewtonOptimizer(false);
        assertFalse(optimizer.isUseLU());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization with LU decomposition.
     */
    @Test
    public void testOptimizationLU() throws ConvergenceException {
        optimizer.optimize();
        PointValuePair result = optimizer.getValue();
        RealVector point = result.getPoint();
        double[] expected = {0.0, 0.0};
        double tolerance = 1e-9;
        assertEquals(expected, point.toArray(), tolerance);
        assertEquals(0.0, optimizer.getCost(), tolerance);
    }

    /**
     * Test optimization with QR decomposition.
     */
    @Test
    public void testOptimizationQR() throws ConvergenceException {
        optimizer.setUseLU(false);
        optimizer.optimize();
        PointValuePair result = optimizer.getValue();
        RealVector point = result.getPoint();
        double[] expected = {0.0, 0.0};
        double tolerance = 1e-9;
        assertEquals(expected, point.toArray(), tolerance);
        assertEquals(0.0, optimizer.getCost(), tolerance);
    }

    /**
     * Test optimization with singular matrix.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizationSingular() throws ConvergenceException {
        RealMatrix weightMatrix = new SingularValueDecomposition(MatrixUtils.dense2DIdentity(2)).getSolver().getMatrixB();
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test computation of objective value.
     */
    @Test
    public void testObjectiveValue() {
        double[] targetValues = {1.0, 2.0};
        double[] currentPoint = {-1.0, -1.0};
        double[] expected = {3.0, 4.0};
        double tolerance = 1e-9;
        assertEquals(expected, optimizer.computeObjectiveValue(currentPoint), tolerance);
    }

    /**
     * Test computation of residuals.
     */
    @Test
    public void testResiduals() {
        double[] expected = {-2.0, -1.0};
        double tolerance = 1e-9;
        assertEquals(expected, optimizer.computeResiduals(new double[] {1.0, 1.0}), tolerance);
    }

    /**
     * Test computation of weighted jacobian.
     */
    @Test
    public void testWeightedJacobian() {
        double[][] expected = {{1.0, 1.0}, {1.0, 1.0}};
        double tolerance = 1e-9;
        assertEquals(expected, optimizer.computeWeightedJacobian(new double[] {1.0, 1.0}).getDataStore().toArray(), tolerance);
    }

    /**
     * Test computation of cost.
     */
    @Test
    public void testCost() {
        double expected = 5.0;
        assertEquals(expected, optimizer.computeCost(new double[] {1.0, 1.0}), tolerance);
    }

    /**
     * Test optimization with null convergence checker.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizationNullChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.optimize();
    }

    /**
     * Test objective function.
     */
    private double[] simpleObjectiveFunction(double[] point) {
        double[] targetValues = {1.0, 2.0};
        double[] weights = {0.5, 0.5};
        double sum = 0.0;
        for (int i = 0; i < targetValues.length; i++) {
            sum += Math.pow(targetValues[i] - point[i], 2.0) * weights[i];
        }
        return new double[] {sum};
    }
}
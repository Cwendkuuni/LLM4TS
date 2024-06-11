package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
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
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Vector3D;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Comprehensive tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-10, 1.0e-10));
    }

    @Test
    public void testGaussNewtonOptimizerConstruction() {
        // Default constructor
        GaussNewtonOptimizer optimizer1 = new GaussNewtonOptimizer();
        assertTrue(optimizer1 instanceof GaussNewtonOptimizer);
        // Constructor with LU decomposition
        GaussNewtonOptimizer optimizer2 = new GaussNewtonOptimizer(true);
        assertTrue(optimizer2 instanceof GaussNewtonOptimizer);
        // Constructor with QR decomposition
        GaussNewtonOptimizer optimizer3 = new GaussNewtonOptimizer(false);
        assertTrue(optimizer3 instanceof GaussNewtonOptimizer);
        // Constructor with LU decomposition and custom convergence checker
        GaussNewtonOptimizer optimizer4 = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-5, 1.0e-5));
        assertTrue(optimizer4 instanceof GaussNewtonOptimizer);
        // Constructor with QR decomposition and custom convergence checker
        GaussNewtonOptimizer optimizer5 = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1.0e-5, 1.0e-5));
        assertTrue(optimizer5 instanceof GaussNewtonOptimizer);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeNullWeight() {
        expectedException.expect(NullArgumentException.class);
        optimizer.doOptimize(null);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeNullStartPoint() {
        expectedException.expect(NullArgumentException.class);
        optimizer.doOptimize(null, null);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeNullTarget() {
        expectedException.expect(NullArgumentException.class);
        optimizer.doOptimize(new double[]{}, null);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeEmptyTarget() {
        double[] targetValues = {};
        double[] startPoint = {1.0, 2.0};
        double[] expectedPoint = {};
        PointVectorValuePair result = optimizer.doOptimize(targetValues, startPoint);
        assertNull(result);
        assertArrayEquals(expectedPoint, startPoint);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeInvalidMaxIterations() {
        ObjectiveFunction objectiveFunction = new MaxEval(new Vector3D(0.0, 0.0), 0);
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {1.0, 1.0};
        expectedException.expect(UnsupportedOperationException.class);
        optimizer.doOptimize(targetValues, startPoint, objectiveFunction, 0);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeInvalidGoalType() {
        ObjectiveFunction objectiveFunction = new MaxEval(new Vector3D(0.0, 0.0), 0);
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {1.0, 1.0};
        expectedException.expect(UnsupportedOperationException.class);
        optimizer.doOptimize(targetValues, startPoint, objectiveFunction, GoalType.MINIMIZE);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeConvergenceCheckerNull() {
        double[] startPoint = {1.0, 2.0};
        double[] targetValues = {1.0, 1.0};
        expectedException.expect(NullArgumentException.class);
        optimizer = new GaussNewtonOptimizer(false, null);
        optimizer.doOptimize(targetValues, startPoint);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeSimpleExample() {
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double... arguments) {
                double x = arguments[0];
                double y = arguments[1];
                return Math.pow(x - 3.0, 2.0) + Math.pow(y - 2.0, 2.0);
            }

            @Override
            public double[] gradient(double... arguments) {
                double x = arguments[0];
                double y = arguments[1];
                double[] result = new double[2];
                result[0] = 2.0 * (x - 3.0);
                result[1] = 2.0 * (y - 2.0);
                return result;
            }
        };
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {3.0, 2.0};
        double[] expectedPoint = {3.0, 2.0};
        PointVectorValuePair result = optimizer.doOptimize(targetValues, startPoint, objectiveFunction, 100);
        assertTrue(result != null);
        assertArrayEquals(expectedPoint, result.getPoint());
        assertArrayEquals(new double[]{0.0, 0.0}, result.getValue(), 1.0e-10);
    }

    @Test
    public void testGaussNewtonOptimizerDoOptimizeSingularMatrixException() {
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double... arguments) {
                double x = arguments[0];
                double y = arguments[1];
                return Math.pow(x - 3.0, 2.0) + Math.pow(y - 2.0, 2.0);
            }

            @Override
            public double[] gradient(double... arguments) {
                double x = arguments[0];
                double y = arguments[1];
                double[] result = new double[2];
                result[0] = 2.0 * (x - 3.0);
                result[1] = 2.0 * (y - 2.0);
                return result;
            }
        };
        double[] startPoint = {1.0, 1.0};
        double[] targetValues = {3.0, 2.0};
        expectedException.expect(ConvergenceException.class);
        optimizer.doOptimize(targetValues, startPoint, objectiveFunction, 10);
    }
}
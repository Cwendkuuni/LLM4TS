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
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private ObjectiveFunction function;
    private RealVector initialPoint;

    @Before
    public void setup() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        function = new ObjectiveFunction() {
            @Override
            public double value(RealVector vector) {
                double[] point = vector.toArray();
                return point[0] * point[0] + point[1] * point[1];
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        initialPoint = new ArrayRealVector(new double[]{1.0, 1.0});
    }

    /**
     * Test optimization with LU decomposition.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testOptimizeLU() throws Exception {
        optimizer.setFunction(function);
        optimizer.setStartPoint(initialPoint);
        PointValuePair solution = optimizer.optimize();
        assertEquals(0.0, solution.getValue(), 1e-7);
        assertEquals(0.0, solution.getPoint()[0], 1e-7);
        assertEquals(0.0, solution.getPoint()[1], 1e-7);
    }

    /**
     * Test optimization with QR decomposition.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testOptimizeQR() throws Exception {
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setFunction(function);
        optimizer.setStartPoint(initialPoint);
        PointValuePair solution = optimizer.optimize();
        assertEquals(0.0, solution.getValue(), 1e-7);
        assertEquals(0.0, solution.getPoint()[0], 1e-7);
        assertEquals(0.0, solution.getPoint()[1], 1e-7);
    }

    /**
     * Test optimization with wrong function.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWrongFunction() throws Exception {
        optimizer.setFunction(null);
        optimizer.optimize();
    }

    /**
     * Test optimization with wrong start point.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = NullArgumentException.class)
    public
    void testOptimizeWrongStartPoint() throws Exception {
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    /**
     * Test optimization with an unbounded domain.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = UnboundedDomainException.class)
    public void testOptimizeUnboundedDomain() throws Exception {
        function = new ObjectiveFunction() {
            @Override
            public double value(RealVector vector) {
                double[] point = vector.toArray();
                return Math.log(point[0]);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        optimizer.setFunction(function);
        optimizer.setStartPoint(new ArrayRealVector(new double[]{-1.0}));
        optimizer.optimize();
    }

    /**
     * Test optimization with an ill-conditioned problem.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = SingularMatrixException.class)
    public void testOptimizeIllConditioned() throws Exception {
        function = new ObjectiveFunction() {
            @Override
            public double value(RealVector vector) {
                double[] point = vector.toArray();
                return Math.pow(point[0], 2) / Math.pow(point[1], 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        optimizer.setFunction(function);
        optimizer.setStartPoint(initialPoint);
        optimizer.optimize();
    }
}
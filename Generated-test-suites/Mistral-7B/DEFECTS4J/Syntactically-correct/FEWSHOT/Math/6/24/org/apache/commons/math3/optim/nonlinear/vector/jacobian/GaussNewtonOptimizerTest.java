package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointValueChecker;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnboundedMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LsqLinearConstraintChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LsqLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LsqLinearConstraintSet;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    private GaussNewtonOptimizer optimizer;
    private double[] startPoint;
    private double[] targetValues;
    private RealMatrix weightMatrix;
    private LinearObjectiveFunction objectiveFunction;
    private LsqLinearConstraintSet constraintSet;
    private SimpleVectorValueChecker checker;

    @Before
    public void setUp() {
        startPoint = new double[]{-1.0, 1.0};
        targetValues = new double[]{3.0, 2.0};
        weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });

        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);

        objectiveFunction = new LsqLinearObjectiveFunction(new LinearConstraintSet(), new ArrayRealVector(targetValues));
        constraintSet = new LsqLinearConstraintSet();
        checker = new SimpleVectorValueChecker();
    }

    @Test
    public void testOptimizeWithLUDecomposition() {
        optimizer.setUseLU(true);
        PointValuePair result = optimizer.optimize();
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(new double[]{-1.154700538379253, 1.154700538379253}, result.getValue());
        assertEquals(new double[]{1.9999999999999998E-5, 2.7763568394465446E-7}, result.getPoint(), 1e-9);
    }

    @Test
    public void testOptimizeWithQRDecomposition() {
        optimizer.setUseLU(false);
        PointValuePair result = optimizer.optimize();
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(new double[]{-1.154700538379253, 1.154700538379253}, result.getValue());
        assertEquals(new double[]{1.9999999999999998E-5, 2.7763568394465446E-7}, result.getPoint(), 1e-9);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullTarget() {
        optimizer.setTarget(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullWeight() {
        optimizer.setWeight(null);
        optimizer.optimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithSingularMatrix() {
        optimizer.setUseLU(true);
        weightMatrix = new SingularValueDecomposition(new BlockRealMatrix(new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        })).getMatrixL();
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithInvalidConstraint() {
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, new double[]{1.0}, 0.0);
        constraintSet.addConstraint(constraint);
        optimizer.setConstraintSet(constraintSet);
        optimizer.setConstraintChecker(new LsqLinearConstraintChecker());
        optimizer.optimize();
    }

    @Test
    public void testOptimizeWithUnboundedConstraint() {
        LinearConstraint constraint = new LinearConstraint(new double[]{0.0}, new double[]{0.0}, Double.POSITIVE_INFINITY);
        constraintSet.addConstraint(constraint);
        optimizer.setConstraintSet(constraintSet);
        optimizer.setConstraintChecker(new UnboundedMinimizer());
        PointValuePair result = optimizer.optimize();
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, 0.0}, result.getValue());
        assertNull(result.getPoint());
    }

    @Test
    public void testOptimizeWithInvalidChecker() {
        SimplePointValueChecker invalidChecker = new SimplePointValueChecker() {
            @Override
            public boolean isSatisfied(PointValuePair pointValuePair, double tolerance) {
                throw new RuntimeException("Invalid checker.");
            }
        };
        optimizer.setConvergenceChecker(invalidChecker);
        PointValuePair result = optimizer.optimize();
        // The test should not reach this point.
        assertTrue(false);
    }
}
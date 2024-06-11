package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GaussNewtonMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class GaussNewtonOptimizerTest {

    @Mock
    private RealMatrix weightMatrix;
    @Mock
    private RealVector startPoint;
    @Mock
    private ObjectiveFunction objectiveFunction;
    @Mock
    private LinearObjectiveFunction linearObjectiveFunction;
    @Mock
    private LinearConstraints constraints;
    @Mock
    private LinearConstraint constraint;

    private GaussNewtonOptimizer optimizer;
    private GaussNewtonMethod optimizerWrapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker());
        optimizerWrapper = new GaussNewtonMethod(optimizer);
        optimizer.setWeightMatrix(weightMatrix);
        optimizer.setStartPoint(startPoint);
        optimizer.setObjectiveFunction(objectiveFunction);

        // LinearObjectiveFunction is a wrapper for ObjectiveFunction
        when(objectiveFunction.getValue(any(RealVector.class)))
            .thenCallRealMethod("value", linearObjectiveFunction);
        when(linearObjectiveFunction.getValue(any(RealVector.class)))
            .thenCallRealMethod("value", anyDouble());
        when(linearObjectiveFunction.gradValue(any(RealVector.class)))
            .thenCallRealMethod("gradValue", any(RealVector.class), any(RealVector.class));

        // LinearConstraints is a wrapper for Constraints
        when(constraints.isSatisfied(any(RealVector.class)))
            .thenCallRealMethod("isSatisfied", any(RealVector.class), anyBoolean());
        when(constraint.isSatisfied(any(RealVector.class)))
            .thenCallRealMethod("isSatisfied", any(RealVector.class), anyBoolean());
        when(constraints.getConstraints())
            .thenReturn(new Object[] { constraint });

        optimizer.setConstraints(constraints);
    }

    @Test
    public void testOptimize() throws ConvergenceException, NullArgumentException {
        // Test with default settings.
        double[] targetValues = new double[] { 2.0, 3.0 };
        optimizer.setTarget(new ArrayRealVector(targetValues));
        double[] expected = new double[] { 1.0, 2.0 };
        optimizer.optimize();
        RealVector actual = optimizer.getStartPoint();
        assertArrayEquals(expected, actual.toArray());

        // Test with LU decomposition and custom convergence checker.
        optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(0.01));
        optimizer.setTarget(new ArrayRealVector(targetValues));
        optimizer.setWeightMatrix(weightMatrix);
        optimizer.setStartPoint(startPoint);
        optimizer.setObjectiveFunction(objectiveFunction);
        optimizer.optimize();
        actual = optimizer.getStartPoint();
        assertArrayEquals(expected, actual.toArray());

        // Test with QR decomposition.
        optimizer = new GaussNewtonOptimizer(false, new SimpleValueChecker());
        optimizer.setTarget(new ArrayRealVector(targetValues));
        optimizer.setWeightMatrix(weightMatrix);
        optimizer.setStartPoint(startPoint);
        optimizer.setObjectiveFunction(objectiveFunction);
        optimizer.optimize();
        actual = optimizer.getStartPoint();
        assertArrayEquals(expected, actual.toArray());

        // Test with constraints.
        double[] lowerBounds = new double[] { 0.0, 0.0 };
        double[] upperBounds = new double[] { 5.0, 5.0 };
        LinearConstraints linearConstraints = new LinearConstraints(
            new LinearObjectiveFunction(new ArrayRealVector(new double[] { 1.0, 1.0 }),
                new ArrayRealVector(new double[] { 2.0, 2.0 }), new GoalType(GoalType.MINIMIZE)));
        LinearConstraint constraint = new LinearConstraint(new ArrayRealVector(new double[] { 1.0, 1.0 }),
            new ArrayRealVector(new double[] { 3.0 }), Relationship.LEQ);
        linearConstraints.addConstraint(constraint);
        optimizer.setConstraints(linearConstraints);
        double[] expectedWithConstraints = new double[] { 1.0, 1.0 };
        optimizer.optimize();
        actual = optimizer.getStartPoint();
        assertArrayEquals(expectedWithConstraints, actual.toArray());

        // Test with error cases.
        optimizer.setTarget(null);
        try {
            optimizer.optimize();
            fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        optimizer.setStartPoint(null);
        try {
            optimizer.optimize();
            fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        optimizer.setWeightMatrix(null);
        try {
            optimizer.optimize();
            fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        optimizer.setObjectiveFunction(new UnboundedObjectiveFunction());
        try {
            optimizer.optimize();
            fail();
        } catch (ConvergenceException e) {
            // Expected.
        }

        optimizer.setConstraints(null);
        try {
            optimizer.optimize();
            fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        optimizer.setConstraints(new LinearConstraints(linearObjectiveFunction));
        optimizer.setStartPoint(new ArrayRealVector(new double[] { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY }));
        try {
            optimizer.optimize();
            fail();
        } catch (IllegalArgumentException e) {
            // Expected.
        }

        optimizer.setConstraints(new LinearConstraints(new LinearObjectiveFunction(
            new ArrayRealVector(new double[] { 1.0, 1.0 }), new ArrayRealVector(new double[] { 2.0, 2.0 }), new GoalType(GoalType.MINIMIZE))));
        optimizer.setStartPoint(new ArrayRealVector(new double[] { 1.0, 1.0 }));
        optimizer.optimize();
        actual = optimizer.getStartPoint();
        assertArrayEquals(expected, actual.toArray());
    }
}
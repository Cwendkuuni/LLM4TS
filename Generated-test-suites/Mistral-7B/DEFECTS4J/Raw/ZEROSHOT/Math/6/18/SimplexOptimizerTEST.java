package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet.ConstraintType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunctionSet.ObjectiveSense;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LUSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer(0.001, 0.0001);
    }

    /**
     * Test optimization with no constraints.
     */
    @Test
    public void testNoConstraints() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return FunctionUtils.pow(FunctionUtils.sum(point), 2);
            }
        };
        PointValuePair solution = optimizer.optimize(100, f, GoalType.MINIMIZE, new double[] {1.0});
        assertEquals(0.0, solution.getValue(), 0.001);
        assertArrayEquals(new double[] {0.0}, solution.getPoint(), 0.001);
    }

    /**
     * Test optimization with linear constraints.
     */
    @Test
    public void testConstraints() {
        MultivariateFunction f = new LinearObjectiveFunctionSet(new LinearObjectiveFunction[] {
            new LinearObjectiveFunction(new double[] {1.0, 1.0}, 2.0, ObjectiveSense.MINIMIZE)
        });
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(1.0, ConstraintType.LEQ, 1.0),
            new LinearConstraint(new double[] {1.0, 1.0}, ConstraintType.GEQ, new double[] {0.5, 0.5})
        };
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        PointValuePair solution = optimizer.optimize(100, f, GoalTypeWeightedSum.IDENTITY, new double[] {1.0, 1.0}, constraintSet);
        assertEquals(1.0 / Math.sqrt(2.0), solution.getValue(), 0.001);
        assertArrayEquals(new double[] {0.5, 0.5}, solution.getPoint(), 0.001);
    }

    /**
     * Test optimization with null arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullArguments() {
        optimizer.optimize(null, null, null, null);
    }
}
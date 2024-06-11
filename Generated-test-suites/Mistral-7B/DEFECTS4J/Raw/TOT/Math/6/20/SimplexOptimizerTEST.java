package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeNotFoundException;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleOptimizationData;
import org.apache.commons.math3.optim.nonlinear.vector.GoalTypeVector;
import org.apache.commons.math3.optim.nonlinear.vector.ObjectiveFunctionVector;
import org.apache.commons.math3.optim.nonlinear.vector.SimpleVectorOptimizationData;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.simple.AbstractSimplex;
import org.apache.commons.math3.optim.simple.SimplexPoint;
import org.apache.commons.math3.optim.simple.SimplexSimplex;
import org.apache.commons.math3.optim.simple. SimplexSimplex.SimplexType;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /** Objective function for testing. */
    private static final MultivariateFunction TEST_FUNCTION = new ObjectiveFunction() {
        @Override
        public double value(double[] x) {
            double sum = 0;
            for (int i = 0; i < x.length; i++) {
                sum += Math.pow(x[i] - 1, 2);
            }
            return sum;
        }

        @Override
        public MultivariateFunction gradient(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public MultivariateFunction hessian(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "Test Function";
        }
    };

    /** Simplex optimizer for testing. */
    private SimplexOptimizer optimizer;

    /** Simplex for testing. */
    private AbstractSimplex simplex;

    /** Start point for testing. */
    private double[] startPoint = new double[] {0.1, 0.2, 0.3};

    /**
     * Setup the test.
     */
    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer(new SimpleValueChecker());
        simplex = new SimplexSimplex(SimplexType.NELDER_MIDDLETON, 3);
    }

    /**
     * Test optimization with scalar goal type.
     */
    @Test
    public void testScalarOptimization() {
        optimizer.optimize(new MaxEval(100), TEST_FUNCTION, GoalType.MINIMIZE,
                           new double[] {0.1, 0.2, 0.3});
        PointValuePair result = optimizer.getOptimum();
        assertNotNull(result);
        assertEquals(0, result.getValue(), 0.001);
    }

    /**
     * Test optimization with vector goal type.
     */
    @Test
    public void testVectorOptimization() throws GoalTypeNotFoundException {
        optimizer.optimize(new MaxEval(100), new ObjectiveFunctionVector(TEST_FUNCTION),
                           GoalTypeVector.MINIMIZE_AXIS_BY_AXIS,
                           new SimpleVectorOptimizationData(new double[] {0.1, 0.2,
                                                                        0.3}));
        PointValuePairList result = optimizer.getOptima();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getValue()[0], 0.001);
        assertEquals(0, result.get(0).getValue()[1], 0.001);
        assertEquals(0, result.get(0).getValue()[2], 0.001);
    }

    /**
     * Test optimization with invalid goal type.
     */
    @Test(expected = GoalTypeNotFoundException.class)
    public void testInvalidGoalType() {
        optimizer.optimize(new MaxEval(100), TEST_FUNCTION, new GoalType() {
            @Override
            public String getName() {
                return "Invalid Goal Type";
            }
        }, new double[] {0.1, 0.2, 0.3});
    }

    /**
     * Test optimization with null optimization data.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullOptimizationData() {
        optimizer.optimize(new MaxEval(100), TEST_FUNCTION,
                           null, new double[] {0.1, 0.2, 0.3});
    }

    /**
     * Test optimization with null optimization data start point.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        optimizer.optimize(new MaxEval(100), TEST_FUNCTION, GoalType.MINIMIZE, null);
    }
}
package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SingularityException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleOptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AmplSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexPoint;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private AbstractSimplex simplex;
    private MultivariateFunction function;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        simplex = new AmplSimplex();
        function = new MultivariateFunction() {
            public double value(double[] x) {
                double sum = 0.0;
                for (double d : x) {
                    sum += Math.pow(d, 2);
                }
                return sum;
            }
        };
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
    }

    @Test
    public void testEmptyData() {
        try {
            optimizer.optimize(1, function, GoalType.MINIMIZE, new Object[0]);
            assertFalse(true);
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInitSimplex() {
        PointValuePair result = optimizer.optimize(1, function, GoalType.MINIMIZE, new Object[] {simpleX()});
        assertEquals(new PointValuePair(new double[] {0.0, 0.0}, 0.0), result);
    }

    @Test
    public void testOptimize() {
        PointValuePair result = optimizer.optimize(1, function, GoalType.MINIMIZE, new Object[] {simpleX(), simpleX()});
        assertNotNull(result);
        assertArrayEquals(new double[] {-1.0, -1.0}, result.getPoint(), 0.0);
    }

    @Test
    public void testOptimizeSingularPoint() {
        MultivariateFunction singularFunction = new MultivariateFunction() {
            public double value(double[] x) {
                if (FunctionUtils.isZero(x[0])) {
                    throw new SingularityException("Singular point reached");
                }
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }
        };

        try {
            optimizer.optimize(1, singularFunction, GoalType.MINIMIZE, new Object[] {simpleX(), simpleX()});
            fail("SingularityException expected");
        } catch (SingularityException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeConvergenceChecker() {
        SimpleValueChecker checker = new SimpleValueChecker(1e-8, 1e-8);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        PointValuePair result = optimizer.optimize(1, function, GoalType.MINIMIZE, new Object[] {simpleX(), simpleX(), simpleX()});
        assertNotNull(result);
        assertArrayEquals(new double[] {-1.0, -1.0}, result.getPoint(), 0.0);
    }

    @Test
    public void testOptimizeWithComplexGoalType() {
        try {
            optimizer.optimize(1, function, GoalType.MAXIMIZE, new Object[] {simpleX(), simpleX(), simpleX()});
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private Object simpleX() {
        return new SimpleOptimizationData(new SimplexPoint[] {new SimplexPoint(new double[] {0.0, 0.0}, 0.0)});
    }
}
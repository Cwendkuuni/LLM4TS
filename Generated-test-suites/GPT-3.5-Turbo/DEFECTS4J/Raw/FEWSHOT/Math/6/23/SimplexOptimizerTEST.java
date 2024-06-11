package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        AbstractSimplex simplex = new Simplex();
        simplex.createInitialSimplex(new double[]{1, 1, 1});
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertNotNull(result.getValue());
    }

    @Test
    public void testOptimize() {
        AbstractSimplex simplex = new Simplex();
        simplex.createInitialSimplex(new double[]{1, 1, 1});
        optimizer.setSimplex(simplex);

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 2 * point[0] + 3 * point[1] + 4 * point[2];
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;

        PointValuePair result = optimizer.optimize(1000, function, goalType);

        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertNotNull(result.getValue());
    }
}
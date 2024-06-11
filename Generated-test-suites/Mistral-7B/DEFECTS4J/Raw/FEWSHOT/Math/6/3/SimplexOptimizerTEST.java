package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.goaltypes.MinimizationGoalType;
import org.apache.commons.math3.optimization.multivariate.LinearConvergenceChecker;
import org.apache.commons.math3.optimization.multivariate.PointValuePair;
import org.apache.commons.math3.optimization.multivariate.Simplex;
import org.apache.commons.math3.optimization.multivariate.SimpleValueChecker;
import org.apache.commons.math3.optimization.multivariate.function.LinearFunction;

public class SimplexOptimizerTest {
    @Test
    public void testSimpleOptimization() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0001);
        OptimizationData data = new OptimizationData() {
            @Override
            public AbstractSimplex getSimplex() {
                Simplex simplex = new Simplex(3);
                simplex.addPoint(new double[]{1.0, 1.0, 1.0});
                simplex.addPoint(new double[]{2.0, 2.0, 2.0});
                simplex.addPoint(new double[]{3.0, 3.0, 3.0});
                simplex.addPoint(new double[]{0.0, 0.0, 0.0});
                return simplex;
            }
        };

        LinearFunction f = new LinearFunction(new double[]{-3.0, 2.0, 1.0});

        PointValuePair solution = optimizer.optimize(100, f, MinimizationGoalType.MINIMIZE, data);
        assertEquals(new double[]{0.0, 0.0, 0.0}, solution.getPoint(), 0.001);
        assertEquals(-3.0, solution.getValue(), 0.001);
    }

    @Test
    public void testConvergenceChecking() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0001);
        ConvergenceChecker<PointValuePair> checker = new LinearConvergenceChecker(0.9);
        optimizer.setConvergenceChecker(checker);

        Simplex simplex = new Simplex(3);
        simplex.addPoint(new double[]{1.0, 1.0, 1.0});
        simplex.addPoint(new double[]{2.0, 2.0, 2.0});
        simplex.addPoint(new double[]{3.0, 3.0, 3.0});
        simplex.addPoint(new double[]{0.0, 0.0, 0.0});

        LinearFunction f = new LinearFunction(new double[]{-3.0, 2.0, 1.0});

        PointValuePair[] previous = new PointValuePair[simplex.getSize()];
        for (int i = 0; i < simplex.getSize(); i++) {
            previous[i] = new PointValuePair(simplex.getPoint(i), i);
        }

        optimizer.optimizeInternal(1, f, MinimizationGoalType.MINIMIZE, new OptimizationData[]{simplex});

        assertTrue(checker.isConverged(1, previous[0], simplex.getPoint(0)));
        assertTrue(checker.isConverged(1, previous[1], simplex.getPoint(1)));
        assertTrue(checker.isConverged(1, previous[2], simplex.getPoint(2)));
        assertFalse(checker.isConverged(1, previous[3], simplex.getPoint(3)));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0001);
        optimizer.setSimplex(null);
        optimizer.optimize(1, new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }
        }, MinimizationGoalType.MINIMIZE, new OptimizationData[0]);
    }
}
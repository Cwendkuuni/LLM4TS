package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.precise.AbstractPoint;
import org.apache.commons.math3.optim.precise.Simplex;
import org.apache.commons.math3.optim.precise.SimplexIterator;
import org.apache.commons.math3.optim.precise.SimplexPoint;
import org.apache.commons.math3.optim.precise.SimplexSimplex;
import org.apache.commons.math3.optim.precise.StoppingCondition;
import org.apache.commons.math3.optim.precise.TestFunctions;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleOptimization() {
        // Test with a simple function
        MultivariateFunction function = TestFunctions.SIN;
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{1.0, 1.0};

        // Initialize simplex optimizer
        SimplexOptimizer optimizer = new SimplexOptimizer(0.0001, 1.0E-12);

        // Initialize optimization data
        Simplex simplex = new SimplexSimplex(new SimplexPoint[] {
            new SimplexPoint(new AbstractPoint[] {0.0, 0.0}, 0.0),
            new SimplexPoint(new AbstractPoint[] {1.0, 0.0}, 1.0),
            new SimplexPoint(new AbstractPoint[] {0.5, 0.5}, 0.5)
        });
        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public AbstractSimplex getSimplex() {
                return simplex;
            }
        };
        optimizer.parseOptimizationData(optimizationData);

        // Optimize
        PointValuePair result = optimizer.optimize(10, function, goalType, optimizationData);

        // Verify result
        SimplexIterator iter = simplex.iterator();
        PointValuePair expected = iter.next();
        assertEquals("Unexpected optimum found", expected, result);

        // Check convergence
        double[] currentPoint = simplex.getPoint(0);
        double[] previousPoint = simplex.getPoint(0);
        StoppingCondition stoppingCondition = new StoppingCondition() {
            @Override
            public boolean isSatisfied(int iteration, PointValuePair previous, PointValuePair current) {
                double error = FunctionUtils.distanceSquared(previous, current);
                return error < Math.pow(10, -12);
            }
        };
        optimizer.getConvergenceChecker().checkConvergence(iteration, previousPoint, currentPoint, stoppingCondition);
    }
}
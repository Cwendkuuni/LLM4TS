package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.point.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.simple.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.simple.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.simple.NelderMeadSimplexConfig;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer(GoalType.MINIMIZE);
        AbstractSimplex simplex = new NelderMeadSimplex(new NelderMeadSimplexConfig());

        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return FunctionUtils.fmin(x) * FunctionUtils.fmin(x) + 1.0;
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        };

        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public double[] getStartPoint() {
                return new double[]{2.0, 2.0};
            }

            @Override
            public AbstractSimplex getSimplex() {
                return simplex;
            }
        };

        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, optimizationData);
        assertNotNull(result);
        System.out.println("Optimization result: " + result);
        assertTrue(result.getValue() < 3.0);
    }
}
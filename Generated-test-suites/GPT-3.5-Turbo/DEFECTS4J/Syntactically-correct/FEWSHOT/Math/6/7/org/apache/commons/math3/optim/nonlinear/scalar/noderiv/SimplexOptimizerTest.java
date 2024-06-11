package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.NelderMeadSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.apache.commons.math3.optimization.direct.ContractorsStateData;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1] - 2 * x[0] * x[1];
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        InitialGuess guess = new InitialGuess(new double[]{1.0, 1.0});
        AbstractSimplex simplex = new NelderMeadSimplex(new double[]{0.1, 0.1});
        ContractorsStateData data = new ContractorsStateData(new double[]{0.1, 0.1});

        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, guess, simplex, data);
        double[] point = result.getPoint();
        double value = result.getValue();

        Assert.assertEquals(FastMath.sqrt(2), value, 1e-6);
        Assert.assertEquals(1, point[0], 1e-6);
        Assert.assertEquals(1, point[1], 1e-6);
    }
}
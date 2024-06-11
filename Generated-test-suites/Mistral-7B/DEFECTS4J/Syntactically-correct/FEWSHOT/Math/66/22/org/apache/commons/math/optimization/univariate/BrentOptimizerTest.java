package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.Value;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateRealFunction f = new Function();
        BrentOptimizer optimizer = new BrentOptimizer();

        PointValuePair result = optimizer.optimize(f, SimpleGoalType.MINIMIZE, -10.0, 10.0, 0.0);
        assertTrue(result != null);
        assertEquals(-2.0, result.getPoint(), 0.001);
        assertEquals(-2.44948974278317, result.getValue(), 0.001);

        result = optimizer.optimize(f, SimpleGoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        assertTrue(result != null);
        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(2.44948974278317, result.getValue(), 0.001);
    }

    public static class Function implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 5 * Math.pow(x, 2) + 6 * x + 3;
        }

        @Override
        public double derivative(double x) {
            return 3 * Math.pow(x, 2) - 10 * x + 6;
        }
    }
}
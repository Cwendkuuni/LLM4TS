package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.junit.Test;

public class PowellOptimizerTest {
    @Test
    public void testPowellOptimizerDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerInvalidRelativeThreshold() {
        new PowellOptimizer(-0.01, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerInvalidAbsoluteThreshold() {
        new PowellOptimizer(0.01, -0.001);
    }

    @Test
    public void testPowellOptimizerWithUserDefinedConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, new CustomConvergenceChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimize() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{1.0, 1.0});

        assertEquals(0.0, result.getPoint()[0], 0.001);
        assertEquals(0.0, result.getPoint()[1], 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(-x[0] * x[0]) - x[1] * x[1];
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, new CustomConvergenceChecker());
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{2.0, 2.0});

        assertEquals(-1.0, result.getPoint()[0], 0.001);
        assertEquals(1.0, result.getPoint()[1], 0.001);
        assertEquals(-3.141592653589793, result.getValue(), 0.001);
    }

    @Test
    public void testLineSearch() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        PowellOptimizer.LineSearch lineSearch = new PowellOptimizer.LineSearch(0.01, 0.001);
        UnivariatePointValuePair result = lineSearch.optimize(function, GoalType.MINIMIZE, 1.0, 3.0);

        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            double delta = current.getValue() - previous.getValue();
            return Math.abs(delta) < 0.001;
        }
    }
}
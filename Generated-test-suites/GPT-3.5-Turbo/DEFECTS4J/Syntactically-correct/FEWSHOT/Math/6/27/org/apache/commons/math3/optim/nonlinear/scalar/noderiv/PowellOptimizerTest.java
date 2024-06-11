package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class PowellOptimizerTest {

    private PowellOptimizer optimizer;

    private static final double RELATIVE_THRESHOLD = 1e-6;
    private static final double ABSOLUTE_THRESHOLD = 1e-12;

    private final MultivariateFunction function = new MultivariateFunction() {
        public double value(double[] variables) {
            // Define your test function here
            double x = variables[0];
            double y = variables[1];
            return (x - 2) * (x - 2) + (y + 3) * (y + 3);
        }
    };

    @Test
    public void testOptimizeMinimize() {
        optimizer = new PowellOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD,
                new ConvergenceChecker<PointValuePair>() {
                    public boolean converged(int iteration, PointValuePair previous,
                            PointValuePair current) {
                        double prevObj = previous.getValue();
                        double currObj = current.getValue();
                        return Math.abs(prevObj - currObj) < ABSOLUTE_THRESHOLD;
                    }
                });

        double[] startPoint = { 0, 0 };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, startPoint);
        double[] expectedPoint = { 2, -3 };
        assertEquals(expectedPoint[0], result.getPoint()[0], ABSOLUTE_THRESHOLD);
        assertEquals(expectedPoint[1], result.getPoint()[1], ABSOLUTE_THRESHOLD);
    }

    @Test
    public void testOptimizeMaximize() {
        optimizer = new PowellOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD,
                new ConvergenceChecker<PointValuePair>() {
                    public boolean converged(int iteration, PointValuePair previous,
                            PointValuePair current) {
                        double prevObj = previous.getValue();
                        double currObj = current.getValue();
                        return Math.abs(prevObj - currObj) < ABSOLUTE_THRESHOLD;
                    }
                });

        double[] startPoint = { 0, 0 };
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, startPoint);
        double[] expectedPoint = { 2, -3 };
        assertEquals(expectedPoint[0], result.getPoint()[0], ABSOLUTE_THRESHOLD);
        assertEquals(expectedPoint[1], result.getPoint()[1], ABSOLUTE_THRESHOLD);
    }

}
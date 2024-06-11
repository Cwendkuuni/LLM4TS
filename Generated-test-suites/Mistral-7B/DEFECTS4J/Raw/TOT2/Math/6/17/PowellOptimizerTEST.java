package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonConvergentIterationException;
import org.apache.commons.math3.optim.NoBracketingException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class PowellOptimizerTest {

    @Test
    public void testMinimization() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(f, GoalType.MINIMIZE, new double[] {2, 2});

        assertArrayEquals(new double[] {0, 0}, solution.getPoint(), 1e-6);
        assertEquals(0, solution.getValue(), 1e-6);
    }

    @Test
    public void testMaximization() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return -(x[0] * x[0] + x[1] * x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(f, GoalType.MAXIMIZE, new double[] {-2, -2});;

        assertArrayEquals(new double[] {0, 0}, solution.getPoint(), 1e-6);
        assertEquals(-0, solution.getValue(), 1e-6);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNegativeAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, -1e-6);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testZeroRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0, 1e-6);
    }

    @Test
    public void testConvergenceChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new MyConvergenceChecker());

        try {
            optimizer.optimize(f, GoalType.MINIMIZE, new double[] {1, 1});
            fail();
        } catch (AssertionError e) {
            // success
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
            return current.getValue() > previous.getValue() || current.getValue() < previous.getValue();
        }
    }

    @Test (expected = NoBracketingException.class)
    public void testNoBracketingException() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, null);
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, new double[] {2, 2});
        } catch (NoBracketingException e) {
            // success
        }
    }

    @Test (expected = NonConvergentIterationException.class)
    public void testNonConvergentIterationException() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, null);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, new double[] {2, 2});
        fail("Should have thrown NonConvergentIterationException");
    }

    @Test
    public void testMaxEvalException() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, null);
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, new double[] {2, 2}, new MaxEval(100000));
            fail();
        } catch (AssertionError e) {
            // success
        }
    }

    @Test
    public void testCustomGoalType() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, null);
        PointValuePair result = optimizer.optimize(f, new CustomGoalType(), new double[] {2, 2});
        assertArrayEquals(new double[] {0, 0}, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }

    private static class CustomGoalType implements GoalType {
        @Override
        public String toString() {
            return "CUSTOM";
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof CustomGoalType) {
                return true;
            }
            return false;
        }

        @Override
        public int compareTo(GoalType o) {
            return 0;
        }
    }
}
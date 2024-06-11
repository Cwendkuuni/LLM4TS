package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MinEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonUnivariateOptimizer;
import org.apache.commons.math3.optim.PointValues;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    @Test
    public void testConstructor() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);
        assertNotNull(powellOptimizer);
    }

    @Test
    public void testOptimizeWithMinimizationGoalType() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);
        double[] initialPoint = new double[] {1.0, 2.0};
        double[] expectedResult = new double[] {-2.0, -3.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + 4 * x[1] * x[1];
            }
        };

        PointValuePair result = powellOptimizer.optimize(function, GoalType.MINIMIZE, initialPoint);
        assertEquals(GoalType.MINIMIZE, powellOptimizer.getGoalType());
        assertArrayEquals(expectedResult, result.getPoint(), 0.0001);
    }

    @Test
    public void testOptimizeWithMaximizationGoalType() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);
        double[] initialPoint = new double[] {1.0, 2.0};
        double[] expectedResult = new double[] {3.0, 2.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return -(x[0] * x[0] + 4 * x[1] * x[1]);
            }
        };

        PointValuePair result = powellOptimizer.optimize(function, GoalType.MAXIMIZE, initialPoint);
        assertEquals(GoalType.MAXIMIZE, powellOptimizer.getGoalType());
        assertArrayEquals(expectedResult, result.getPoint(), 0.0001);
    }

    @Test
    public void testOptimizeWithNullFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        assertThrows(IllegalArgumentException.class, () -> powellOptimizer.optimize(null, GoalType.MINIMIZE, new double[0]));
    }

    @Test
    public void testOptimizeWithNullInitialPoint() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        assertThrows(IllegalArgumentException.class, () -> powellOptimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0;
            }
        }, GoalType.MINIMIZE, null));
    }

    @Test
    public void testOptimizeWithUnboundedFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);
        double[] initialPoint = new double[] {1.0, 2.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return 1.0 / (Math.sqrt(x[0]) - Math.sqrt(x[1]));
            }
        };

        assertThrows(ArithmeticException.class, () -> powellOptimizer.optimize(function, GoalType.MINIMIZE, initialPoint));
    }

    @Test
    public void testOptimizeWithConvergenceChecker() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001, new MyConvergenceChecker());
        double[] initialPoint = new double[] {1.0, 2.0};
        double[] expectedResult = new double[] {-1.0, -1.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        PointValuePair result = powellOptimizer.optimize(function, GoalType.MINIMIZE, initialPoint);
        assertEquals(GoalType.MINIMIZE, powellOptimizer.getGoalType());
        assertArrayEquals(expectedResult, result.getPoint(), 0.0001);
    }

    @Test
    public void testOptimizeWithCustomLineSearchTolerances() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001, 1e-5, 1e-6);
        double[] initialPoint = new double[] {1.0, 2.0};
        double[] expectedResult = new double[] {-2.0, -3.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + 4 * x[1] * x[1];
            }
        };

        PointValuePair result = powellOptimizer.optimize(function, GoalType.MINIMIZE, initialPoint);
        assertEquals(GoalType.MINIMIZE, powellOptimizer.getGoalType());
        assertArrayEquals(expectedResult, result.getPoint(), 0.0001);
    }

    @Test
    public void testOptimizeWithNullConvergenceChecker() {
        assertThrows(NullPointerException.class, () -> new PowellOptimizer(0.01, 0.001, null));
    }

    @Test
    public void testOptimizeWithInvalidGoalType() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);
        double[] initialPoint = new double[] {1.0, 2.0};

        assertThrows(IllegalArgumentException.class, () -> powellOptimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0;
            }
        }, GoalType.NEUTRAL, initialPoint));
    }

    @Test
    public void testOptimizeWithNonMultivariateFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        assertThrows(ClassCastException.class, () -> powellOptimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, new double[0]));
    }

    @Test
    public void testOptimizeWithZeroSizeInitialPoint() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        assertThrows(IllegalArgumentException.class, () -> powellOptimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }
        }, GoalType.MINIMIZE, new double[0]));
    }

    @Test
    public void testOptimizeWithMaximumNumberOfEvaluations() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);
        double[] initialPoint = new double[] {1.0, 2.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        assertThrows(MaxEval.class, () -> powellOptimizer.optimize(function, GoalType.MINIMIZE, initialPoint, new MaxEval(1)));
    }

    @Test
    public void testOptimizeWithPointValues() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);
        double[] initialPoint = new double[] {1.0, 2.0};
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PointValues pointValues = new PointValues(new double[][]{{1.0, 2.0}, {1.5, 2.5}}, new double[]{3.0, 4.0});

        PointValuePair result = powellOptimizer.optimize(function, GoalType.MINIMIZE, initialPoint, pointValues);
        assertEquals(GoalType.MINIMIZE, powellOptimizer.getGoalType());
        assertArrayEquals(new double[]{1.5, 2.5}, result.getPoint(), 0.0001);
    }

    @Test
    public void testOptimizeWithNonUnivariateFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);
        double[] initialPoint = new double[] {1.0, 2.0};
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public PointValuePair value(PointValuePair point) {
                double[] pointValue = point.getPoint();
                return new PointValuePair(pointValue, pointValue[0] * pointValue[0] + pointValue[1] * pointValue[1]);
            }
        };

        assertThrows(ClassCastException.class, () -> powellOptimizer.optimize(function, GoalType.MINIMIZE, initialPoint));
    }

    /**
     * Custom convergence checker.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double delta = Math.abs(current.getValue() - previous.getValue());
            return delta < 1e-6;
        }
    }
}
package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimizeSimpleFunction() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerMinimizeAbsoluteThresholdNegative() {
        new BrentOptimizer(-1e-6, 1e-6, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerMinimizeRelativeThresholdTooSmall() {
        new BrentOptimizer(1e-12, 1e-6, null);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithLocalMinimum() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.pow(x - 2, 2);
                } else {
                    return Math.pow(x + 2, 2);
                }
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(-2.0, result.getPoint(), 1e-6);
        assertEquals(4.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithGlobalMinimumAtEdge() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 3), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(3.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithGlobalMinimumAtStart() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x + 3), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(-3.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithGlobalMinimumAtEnd() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 7), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(7.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithGlobalMinimumOutsideBounds() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x < -5) {
                    return Math.pow(x + 5, 2);
                } else {
                    return Math
                        .pow(Math.abs(x - 2), 2);
                }
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(-5.0, result.getPoint(), 1e-6);
        assertEquals(25.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithMultipleLocalMinima() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.pow(x - 2, 2);
                } else {
                    return Math.pow(x + 2, 2);
                }
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -5, 5);

        // Check result
        assertEquals(-2.0, result.getPoint(), 1e-6);
        assertEquals(4.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizeSimpleFunction() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.cos(x), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithLocalMaximum() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.pow(x - 2, 2);
                } else {
                    return Math.pow(x + 2, 2);
                }
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(4.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithGlobalMaximumAtEdge() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 3), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(3.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithGlobalMaximumAtStart() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x + 3), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(3.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithGlobalMaximumAtEnd() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 7), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(7.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithGlobalMaximumOutsideBounds() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x < -5) {
                    return Math.pow(x + 5, 2);
                } else {
                    return Math.pow(Math.abs(x - 2), 2);
                }
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -10, 10);

        // Check result
        assertEquals(-5.0, result.getPoint(), 1e-6);
        assertEquals(25.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithMultipleLocalMinima() {
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.pow(x - 2, 2);
                } else {
                    return Math.pow(x + 2, 2);
                }
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func, -5, 5);

        // Check result
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(4.0, result.getValue(), 1e-6);
    }
}
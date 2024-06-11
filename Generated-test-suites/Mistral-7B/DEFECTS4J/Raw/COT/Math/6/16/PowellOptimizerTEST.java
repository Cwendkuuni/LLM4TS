package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFissionFunction;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriterion;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.ValueChecker;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    private PowellOptimizer optimizer;
    private double[] startPoint;
    private UnivariateFunction objectiveFunction;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        optimizer = new PowellOptimizer(1e-6, 1e-6);
        startPoint = new double[] {1.0, 2.0};
        objectiveFunction = new UnivariateFissionFunction(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2.0) + FastMath.pow(x - 1.0, 2.0);
            }
        });
    }

    @Test
    public void testConstructor() {
        optimizer = new PowellOptimizer(1e-6, 1e-6, null);
        assertNotNull(optimizer);

        optimizer = new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-8, 1e-8));
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeThreshold() {
        new PowellOptimizer(-1e-6, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeThreshold() {
        new PowellOptimizer(MIN_RELATIVE_TOLERANCE, 1e-6);
    }

    @Test
    public void testMinimizerFunctionality() {
        PointValuePair result = optimizer.optimize(startPoint, objectiveFunction, GoalType.MINIMIZE);
        double expectedX[] = new double[]{1.0, 1.0};
        double expectedF[] = new double[]{1.0, 2.0};
        assertArrayEquals(expectedX, result.getPoint(), 0.0);
        assertEquals(expectedF[0], result.getValue(), 0.0);
    }

    @Test
    public void testMaximizerFunctionality() {
        optimizer = new PowellOptimizer(1e-6, 1e-6, GoalTypeMaximizer.INSTANCE);
        PointValuePair result = optimizer.optimize(startPoint, objectiveFunction, GoalType.MAXIMIZE);
        double expectedX[] = new double[]{1.0, 2.0};
        double expectedF[] = new double[]{3.0, 3.0};
        assertArrayEquals(expectedX, result.getPoint(), 0.0);
        assertEquals(expectedF[0], result.getValue(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(startPoint, objectiveFunction, new GoalTypeMinimizer() {
            @Override
            public GoalType getGoalType() {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test
    public void testEdgeCases() {
        PointValuePair result;

        // Test objective function that always returns zero
        optimizer = new PowellOptimizer(1e-6, 1e-6);
        objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        result = optimizer.optimize(new double[] {0.0}, objectiveFunction, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.0);

        // Test objective function that always returns large value
        objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Double.MAX_VALUE;
            }
        };
        result = optimizer.optimize(new double[] {0.0}, objectiveFunction, GoalType.MINIMIZE);
        assertNull(result);

        // Test objective function with a local minimum and a global minimum
        optimizer = new PowellOptimizer(1e-6, 1e-6);
        objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0.5) {
                    return Math.pow(x, 2.0);
                } else {
                    return Math.pow(x - 2.0, 2.0);
                }
            }
        };
        PointValuePair localMinimum = optimizer.optimize(new double[] {0.0}, objectiveFunction, GoalType.MINIMIZE);
        assertEquals(0.25, localMinimum.getValue(), 0.0);
        PointValuePair globalMinimum = optimizer.optimize(new double[] {3.0}, objectiveFunction, GoalType.MINIMIZE);
        assertEquals(2.0, globalMinimum.getValue(), 0.0);

        // Test objective function with multiple local minima
        optimizer = new PowellOptimizer(1e-6, 1e-6);
        objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 1.0) {
                    return Math.pow(x, 2.0);
                } else {
                    return Math.pow(x - 1.0, 2.0);
                }
            }
        };
        result = optimizer.optimize(new double[] {0.0}, objectiveFunction, GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue(), 0.0);
        result = optimizer.optimize(new double[] {1.5}, objectiveFunction, GoalType.MINIMIZE);
        assertEquals(1.0, result.getValue(), 0.0);

        // Test objective function with multiple local maxima
        objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 1.0) {
                    return -Math.pow(x, 2.0);
                } else {
                    return -Math.pow(x - 1.0, 2.0);
                }
            }
        };
        result = optimizer.optimize(new double[] {0.0}, objectiveFunction, GoalType.MAXIMIZE);
        assertEquals(0.0, result.getValue(), 0.0);
        result = optimizer.optimize(new double[] {1.5}, objectiveFunction, GoalType.MAXIMIZE);
        assertEquals(1.0, result.getValue(), 0.0);

        // Test objective function with a singular point
        objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) * Math.sin(FastMath.toRadians(Math.PI / 4.0));
            }
        };
        result = optimizer.optimize(new double[] {0.0}, objectiveFunction, GoalType.MINIMIZE);
        assertNull(result);
    }

    @Test
    public void testExceptionHandling() {
        // Test exception handling with invalid goal type
        optimizer = new PowellOptimizer(1e-6, 1e-6);
        try {
            optimizer.optimize(startPoint, objectiveFunction, new GoalType() {
                @Override
                public GoalType getGoalType() {
                    throw new UnsupportedOperationException();
                }
            });
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test exception handling with non-univariate objective function
        UnivariateOptimizer nonUnivariateOptimizer = new PowellOptimizer(1e-6, 1e-6);
        MultivariateFunction multivariateObjectiveFunction = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        };
        try {
            nonUnivariateOptimizer.optimize(startPoint, multivariateObjectiveFunction, GoalType.MINIMIZE);
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test exception handling with non-GoalTypeCriterion convergence checker
        ValueChecker nonGoalTypeCriterionChecker = new ValueChecker() {
            @Override
            public boolean isSatisfied(UnivariatePointValuePair pointValuePair, int iteration, UnivariatePointValuePair previousPointValuePair) {
                throw new UnsupportedOperationException();
            }
        };
        try {
            optimizer = new PowellOptimizer(1e-6, 1e-6, new ConvergenceChecker<PointValuePair>() {
                @Override
                public ConvergenceChecker<PointValuePair> setMaxIterations(int maxIterations) {
                    return this;
                }

                @Override
                public ConvergenceChecker<PointValuePair> setValueChecker(ValueChecker valueChecker) {
                    return this;
                }

                @Override
                public ConvergenceChecker<PointValuePair> setGoalTypeChecker(GoalTypeCriterion goalTypeCriterion) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean isSatisfied(PointValuePair pointValuePair, int iteration, PointValuePair previousPointValuePair) {
                    return nonGoalTypeCriterionChecker.isSatisfied(pointValuePair, iteration, previousPointValuePair);
                }
            });
            optimizer.optimize(startPoint, objectiveFunction, GoalType.MINIMIZE);
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}
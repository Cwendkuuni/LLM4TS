package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.simplextools.AmplSimplex;
import org.apache.commons.math3.optim.nonlinear.simplextools.AmplSimplexLimitException;
import org.apache.commons.math3.optim.nonlinear.simplextools.SimplexConstraint;
import org.apache.commons.math3.optim.nonlinear.simplextools.SimplexMethod;
import org.apache.commons.math3.optim.nonlinear.simplextools.SimplexPoint;
import org.junit.Test;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Test optimization of a simple function.
     */
    @Test
    public void testSimpleFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {3.0, 3.0};
        OptimizationData data = new OptimizationData() {
            {
                setInitialGuess(new InitialGuess() {
                    public double[] getInitialPoint() {
                        return startPoint;
                    }
                });
                setSimplex(new AmplSimplex(3, new SimplexMethod(), new SimplexConstraint[0], 500, 1.0E-6, 1.0E-6));
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        PointValuePair result = optimizer.optimize(500, f, goalType, data);
        SimplexPoint expected = new SimplexPoint(new double[]{1.0, 1.0}, 1.0E-6);
        assertEquals(expected.getPoint(), result.getPoint(), 1.0E-6);
        assertEquals(expected.getValue(), result.getValue(), 1.0E-6);
    }

    /**
     * Test optimization of a function with a constraint.
     */
    @Test(expected = NullArgumentException.class)
    public void testFunctionWithConstraint() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                if (x[0] <= 0.0) {
                    throw new IllegalArgumentException();
                }
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {-1.0, 3.0};
        OptimizationData data = new OptimizationData() {
            {
                setInitialGuess(new InitialGuess() {
                    public double[] getInitialPoint() {
                        return startPoint;
                    }
                });
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        optimizer.optimize(500, f, goalType, data);
    }

    /**
     * Test optimization of a function with a null objective function.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullObjectiveFunction() {
        MultivariateFunction f = null;
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {3.0, 3.0};
        OptimizationData data = new OptimizationData() {
            {
                setInitialGuess(new InitialGuess() {
                    public double[] getInitialPoint() {
                        return startPoint;
                    }
                });
                setSimplex(new AmplSimplex(3, new SimplexMethod(), new SimplexConstraint[0], 500, 1.0E-6, 1.0E-6));
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        optimizer.optimize(500, f, goalType, data);
    }

    /**
     * Test optimization of a function with a null convergence checker.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {3.0, 3.0};
        OptimizationData data = new OptimizationData() {
            {
                setInitialGuess(new InitialGuess() {
                    public double[] getInitialPoint() {
                        return startPoint;
                    }
                });
                setSimplex(new AmplSimplex(3, new SimplexMethod(), new SimplexConstraint[0], 500, 1.0E-6, 1.0E-6));
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(null);
        optimizer.optimize(500, f, goalType, data);
    }

    /**
     * Test optimization of a function with a null optimization data.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullOptimizationData() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {3.0, 3.0};
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        optimizer.optimize(500, f, goalType, null);
    }

    /**
     * Test optimization of a function with the maximum number of evaluations
     * reached.
     */
    @Test
    public void testMaxEvaluationsReached() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {3.0, 3.0};
        OptimizationData data = new OptimizationData() {
            {
                setInitialGuess(new InitialGuess() {
                    public double[] getInitialPoint() {
                        return startPoint;
                    }
                });
                setSimplex(new AmplSimplex(3, new SimplexMethod(), new SimplexConstraint[0], 1, 1.0E-6, 1.0E-6));
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        PointValuePair result = optimizer.optimize(1, f, goalType, data);
        assertEquals(Double.NaN, result.getValue(), 1.0E-6);
    }

    /**
     * Test optimization of a function that does not converge.
     */
    @Test
    public void testFunctionThatDoesNotConverge() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.abs(Math.sin(x[0]) + Math.sin(x[1]));
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {0.0, 0.0};
        OptimizationData data = new OptimizationData() {
            {
                setInitialGuess(new InitialGuess() {
                    public double[] getInitialPoint() {
                        return startPoint;
                    }
                });
                setSimplex(new AmplSimplex(3, new SimplexMethod(), new SimplexConstraint[0], 1000, 1.0E-6, 1.0E-6));
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        PointValuePair result = optimizer.optimize(1000, f, goalType, data);
        assertEquals(Double.NaN, result.getValue(), 1.0E-6);
    }

    /**
     * Test optimization of a function with a limit on the number of iterations.
     */
    @Test
    public void testFunctionWithLimitOnIterations() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 2.0, 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {3.0, 3.0};
        OptimizationData data = new OptimizationData() {
            {
                setInitialGuess(new InitialGuess() {
                    public double[] getInitialPoint() {
                        return startPoint;
                    }
                });
                setSimplex(new AmplSimplex(3, new SimplexMethod(), new SimplexConstraint[0], 5, 1.0E-6, 1.0E-6));
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        PointValuePair result = optimizer.optimize(5, f, goalType, data);
        SimplexPoint expected = new SimplexPoint(new double[]{1.0, 1.0}, 1.0E-6);
        assertEquals(expected.getPoint(), result.getPoint(), 1.0E-6);
        assertEquals(expected.getValue(), result.getValue(), 1.0E-6);
    }
}
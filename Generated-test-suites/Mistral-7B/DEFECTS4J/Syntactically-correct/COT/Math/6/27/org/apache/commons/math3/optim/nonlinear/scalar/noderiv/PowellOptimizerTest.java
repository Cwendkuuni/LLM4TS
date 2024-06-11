package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.apache.commons.math3.test.utils.TestArrays;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PowellOptimizerTest extends AbstractMathTestCase {

    /**
     * Test the PowellOptimizer constructor with default values.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(2.0e-6, 1.0e-6);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNotNull(optimizer.getConvergenceChecker());
        assertEquals(2.0e-6, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(1.0e-6, optimizer.getAbsoluteThreshold(), 1e-9);
    }

    /**
     * Test the PowellOptimizer constructor with custom convergence checker.
     */
    @Test
    public void testCustomConstructor() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return previous.getValue() - current.getValue() < 1e-8;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6, convergenceChecker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNotNull(optimizer.getConvergenceChecker());
        assertEquals(1.0e-6, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(1.0e-6, optimizer.getAbsoluteThreshold(), 1e-9);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test the PowellOptimizer optimize method with default values.
     */
    @Test
    public void testOptimizeDefault() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(2.0e-6, 1.0e-6);
        final double[] x = new double[]{1.0, 1.0};

        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, x);
        assertEquals(0.0, result.getValue(), 1e-9);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-9);
    }

    /**
     * Test the PowellOptimizer optimize method with custom convergence checker.
     */
    @Test
    public void testOptimizeCustom() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return previous.getValue() - current.getValue() < 1e-8;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6, convergenceChecker);
        final double[] x = new double[]{1.0, 1.0};

        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, x);
        assertEquals(0.0, result.getValue(), 1e-9);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-9);
    }

    /**
     * Test the PowellOptimizer optimize method with function that has multiple
     * local minima.
     */
    @Test
    public void testOptimizeMultipleMinima() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] * Math.exp(-x[1]);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6);
        final double[] x = new double[]{2.0, 2.0};

        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, x);
        double minValue = result.getValue();
        assertArrayEquals(new double[]{0.5, 0.5}, result.getPoint(), 1e-9);

        // Check that there is another minimum at (-2, -2)
        PointValuePair result2 = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{2.0, 2.0});
        double minValue2 = result2.getValue();
        assertArrayEquals(new double[]{-2.0, -2.0}, result2.getPoint(), 1e-9);

        // Check that the minima are indeed minima
        assertEquals(minValue, minValue2, 1e-9);
    }

    /**
     * Test the PowellOptimizer optimize method with non-differentiable function.
     */
    @Test
    public void testOptimizeNonDifferentiable() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                if (x[0] > 0 && x[1] > 0 && x[0] + x[1] <= 1) {
                    return Math.log(Math.sin(Math.PI * x[0] * x[1]));
                } else {
                    throw new IllegalArgumentException("Invalid input");
                }
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6);
        final double[] x = new double[]{0.5, 0.5};

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, x);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the PowellOptimizer optimize method with line search exceptions.
     */
    @Test(expected = org.apache.commons.math3.exception.GoalTypeMismatchException.class)
    public void testOptimizeGoalTypeMismatch() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6);
        final double[] x = new double[]{1.0, 1.0};

        optimizer.optimize(function, GoalType.MAXIMIZE, x);
    }

    /**
     * Test the PowellOptimizer optimize method with convergence checker exceptions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeIllegalArgumentException() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                throw new IllegalArgumentException("Not a valid checker");
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6, convergenceChecker);
        final double[] x = new double[]{1.0, 1.0};

        optimizer.optimize(function, GoalType.MINIMIZE, x);
    }
}
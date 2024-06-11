package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * PowellOptimizerTest.
 */
public class PowellOptimizerTest {

    

    @Test
    public void testPowellOptimizerConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerConstructorNegativeAbsThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, -1.0e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerConstructorRelThresholdTooSmall() {
        PowellOptimizer optimizer = new PowellOptimizer(0.5 * FastMath.ulp(1d), 1.0e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerConstructorNullConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6, null);
    }

    @Test
    public void testDoOptimize() {
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
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{2.0, 2.0});
        assertEquals(new PointValuePair(new double[]{0.0, 0.0}, 0.0), result);
    }

    @Test
    public void testDoOptimizeWithCustomConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(-Math.pow(Math.sqrt(x[0] * x[0] + x[1] * x[1]), 2.0));
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return previous.getValue() < current.getValue() && iter > 10;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6, customChecker);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{3.0, 3.0});
        assertTrue(result.getValue() < 0.1); // The function has a global minimum at (0, 0), but the test won't find it with the given custom convergence checker.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeNullFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0e-6, 1.0e-6);
        optimizer.optimize(null, GoalType.MINIMIZE, new double[]{0.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeNullStartPoint() {
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
        optimizer.optimize(function, GoalType.MINIMIZE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeInvalidGoalType() {
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
        optimizer.optimize(function, GoalType.MAXIMIZE, new double[]{0.0});
    }

    @Test
    public void testNewPointAndDirection() {
        double[] p = new double[]{1.0, 2.0};
        double[] d = new double[]{1.0, 1.0};
        double optimum = 3.0;
        double[][] result = PowellOptimizer.this.newPointAndDirection(p, d, optimum);
        assertEquals(new double[]{4.0, 3.0}, result[0]);
        assertEquals(new double[]{1.0, 1.0}, result[1]);
    }

    @Test
    public void testLineSearch() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0 * x + 4.0;
            }
        };

        double[] p = new double[]{2.0};
        double[] d = new double[]{1.0};
        LineSearch lineSearch = new PowellOptimizer.LineSearch(1.0e-6, 1.0e-6);
        UnivariatePointValuePair result = lineSearch.search(p, d);
        assertEquals(1.5, result.getPoint(), 0.001);
        assertEquals(-1.5, result.getValue(), 0.001);
    }

    
}
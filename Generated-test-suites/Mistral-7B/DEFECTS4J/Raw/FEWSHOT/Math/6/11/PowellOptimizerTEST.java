package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test constructor with default parameters and no convergence checker.
     */
    @Test
    public void testDefaultConstructor() {
        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        assertNotNull(optimizer);
        assertEquals(0.01, optimizer.getRelativeThreshold(), Precision.SAFE_EQ);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), Precision.SAFE_EQ);
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with specified convergence checker.
     * @throws NotStrictlyPositiveException if the absolute or relative
     * threshold is non-positive.
     */
    @Test
    public void testCustomConvergenceChecker() throws NotStrictlyPositiveException {
        final ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return previous.getValue() > current.getValue() && iter > 10;
            }
        };

        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        assertNotNull(optimizer);
        assertEquals(0.01, optimizer.getRelativeThreshold(), Precision.SAFE_EQ);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), Precision.SAFE_EQ);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization of a simple multivariate function.
     */
    @Test
    public void testOptimize() {
        final MultivariateFunction function = new MultivariateFunction() {
                @Override
                public double value(double[] x) {
                    final double sum = 0;
                    for (int i = 0; i < x.length; i++) {
                        sum += x[i] * x[i];
                    }
                    return sum;
                }
            };

        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        final PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[] {1.0, 1.0});
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(1.0 / Math.sqrt(2.0), result.getPoint()[0], Precision.SAFE_EQ);
        assertEquals(1.0 / Math.sqrt(2.0), result.getPoint()[1], Precision.SAFE_EQ);
        assertEquals(0.0, result.getValue(), Precision.SAFE_EQ);
    }

    /**
     * Test optimization of a multivariate function with a custom convergence
     * checker.
     */
    @Test
    public void testCustomConvergenceCheckerOptimize() {
        final ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return previous.getValue() > current.getValue() && iter > 10;
            }
        };

        final MultivariateFunction function = new MultivariateFunction() {
                @Override
                public double value(double[] x) {
                    final double sum = 0;
                    for (int i = 0; i < x.length; i++) {
                        sum += Math.pow(x[i], 2);
                    }
                    return sum;
                }
            };

        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        final PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[] {1.0, 1.0});
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(new double[] {0.0, 0.0}, result.getPoint(), Precision.SAFE_EQ);
        assertEquals(0.0, result.getValue(), Precision.SAFE_EQ);
        assertTrue(iterationsReached(optimizer, 11));
    }

    /**
     * Test optimization of a multivariate function that is not monotonous.
     */
    @Test
    public void testNonMonotonousFunction() {
        final MultivariateFunction function = new MultivariateFunction() {
                @Override
                public double value(double[] x) {
                    final double sum = 0;
                    for (int i = 0; i < x.length; i++) {
                        sum += Math.signum(x[i]) * Math.pow(Math.abs(x[i]), 2);
                    }
                    return sum;
                }
            };

        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        final PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[] {1.0, 1.0});
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertFalse(Arrays.equals(new double[] {0.0, 0.0}, result.getPoint()));
        assertFalse(Precision.isClose(0.0, result.getValue()));
        assertTrue(iterationsReached(optimizer, 100));
    }

    private boolean iterationsReached(MultivariateOptimizer optimizer, int iterations) {
        final int currentIterations = optimizer.getIterations();
        return currentIterations >= iterations;
    }
}
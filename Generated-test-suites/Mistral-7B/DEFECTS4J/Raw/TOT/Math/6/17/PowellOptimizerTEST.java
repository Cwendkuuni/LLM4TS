package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test function for Powell optimization.
     */
    private static final UnivariateFunction TEST_FUNCTION = new PointValueFunction() {
        @Override
        public double value(double x) {
            return Math.sin(x) * Math.exp(-x);
        }

        @Override
        public double derivative(double x) {
            return Math.sin(x) * (1 + Math.cos(x)) * Math.exp(-x) - Math.cos(x) * Math.exp(-x);
        }
    };

    /**
     * Test optimization with default values.
     */
    @Test
    public void testDefaultOptimization() {
        final double[] startPoint = new double[] {1.0, 1.0};
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        final PointValuePair result = optimizer.optimize(startPoint, TEST_FUNCTION, GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue(), 1e-9);
        assertArrayEquals(new double[] {-1.19646465, -1.19646465}, result.getPoint(), 1e-9);
    }

    /**
     * Test optimization with custom values.
     */
    @Test
    public void testCustomOptimization() {
        final double[] startPoint = new double[] {1.0, 1.0};
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, 1e-8, 1e-8, null);
        final PointValuePair result = optimizer.optimize(startPoint, TEST_FUNCTION, GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue(), 1e-9);
        assertArrayEquals(new double[] {-1.19646465, -1.19646465}, result.getPoint(), 1e-9);
    }

    /**
     * Test optimization with specific convergence criteria.
     */
    @Test
    public void testCustomConvergenceCriteria() {
        final double[] startPoint = new double[] {1.0, 1.0};
        final ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                final double delta = Math.abs(previous.getValue() - current.getValue());
                return delta < 1e-10;
            }
        };
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, convergenceChecker);
        final PointValuePair result = optimizer.optimize(startPoint, TEST_FUNCTION, GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue(), 1e-9);
        assertArrayEquals(new double[] {-1.19646465, -1.19646465}, result.getPoint(), 1e-9);
    }

    /**
     * Test optimization with specific bounds.
     */
    @Test
    public void testCustomBounds() {
        final double[] startPoint = new double[] {1.0, 1.0};
        final SimpleBounds bounds = new SimpleBounds(new double[] {-10, -10}, new double[] {10, 10});
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, null, null, null);
        final PointValuePair result = optimizer.optimize(startPoint, TEST_FUNCTION, GoalType.MINIMIZE, bounds);
        assertEquals(0.0, result.getValue(), 1e-9);
        assertArrayEquals(new double[] {-1.19646465, -1.19646465}, result.getPoint(), 1e-9);
    }

    /**
     * Test optimization with MaxEval exception.
     */
    @Test(expected = MaxEval. class)
    public void testMaxEvalException() {
        final double[] startPoint = new double[] {1.0, 1.0};
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, null, null, null);
        optimizer.optimize(startPoint, new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new MaxEval(1, "maximum number of evaluations exceeded");
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException();
            }
        }, GoalType.MINIMIZE);
    }
}
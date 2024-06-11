package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PowellOptimizerTest {

    private PowellOptimizer optimizer;
    private final double[] startPoint = new double[]{1.0, 2.0, 3.0};
    private final UnivariateFunction rosenbrockFunction = new RosenbrockFunction();

    @Before
    public void setup() {
        optimizer = new PowellOptimizer(1e-5, 1e-5);
    }

    @Test
    public void testConstructorWithDefaultValues() {
        PowellOptimizer powellOptimizer = new PowellOptimizer();
        assertNotNull(powellOptimizer);
    }

    @Test
    public void testOptimize() throws ConvergenceException, MaxEvaluationsExceededException {
        PointValuePair result = optimizer.optimize(rosenbrockFunction, startPoint);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(-1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), 1e-5);
    }

    @Test
    public void testOptimizeWithCustomGoalType() throws ConvergenceException, MaxEvaluationsExceededException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(rosenbrockFunction, startPoint);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(3.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, result.getPoint(), 1e-5);
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() throws ConvergenceException, MaxEvaluationsExceededException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, new CustomConvergenceChecker());
        PointValuePair result = optimizer.optimize(rosenbrockFunction, startPoint);
        assertTrue(optimizer.isConverged());
        assertEquals(-1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), 1e-5);
    }

    @Test
    public void testOptimizeWithExceptionalInput() {
        assertThrows(IllegalArgumentException.class, () -> new PowellOptimizer(-1.0, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new PowellOptimizer(1.0, -1.0));
        assertThrows(IllegalArgumentException.class, () -> new PowellOptimizer(0.0, 0.0));
    }

    @Test
    public void testOptimizeWithNullFunction() {
        assertThrows(NullPointerException.class, () -> optimizer.optimize(null, startPoint));
    }

    @Test
    public void testOptimizeWithNullStartPoint() {
        assertThrows(NullPointerException.class, () -> optimizer.optimize(rosenbrockFunction, null));
    }

    @Test
    public void testOptimizeWithFunctionThatReturnsNaN() throws ConvergenceException, MaxEvaluationsExceededException {
        UnivariateFunction nanFunction = new UnivariateFunction() {
            public double value(double x) {
                return Double.NaN;
            }
        };
        assertThrows(ConvergenceException.class, () -> optimizer.optimize(nanFunction, startPoint));
    }

    @Test
    public void testOptimizeWithFunctionThatReturnsInfiniteValue()
            throws ConvergenceException, MaxEvaluationsExceededException {
        UnivariateFunction infiniteFunction = new UnivariateFunction() {
            public double value(double x) {
                return Double.POSITIVE_INFINITY;
            }
        };
        assertThrows(MaxEvaluationsExceededException.class, () -> optimizer.optimize(infiniteFunction, startPoint));
    }

    @Test
    public void testOptimizeWithFunctionThatDoesNotChange() throws ConvergenceException, MaxEvaluationsExceededException {
        UnivariateFunction constantFunction = new UnivariateFunction() {
            public double value(double x) {
                return 1.0;
            }
        };
        PointValuePair result = optimizer.optimize(constantFunction, startPoint);
        assertEquals(1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, result.getPoint(), 1e-5);
    }

    private static class RosenbrockFunction implements UnivariateFunction {
        public double value(double x) {
            double sum = 0.0;
            for (int i = 0; i < startPoint.length; i++) {
                sum += ((i > 0) ? (x[i - 1] * x[i - 1] - x[i] * x[i] + x[i] * x[i]) : (x[i] * x[i] - x[i] * x[i] + x[i] * x[i]));
            }
            return sum;
        }
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            return (iter > 10);
        }
    }
}
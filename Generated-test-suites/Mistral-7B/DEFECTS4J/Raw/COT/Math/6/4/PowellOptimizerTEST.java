package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePairCollection;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.InitialGuess;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    /**
     * Test PowellOptimizer constructor with default parameters.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testDefaultConstructor() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
    }

    /**
     * Test PowellOptimizer constructor with custom convergence checker.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testCustomConvergenceCheckerConstructor() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3, new MyConvergenceChecker());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1e-3, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(1e-3, optimizer.getRelativeThreshold(), 0.0);
    }

    /**
     * Test optimization of a univariate function.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testOptimizeUnivariateFunction() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0, 1), new InitialGuess(0.5), new MaxEval(100));

        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.25, result.getPoint(), 0.0);
        assertEquals(0, result.getValue(), 0.0);
    }

    /**
     * Test optimization of a multivariate function.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testOptimizeMultivariateFunction() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            public int getNumberOfVariables() {
                return 2;
            }
        };

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(new double[]{-1, -1}, new double[]{1, 1}),
                new InitialGuess(new double[]{0.5, 0.5}), new MaxEval(100));

        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.25, result.getPoint()[0], 0.0);
        assertEquals(0.25, result.getPoint()[1], 0.0);
        assertEquals(0, result.getValue(), 0.0);
    }

    /**
     * Test optimization with custom convergence checker.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testOptimizeWithCustomConvergenceChecker() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3, new MyConvergenceChecker());

        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            public int getNumberOfVariables() {
                return 2;
            }
        };

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(new double[]{-1, -1}, new double[]{1, 1}),
                new InitialGuess(new double[]{0.5, 0.5}), new MaxEval(100));

        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.25, result.getPoint()[0], 0.0);
        assertEquals(0.25, result.getPoint()[1], 0.0);
        assertEquals(0, result.getValue(), 0.0);

        // Custom convergence checker should have been called.
        assertTrue(MyConvergenceChecker.hasBeenCalled);
    }

    /**
     * Test optimization with non-converging function.
     * @throws NotStrictlyPositiveException
     */
    @Test(expected = org.apache.commons.math3.optim.MaxEvalException.class)
    public void testOptimizeNonConvergingFunction() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.log(Math.abs(x[0]));
            }

            public int getNumberOfVariables() {
                return 1;
            }
        };

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(new double[]{-1, -1}, new double[]{1, 1}),
                new InitialGuess(0.5), new MaxEval(1));

        // Optimization should not converge.
        assertNull(result);
    }

    /**
     * Test optimization with negative absolute threshold.
     * @throws NotStrictlyPositiveException
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeAbsoluteThreshold() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, -1e-3);
    }

    /**
     * Test optimization with negative relative threshold.
     * @throws NotStrictlyPositiveException
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeWithNegativeRelativeThreshold() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-7);
    }

    /**
     * Custom convergence checker.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        /**
         * Flag indicating whether the custom convergence checker has been called.
         */
        static boolean hasBeenCalled = false;

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            hasBeenCalled = true;
            return (iteration > 1) && (previous.getValue() > current.getValue());
        }
    }
}
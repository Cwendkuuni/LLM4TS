
package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-6;

    private static final int MAX_EVALUATIONS = 10000;

    private static final double MIN_VALUE = 0.0;

    private static final double MAX_VALUE = 10.0;

    private static final GoalType GOAL_TYPE = GoalType.MINIMIZE;

    private static final int NUM_STARTS = 5;

    private static final double[] TEST_STARTS = {0.5, 2.0, 4.5, 6.5, 9.0};

    /** Function to be optimized. */
    private UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) throws FunctionEvaluationException {
            return x * x;
        }
    };

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(createBaseOptimizer(), NUM_STARTS, null);
        UnivariateRealPointValuePair result = optimizer.optimize(function, GOAL_TYPE, MIN_VALUE, MAX_VALUE);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), EPSILON);
        assertEquals(0.0, result.getValue(), EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithConvergenceException() throws FunctionEvaluationException {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(createBaseOptimizer(), NUM_STARTS, null);
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        }, GOAL_TYPE, MIN_VALUE, MAX_VALUE);
    }

    @Test
    public void testOptimizeWithNullGenerator() throws FunctionEvaluationException {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(createBaseOptimizer(), TEST_STARTS.length, null);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariateRealPointValuePair>() {
            public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                return false;
            }
        });
        UnivariateRealPointValuePair result = optimizer.optimize(function, GOAL_TYPE, TEST_STARTS[0], TEST_STARTS[0]);
        assertNotNull(result);
        assertEquals(TEST_STARTS[0], result.getPoint(), EPSILON);
        assertEquals(TEST_STARTS[0] * TEST_STARTS[0], result.getValue(), EPSILON);
        for (int i = 1; i < TEST_STARTS.length; ++i) {
            assertNull(optimizer.optimize(function, GOAL_TYPE, TEST_STARTS[i], TEST_STARTS[i]));
        }
    }

    @Test
    public void testOptimizeWithValidGenerator() throws FunctionEvaluationException {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(createBaseOptimizer(), TEST_STARTS.length, new RandomGenerator() {
                public void setSeed(long seed) {}
                public void setSeed(int[] seed) {}
                public void setSeed(int seed) {}
                public void nextBytes(byte[] bytes) {}
                public int nextInt() { return 0; }
                public int nextInt(int n) { return 0; }
                public long nextLong() { return 0; }
                public boolean nextBoolean() { return false; }
                public float nextFloat() { return 0; }
                public double nextDouble() { return 0; }
                public double nextGaussian() { return 0; }
            });
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariateRealPointValuePair>() {
            public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                return false;
            }
        });
        for (int i = 0; i < TEST_STARTS.length; ++i) {
            UnivariateRealPointValuePair result = optimizer.optimize(function, GOAL_TYPE, TEST_STARTS[i], TEST_STARTS[i]);
            assertNotNull(result);
            assertEquals(TEST_STARTS[i], result.getPoint(), EPSILON);
            assertEquals(TEST_STARTS[i] * TEST_STARTS[i], result.getValue(), EPSILON);
        }
    }

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> createBaseOptimizer() {
        return new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public int getMaxEvaluations() { return MAX_EVALUATIONS; }
            public int getEvaluations() { return 0; }
            public void setMaxEvaluations(int maxEvaluations) {}
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker) {}
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() { return null; }
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) {
                return new UnivariateRealPointValuePair(0.0, 0.0);
            }
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) {
                return new UnivariateRealPointValuePair(0.0, 0.0);
            }
        };
    }
}

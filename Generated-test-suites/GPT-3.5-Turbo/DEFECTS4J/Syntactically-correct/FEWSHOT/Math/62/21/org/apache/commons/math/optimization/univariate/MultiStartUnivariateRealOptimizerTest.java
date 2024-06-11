
package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    private BaseUnivariateRealOptimizer<FUNC> optimizer;
    private int starts;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) { }
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() { return null; }
            public int getMaxEvaluations() { return 0; }
            public int getEvaluations() { return 0; }
            public void setMaxEvaluations(int maxEvaluations) { }
            public UnivariateRealPointValuePair optimize(final FUNC f, final GoalType goal,
                                                         final double min, final double max)
                throws FunctionEvaluationException { return null; }
            public UnivariateRealPointValuePair optimize(final FUNC f, final GoalType goal,
                                                         final double min, final double max,
                                                         final double startValue)
                throws FunctionEvaluationException { return null; }
        };
        starts = 10;
        generator = new RandomGenerator() {
            public double nextDouble() { return 0.0; }
            public double nextDouble(double min, double max) { return 0.0; }
            public int nextInt() { return 0; }
            public int nextInt(int n) { return 0; }
            public boolean nextBoolean() { return false; }
            public float nextFloat() { return 0; }
            public long nextLong() { return 0; }
            public void setSeed(int seed) { }
            public void setSeed(int[] seed) { }
            public void setSeed(long seed) { }
            public void nextBytes(byte[] bytes) { }
        };
    }

    @Test
    public void testGetConvergenceChecker() {
      MultiStartUnivariateRealOptimizer<FUNC> multiStartOpt = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
      assertNull(multiStartOpt.getConvergenceChecker());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaThrowsException() {
      MultiStartUnivariateRealOptimizer<FUNC> multiStartOpt = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
      multiStartOpt.getOptima();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeThrowsException() throws FunctionEvaluationException {
      MultiStartUnivariateRealOptimizer<FUNC> multiStartOpt = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
      multiStartOpt.optimize(null, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
      MultiStartUnivariateRealOptimizer<FUNC> multiStartOpt = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
      UnivariateRealPointValuePair result = multiStartOpt.optimize(null, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
      assertEquals(0.0, result.getValue(), 0.0);
    }
}

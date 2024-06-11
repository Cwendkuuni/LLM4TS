
package org.apache.commons.math.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private static MultiStartUnivariateRealOptimizer optimizer;

    @BeforeClass
    public static void setUp() {
        UnivariateRealOptimizer underlyingOptimizer = new DummyOptimizer();
        RandomGenerator generator = new DummyRandomGenerator();
        int starts = 5;
        optimizer = new MultiStartUnivariateRealOptimizer(underlyingOptimizer, starts, generator);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        assertEquals(optimizer.getAbsoluteAccuracy(), 0.0, 0.0);
    }

    @Test
    public void testGetResult() {
        assertEquals(optimizer.getResult(), 0.0, 0.0);
    }

    @Test
    public void testGetIterationCount() {
        assertEquals(optimizer.getIterationCount(), 0);
    }

    @Test
    public void testGetMaximalIterationCount() {
        assertEquals(optimizer.getMaximalIterationCount(), Integer.MAX_VALUE);
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(optimizer.getMaxEvaluations(), Integer.MAX_VALUE);
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(optimizer.getEvaluations(), 0);
    }

    @Test
    public void testGetRelativeAccuracy() {
        assertEquals(optimizer.getRelativeAccuracy(), 0.0, 0.0);
    }

    @Test
    public void testGetOptima() {
        assertNull(optimizer.getOptima());
    }

    @Test
    public void testGetOptimaValues() {
        assertNull(optimizer.getOptimaValues());
    }

    @Test
    public void testOptimize() throws Exception {
        UnivariateRealFunction f = new DummyFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double result = optimizer.optimize(f, goalType, min, max);
        assertEquals(result, 0.0, 0.0);
    }

}

class DummyOptimizer implements UnivariateRealOptimizer {
    public double getFunctionValue() {
        return 0.0;
    }

    public double getResult() {
        return 0.0;
    }

    public double getAbsoluteAccuracy() {
        return 0.0;
    }

    public int getIterationCount() {
        return 0;
    }

    public int getMaximalIterationCount() {
        return Integer.MAX_VALUE;
    }

    public int getMaxEvaluations() {
        return Integer.MAX_VALUE;
    }

    public int getEvaluations() {
        return 0;
    }

    public double getRelativeAccuracy() {
        return 0.0;
    }

    public void resetAbsoluteAccuracy() {}

    public void resetMaximalIterationCount() {}

    public void resetRelativeAccuracy() {}

    public void setAbsoluteAccuracy(double accuracy) {}

    public void setMaximalIterationCount(int count) {}

    public void setMaxEvaluations(int maxEvaluations) {}

    public void setRelativeAccuracy(double accuracy) {}

    public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max) throws FunctionEvaluationException {
        return 0.0;
    }

    public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max, double startValue) throws FunctionEvaluationException {
        return 0.0;
    }
}

class DummyRandomGenerator implements RandomGenerator {
    public void setSeed(int seed) {}

    public void setSeed(int[] seed) {}

    public void setSeed(long seed) {}

    public void nextBytes(byte[] bytes) {}

    public int nextInt() {
        return 0;
    }

    public int nextInt(int n) {
        return 0;
    }

    public long nextLong() {
        return 0;
    }

    public boolean nextBoolean() {
        return false;
    }

    public float nextFloat() {
        return 0.0f;
    }

    public double nextDouble() {
        return 0.0;
    }

    public double nextGaussian() {
        return 0.0;
    }
}

class DummyFunction implements UnivariateRealFunction {
    public double value(double x) {
        return 0.0;
    }
}
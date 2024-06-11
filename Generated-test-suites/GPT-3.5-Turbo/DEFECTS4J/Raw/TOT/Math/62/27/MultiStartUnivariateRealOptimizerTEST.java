import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizerImpl(), 5, new RandomGeneratorImpl());
    }

    @Test
    public void testOptimizeWithSingleStart() {
        UnivariateRealFunction function = new UnivariateRealFunctionImpl();
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
        assertEquals(1.0, result.getValue(), 1e-5);
    }

    @Test
    public void testOptimizeWithMultipleStart() {
        UnivariateRealFunction function = new UnivariateRealFunctionImpl();
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 10, 5.0);
        assertEquals(0.5, result.getValue(), 1e-5);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() {
        UnivariateRealFunction function = new InvalidFunctionImpl();
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithConvergenceException() {
        UnivariateRealFunction function = new ConvergingFunctionImpl();
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
    }

    @Test
    public void testGetOptima() {
        UnivariateRealFunction function = new UnivariateRealFunctionImpl();
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(5, optima.length);
        assertEquals(1.0, optima[0].getValue(), 1e-5);
        assertEquals(1.0, optima[optima.length-1].getValue(), 1e-5);
    }

    private class UnivariateRealOptimizerImpl implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() { return null; }
        public int getMaxEvaluations() { return 0; }
        public int getEvaluations() { return 0; }
        public void setMaxEvaluations(int maxEvaluations) {}
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException { return null; }

    }

    private class RandomGeneratorImpl implements RandomGenerator {

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

    }

    private class UnivariateRealFunctionImpl implements UnivariateRealFunction {

        public double value(double x) {
            if (x <= 1) {
                return 2 - x;
            } else {
                return x - 1;
            }
        }

    }

    private class InvalidFunctionImpl implements UnivariateRealFunction {

        public double value(double x) {
            throw new FunctionEvaluationException(x);
        }

    }

    private class ConvergingFunctionImpl implements UnivariateRealFunction {

        public double value(double x) {
            double sum = 0;
            for (int i = 0; i < 1000000000; i++) {
                sum += Math.sin(x);
            }
            return sum;
        }

    }

}
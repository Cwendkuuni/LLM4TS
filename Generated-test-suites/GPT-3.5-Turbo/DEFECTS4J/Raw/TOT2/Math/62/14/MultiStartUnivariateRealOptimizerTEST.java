import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimizeWithSingleStart() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4; // (x - 2)^2 function
            }
        };

        RandomGenerator randomGen = new DummyRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 1, randomGen);

        UnivariateRealPointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE, 0.0, 5.0);
        
        assertEquals(2.0, result.getPoint(), 1e-10);
    }

    @Test
    public void testOptimizeWithMultipleStarts() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // x^2 function
            }
        };

        RandomGenerator randomGen = new DummyRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 3, randomGen);

        UnivariateRealPointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);
        
        assertEquals(0.0, result.getPoint(), 1e-10);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithNoConvergence() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            public double value(double x) {
                return 0.0; // Constant function with no minimum/maximum
            }
        };

        RandomGenerator randomGen = new DummyRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 5, randomGen);

        optimizer.optimize(func, GoalType.MINIMIZE, -5.0, 5.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            public double value(double x) {
                if (x == 0.0) {
                    throw new RuntimeException("Test exception");
                }
                return x * x; 
            }
        };

        RandomGenerator randomGen = new DummyRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 2, randomGen);

        optimizer.optimize(func, GoalType.MINIMIZE, -2.0, 2.0);
    }

    // Dummy RandomGenerator for testing purposes
    private class DummyRandomGenerator implements RandomGenerator {
        public void setSeed(long seed) {}
        public void setSeed(int[] seed) {}
        public void setSeed(int seed) {}
        public void nextBytes(byte[] bytes) {}
        public int nextInt() {  return 0; }
        public int nextInt(int n) { return 0; }
        public long nextLong() { return 0; }
        public boolean nextBoolean() { return false; }
        public float nextFloat() { return 0.0f; }
        public double nextDouble() { return 0.0; }
        public double nextGaussian() { return 0.0; }
    }

}
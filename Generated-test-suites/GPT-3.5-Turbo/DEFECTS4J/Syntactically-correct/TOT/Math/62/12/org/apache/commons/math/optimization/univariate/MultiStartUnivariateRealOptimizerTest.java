import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<TestFunction> optimizer;
    private TestFunction testFunction;
    
    @Before
    public void setUp() {
        BaseUnivariateRealOptimizer<TestFunction> underlyingOptimizer = new TestOptimizer();
        RandomGenerator generator = new TestRandomGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer<>(underlyingOptimizer, 3, generator);
        testFunction = new TestFunction();
    }
    
    @Test
    public void testOptimize() throws Exception {
        optimizer.setMaxEvaluations(1000);
        double minValue = -10.0;
        double maxValue = 10.0;
        double minValue2 = -20.0;
        double maxValue2 = 20.0;
        double startValue = 0.0;

        // Typical use case
        UnivariateRealPointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, minValue, maxValue, startValue);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);

        // Edge cases
        result = optimizer.optimize(testFunction, GoalType.MINIMIZE, minValue, minValue, startValue);
        assertNotNull(result);
        assertEquals(minValue, result.getPoint(), 0.0001);
        assertEquals(minValue, result.getValue(), 0.0001);
        
        result = optimizer.optimize(testFunction, GoalType.MINIMIZE, maxValue, maxValue, startValue);
        assertNotNull(result);
        assertEquals(maxValue, result.getPoint(), 0.0001);
        assertEquals(maxValue, result.getValue(), 0.0001);
        
        // Error scenarios
        try {
            result = optimizer.optimize(testFunction, GoalType.MINIMIZE, minValue2, maxValue2, startValue);
            fail("Exception not thrown");
        } catch (MathIllegalStateException e) {
            assertEquals(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, e.getMessageEnum());
        }
        
        try {
            result = optimizer.optimize(null, GoalType.MINIMIZE, minValue, maxValue, startValue);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Function cannot be null", e.getMessage());
        }
    }
    
    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x;
        }
    }
    
    private class TestOptimizer implements BaseUnivariateRealOptimizer<TestFunction> {
        private int maxEvaluations;
        private int evaluations;
        
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
        
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return null;
        }
        
        public int getMaxEvaluations() {
            return maxEvaluations;
        }
        
        public int getEvaluations() {
            return evaluations;
        }
        
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }
        
        public UnivariateRealPointValuePair optimize(TestFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
            evaluations++;
            double value = f.value((min + max) / 2);
            return new UnivariateRealPointValuePair((min + max) / 2, value);
        }
    }
    
    private class TestRandomGenerator implements RandomGenerator {
        public double nextDouble() {
            return 0.5;
        }
        
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
            return 0.5f;
        }
        
        public double nextGaussian() {
            return 0.5;
        }
    }

}
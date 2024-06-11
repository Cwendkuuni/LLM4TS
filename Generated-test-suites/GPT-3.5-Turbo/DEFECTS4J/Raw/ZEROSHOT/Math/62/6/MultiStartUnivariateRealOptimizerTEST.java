import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    
    private MultiStartUnivariateRealOptimizer<MyFunction> optimizer;
    
    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<MyFunction>(new MyOptimizer(), 5, new MyRandomGenerator());
    }
    
    @After
    public void tearDown() {
        optimizer = null;
    }
    
    @Test
    public void testOptimize() throws FunctionEvaluationException {
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;
        
        MyFunction f = new MyFunction();
        UnivariateRealPointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
}

class MyFunction implements UnivariateRealFunction {
    
    public double value(double x) {
        return x * x;
    }
}

class MyOptimizer implements BaseUnivariateRealOptimizer<MyFunction> {
    
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
    }
    
    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
        return null;
    }
    
    public int getMaxEvaluations() {
        return 0;
    }
    
    public int getEvaluations() {
        return 0;
    }
    
    public void setMaxEvaluations(int maxEvaluations) {
    }
    
    public UnivariateRealPointValuePair optimize(MyFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
        double x = (min + max) / 2.0;
        double value = f.value(x);
        return new UnivariateRealPointValuePair(x, value);
    }
}

class MyRandomGenerator implements RandomGenerator {
    
    public boolean nextBoolean() {
        return false;
    }
    
    public double nextDouble() {
        return 0.5;
    }
    
    public void nextBytes(byte[] bytes) {
    }
    
    public float nextFloat() {
        return 0.5f;
    }
    
    public double nextGaussian() {
        return 0.0;
    }
    
    public int nextInt() {
        return 0;
    }
    
    public int nextInt(int n) {
        return 0;
    }
    
    public long nextLong() {
        return 0;
    }
    
    public void setSeed(int seed) {
    }
    
    public void setSeed(int[] seed) {
    }
    
    public void setSeed(long seed) {
    }
    
}
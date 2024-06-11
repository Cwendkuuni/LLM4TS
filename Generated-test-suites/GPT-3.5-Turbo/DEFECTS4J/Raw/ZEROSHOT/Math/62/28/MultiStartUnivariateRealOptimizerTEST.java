import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {
    
    private MultiStartUnivariateRealOptimizer<FUNC> optimizer;
    
    @Before
    public void setUp() {
        BaseUnivariateRealOptimizer<FUNC> singleStart = new DummyUnivariateRealOptimizer();
        int starts = 3;
        RandomGenerator generator = new DummyRandomGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(singleStart, starts, generator);
    }
    
    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new DummyConvergenceChecker();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }
    
    @Test
    public void testOptimize() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 10.0;
        FUNC f = new DummyUnivariateRealFunction();
        GoalType goal = GoalType.MINIMIZE;
        UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.0);
    }
    
    @Test(expected = MathIllegalStateException.class)
    public void testGetOptima() {
        optimizer.getOptima();
    }
    
    private class DummyUnivariateRealOptimizer implements BaseUnivariateRealOptimizer<FUNC> {
        
        private int maxEvaluations = 100;
        private int evaluations = 0;
        private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;
        
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            convergenceChecker = checker;
        }
        
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return convergenceChecker;
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
        
        public UnivariateRealPointValuePair optimize(final FUNC f, final GoalType goal, final double min, final double max)
            throws FunctionEvaluationException {
            evaluations++;
            return new UnivariateRealPointValuePair(0.0, new double[] {0.0});
        }
    }
    
    private class DummyRandomGenerator implements RandomGenerator {
        
        public boolean nextBoolean() {
            return false;
        }
        
        public double nextDouble() {
            return 0.5;
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
            return 0L;
        }
        
        public void setSeed(int seed) {
            
        }
        
        public void setSeed(int[] seed) {
            
        }
        
        public void setSeed(long seed) {
            
        }
    }
    
    private class DummyConvergenceChecker implements ConvergenceChecker<UnivariateRealPointValuePair> {
        
        public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
            return false;
        }
    }
    
    private class DummyUnivariateRealFunction implements UnivariateRealFunction {
        
        public double value(double x) {
            return 0.0;
        }
    }
}
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Assert;
import org.junit.Test;

public class BaseOptimizerTest {
    private class MockOptimizer implements BaseOptimizer<Object> {
    
        @Override
        public int getMaxEvaluations() {
            return 0;
        }
        
        @Override
        public int getEvaluations() {
            return 0;
        }
        
        @Override
        public ConvergenceChecker<Object> getConvergenceChecker() {
            return null;
        }
    }
    
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new MockOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(0, maxEvaluations);
    }
    
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new MockOptimizer();
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(0, evaluations);
    }
    
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new MockOptimizer();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNull(convergenceChecker);
    }
}
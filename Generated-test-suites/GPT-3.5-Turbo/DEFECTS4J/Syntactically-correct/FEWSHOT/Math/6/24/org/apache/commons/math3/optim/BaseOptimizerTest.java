import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BaseOptimizerTest {
    
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();
        assertEquals(0, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();
        assertEquals(0, optimizer.getEvaluations());
    }
    
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();
        assertNull(optimizer.getConvergenceChecker());
    }
    
    // Additional test cases for BaseOptimizer methods can be added here
    
}

class BaseOptimizerImpl implements BaseOptimizer<PAIR> {
    
    public int getMaxEvaluations() {
        return 0;
    }
    
    public int getEvaluations() {
        return 0;
    }
    
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return null;
    }
}
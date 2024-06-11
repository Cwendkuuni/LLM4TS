import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl(100);
        assertEquals(0, optimizer.getEvaluations());
        
        optimizer.optimize(); // call the optimize method to increase the evaluations count
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerImpl(100);
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        
        assertNotNull(convergenceChecker);
    }
}
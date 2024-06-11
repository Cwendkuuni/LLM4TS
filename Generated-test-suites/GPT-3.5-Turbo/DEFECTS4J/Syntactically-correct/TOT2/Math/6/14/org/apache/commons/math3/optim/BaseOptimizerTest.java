import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertNotNull(optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testGetEvaluationsAfterOptimizeMethodCall() {
        BaseOptimizer optimizer = new BaseOptimizer();
        // simulate calling the optimize method
        // and check if the number of evaluations is updated
        optimizer.optimize();
        assertEquals(1, optimizer.getEvaluations());
    }
    
    @Test
    public void testConvergenceCheckerNotNull() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertNotNull(optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testConvergenceCheckerInstance() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertTrue(optimizer.getConvergenceChecker() instanceof ConvergenceChecker);
    }

    @Test
    public void testConvergenceCheckerReturnsFalse() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertFalse(optimizer.getConvergenceChecker().converged(null, null));
    }
}
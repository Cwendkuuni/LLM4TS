import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer optimizer = new BaseOptimizer();
        optimizer.setConvergenceChecker(null);
        assertNull(optimizer.getConvergenceChecker());
    }
}
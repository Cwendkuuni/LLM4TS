import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        assertNotNull(optimizer.getConvergenceChecker());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedMethods() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        optimizer.deprecatedMethod();  // This method should throw an UnsupportedOperationException
    }

}
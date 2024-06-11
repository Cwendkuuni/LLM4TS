import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<String> optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Double> optimizer = new BaseOptimizer<>();
        assertNotNull(optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testDeprecatedAnnotation() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        assertNotNull(optimizer.getClass().getAnnotation(Deprecated.class));
    }
}
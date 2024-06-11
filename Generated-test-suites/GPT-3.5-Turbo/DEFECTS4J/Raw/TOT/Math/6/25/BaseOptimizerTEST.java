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
    public void testDeprecatedAnnotation() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertTrue(optimizer.getClass().isAnnotationPresent(Deprecated.class));
    }

    @Test
    public void testDeprecatedMessage() {
        BaseOptimizer optimizer = new BaseOptimizer();
        Deprecated deprecated = optimizer.getClass().getAnnotation(Deprecated.class);
        String message = deprecated.toString();
        assertTrue(message.contains("As of 3.1 (to be removed in 4.0)"));
    }
}
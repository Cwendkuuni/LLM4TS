import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Integer> baseOptimizer = new BaseOptimizer<>();
        assertEquals(0, baseOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Integer> baseOptimizer = new BaseOptimizer<>();
        assertEquals(0, baseOptimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Integer> baseOptimizer = new BaseOptimizer<>();
        assertNotNull(baseOptimizer.getConvergenceChecker());
    }
}
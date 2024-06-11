
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker checker = new ConvergenceChecker<>();
        BaseOptimizer optimizer = new BaseOptimizer<>();
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
}

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();

        int maxEvaluations = optimizer.getMaxEvaluations();

        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();

        int evaluations = optimizer.getEvaluations();

        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();

        ConvergenceChecker convergenceChecker = optimizer.getConvergenceChecker();

        assertNotNull(convergenceChecker);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();

        int result = optimizer.getMaxEvaluations();

        // functionality test
        assertEquals(0, result);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();

        int result = optimizer.getEvaluations();

        // functionality test
        assertEquals(0, result);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();

        ConvergenceChecker<PAIR> result = optimizer.getConvergenceChecker();

        // functionality test
        assertNotNull(result);
    }

}
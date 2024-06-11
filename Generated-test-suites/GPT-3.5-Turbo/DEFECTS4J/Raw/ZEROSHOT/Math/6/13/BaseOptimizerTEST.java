import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<PAIR>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<PAIR>();
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<PAIR>();
        ConvergenceChecker<PAIR> checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
    }
}

class BaseOptimizerImpl<PAIR> implements BaseOptimizer<PAIR> {
    public int getMaxEvaluations() {
        return 0;
    }

    public int getEvaluations() {
        return 0;
    }

    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return null;
    }
}
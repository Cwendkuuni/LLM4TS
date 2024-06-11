import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        ConvergenceChecker checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
    }

    // Additional test methods go here

}

class BaseOptimizerImpl implements BaseOptimizer {

    public int getMaxEvaluations() {
        return 100;
    }

    public int getEvaluations() {
        return 0;
    }

    public ConvergenceChecker getConvergenceChecker() {
        return new DummyConvergenceChecker();
    }

}

class DummyConvergenceChecker<PAIR> implements ConvergenceChecker<PAIR> {

    public boolean converged(int iteration, PAIR previous, PAIR current) {
        // Dummy implementation, always returns true
        return true;
    }
}
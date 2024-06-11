import static org.junit.Assert.*;

import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        assertNotNull(optimizer.getConvergenceChecker());
    }

    private class BaseOptimizerImpl implements BaseOptimizer<Object> {
        public int getMaxEvaluations() {
            return 0;
        }

        public int getEvaluations() {
            return 0;
        }

        public ConvergenceChecker<Object> getConvergenceChecker() {
            return new ConvergenceChecker<>() {
                public boolean converged(int iteration, Object previous, Object current) {
                    return false;
                }
            };
        }
    }
}
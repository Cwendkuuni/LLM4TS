import static org.junit.Assert.*;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<Integer>() {
            public int getMaxEvaluations() {
                return 100;
            }

        };
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<Integer>() {
            public int getEvaluations() {
                return 50;
            }

        };
        assertEquals(50, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<Integer>() {
            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return new ConvergenceChecker<Integer>() {
                    public boolean converged(Integer x1, Integer x2) {
                        return x1.equals(x2);
                    }
                };
            }
        };
        ConvergenceChecker<Integer> checker = optimizer.getConvergenceChecker();
        assertTrue(checker.converged(5, 5));
        assertFalse(checker.converged(10, 5));
    }
}
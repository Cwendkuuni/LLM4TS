import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        // Create an instance of BaseOptimizer
        BaseOptimizer optimizer = new BaseOptimizerImpl();

        // Test with positive input
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());

        // Test with zero input
        optimizer.setMaxEvaluations(0);
        assertEquals(0, optimizer.getMaxEvaluations());

        // Test with negative input
        optimizer.setMaxEvaluations(-100);
        assertEquals(-100, optimizer.getMaxEvaluations());

        // Test with large input
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Create an instance of BaseOptimizer
        BaseOptimizer optimizer = new BaseOptimizerImpl();

        // Test before optimization
        assertEquals(0, optimizer.getEvaluations());

        // Test after optimization
        optimizer.optimize();
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        // Create an instance of BaseOptimizer
        BaseOptimizer optimizer = new BaseOptimizerImpl();

        // Test with null input
        assertNull(optimizer.getConvergenceChecker());

        // Test with non-null input
        ConvergenceChecker<PAIR> checker = new ConvergenceCheckerImpl<>();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    // Helper classes for testing
    private class BaseOptimizerImpl implements BaseOptimizer<PAIR> {
        private int maxEvaluations;
        private int evaluations;
        private ConvergenceChecker<PAIR> convergenceChecker;

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public int getEvaluations() {
            return evaluations;
        }

        public void optimize() {
            evaluations++;
        }

        public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }
    }

    private class ConvergenceCheckerImpl<PAIR> implements ConvergenceChecker<PAIR> {
        public boolean converged(int iteration, PAIR previous, PAIR current) {
            return false;
        }
    }
}
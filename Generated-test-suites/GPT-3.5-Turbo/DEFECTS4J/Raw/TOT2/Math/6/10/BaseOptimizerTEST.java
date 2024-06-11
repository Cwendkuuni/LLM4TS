import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {

    private BaseOptimizer<Pair> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizerImpl();
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(1000, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertNotNull(optimizer.getConvergenceChecker());
    }

    private class BaseOptimizerImpl implements BaseOptimizer<Pair> {
        @Override
        public int getMaxEvaluations() {
            return 1000;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public ConvergenceChecker<Pair> getConvergenceChecker() {
            return new ConvergenceChecker<Pair>() {
                @Override
                public boolean converged(int iteration, Pair previous, Pair current) {
                    return true;
                }
            };
        }
    }
}
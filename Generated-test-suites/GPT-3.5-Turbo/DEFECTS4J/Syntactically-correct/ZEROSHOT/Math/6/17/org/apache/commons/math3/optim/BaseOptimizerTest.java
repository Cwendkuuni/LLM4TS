import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {

    private BaseOptimizer<Integer> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<Integer>() {
            public int getMaxEvaluations() {
                return 1000;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return null;
            }
        };
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
        assertNull(optimizer.getConvergenceChecker());
    }
}
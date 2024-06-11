import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    private BaseOptimizer<Integer> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<Integer>() {
            @Override
            public int getMaxEvaluations() {
                return 1000;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
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

    // Test for edge cases and exception handling scenarios can be added here

}
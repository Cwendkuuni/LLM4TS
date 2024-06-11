import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseOptimizerTest {

    private BaseOptimizer<Object> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<Object>() {
            public int getMaxEvaluations() {
                return 100;
            }
            public int getEvaluations() {
                return 0;
            }
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertEquals(null, optimizer.getConvergenceChecker());
    }
}
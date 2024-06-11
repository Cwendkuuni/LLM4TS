import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    private BaseOptimizer<PAIR> optimizer;

    @Before
    public void setup() {
        optimizer = new BaseOptimizer<PAIR>() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 10;
            }

            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return null;
            }
        };
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        assertEquals(10, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);
    }
}
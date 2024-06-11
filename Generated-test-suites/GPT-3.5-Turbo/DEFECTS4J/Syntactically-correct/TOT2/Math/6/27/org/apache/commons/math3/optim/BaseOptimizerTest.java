import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {

    private BaseOptimizer<Object> optimizer;

    @Before
    public void setup() {
        optimizer = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                return 1000;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
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
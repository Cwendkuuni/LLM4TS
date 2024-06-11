import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BaseOptimizerTest {

    private BaseOptimizer<TestObject> optimizer;

    // Set up the optimizer before each test
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
        assertNull(optimizer.getConvergenceChecker());
    }
}

class BaseOptimizerImpl implements BaseOptimizer<TestObject> {

    @Override
    public int getMaxEvaluations() {
        return 1000;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<TestObject> getConvergenceChecker() {
        return null;
    }
}

class TestObject {
    // Define test object here
}
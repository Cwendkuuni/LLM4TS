import static org.junit.Assert.*;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        optimizer.setMaxEvaluations(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        optimizer.setEvaluations(50);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();
        ConvergenceChecker checker = new ConvergenceChecker();
        optimizer.setConvergenceChecker(checker);
    }
}
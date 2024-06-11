import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }
    
    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }
    
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();
        ConvergenceChecker checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
    }

    @Test
    public void testDeprecatedInterface() {
        BaseOptimizer optimizer = new BaseOptimizer();
        Deprecated annotation = optimizer.getClass().getAnnotation(Deprecated.class);
        assertNotNull(annotation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        optimizer.setMaxEvaluations(100);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        optimizer.setEvaluations(10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();
        optimizer.setConvergenceChecker(new ConvergenceChecker());
    }
}
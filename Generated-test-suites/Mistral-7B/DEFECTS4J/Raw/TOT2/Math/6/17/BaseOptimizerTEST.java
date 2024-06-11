import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optim.ConvergenceChecker;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseMockOptimizer<>();
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseMockOptimizer<>();
        optimizer.optimize(new MaxEval(new MockFunction(), new PointValuePair(0.0, 0.0)), new SimpleLineSearch(0.01));
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new BaseMockOptimizer<>();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }
}
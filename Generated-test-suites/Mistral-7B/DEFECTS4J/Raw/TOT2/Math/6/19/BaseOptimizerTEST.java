import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizerImpl<>(); // Assuming BaseOptimizerImpl is a valid implementation
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedMaxEvaluations = 100; // You should replace this value with the expected maximum number of evaluations
        assertEquals(expectedMaxEvaluations, maxEvaluations);
    }
}
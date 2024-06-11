import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testSetConvergenceChecker() {
        // Test setting convergence checker
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 3, null);
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<>();
        optimizer.setConvergenceChecker(checker);

        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        // Test getting max evaluations
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 3, null);
        optimizer.setMaxEvaluations(100);

        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Test getting evaluations
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 3, null);
        UnivariateRealPointValuePair pair = optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0);

        assertEquals(pair, optimizer.getOptima()[0]);
    }

    @Test
    public void testOptimize() {
        // Test optimizing function
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 3, null);
        UnivariateRealPointValuePair pair = optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0);

        assertNotNull(pair);
    }

    @Test
    public void testGetOptima() {
        // Test getting optima
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 3, null);
        UnivariateRealPointValuePair pair = optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0);

        assertEquals(pair, optimizer.getOptima()[0]);
    }
}
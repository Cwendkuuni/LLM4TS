
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer(new BaseUnivariateRealOptimizer(), 5, new RandomGenerator());
    }

    @Test
    public void testSetConvergenceChecker() {
        // Test functionality of setConvergenceChecker() method
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>();
        optimizer.setConvergenceChecker(checker);
        assertEquals(optimizer.getConvergenceChecker(), checker);
    }

    @Test
    public void testGetMaxEvaluations() {
        // Test functionality of getMaxEvaluations() method
        optimizer.setMaxEvaluations(100);
        assertEquals(optimizer.getMaxEvaluations(), 100);
    }

    @Test
    public void testGetEvaluations() {
        // Test functionality of getEvaluations() method by conducting optimization with one start and checking the number of evaluations
        UnivariateRealPointValuePair result = optimizer.optimize(new UnivariateRealFunction(), GoalType.MINIMIZE, 0, 10, 5);
        assertEquals(optimizer.getEvaluations(), 1);
    }

    @Test
    public void testGetOptima() {
        // Test functionality of getOptima() method by conducting optimization with five starts and checking the number of optima returned
        optimizer.optimize(new UnivariateRealFunction(), GoalType.MINIMIZE, 0, 10);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(optima.length, 5);
    }

    @Test
    public void testOptimize() {
        // Test overall functionality of optimize() method by conducting optimization with different parameters and checking the result
        UnivariateRealPointValuePair result = optimizer.optimize(new UnivariateRealFunction(), GoalType.MINIMIZE, 0, 10, 5);
        assertNotNull(result);
    }
}


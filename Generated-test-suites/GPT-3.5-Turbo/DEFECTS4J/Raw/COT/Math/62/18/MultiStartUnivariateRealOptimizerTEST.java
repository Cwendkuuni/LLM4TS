
import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testSetConvergenceChecker() {
        // Test functionality of setConvergenceChecker() method
        MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(new BaseUnivariateRealOptimizer<FUNC>(), 5, new RandomGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        // Test functionality of getMaxEvaluations() method
        MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(new BaseUnivariateRealOptimizer<FUNC>(), 5, new RandomGenerator());
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Test functionality of getEvaluations() method
        MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(new BaseUnivariateRealOptimizer<FUNC>(), 5, new RandomGenerator());
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testMinMaxEvaluations() {
        // Test functionality of setMaxEvaluations() and getMaxEvaluations() methods
        MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(new BaseUnivariateRealOptimizer<FUNC>(), 5, new RandomGenerator());
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
        optimizer.setMaxEvaluations(200);
        assertEquals(200, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testOptimize() throws FunctionEvaluationException {
        // Test functionality of optimize() method
        MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(new BaseUnivariateRealOptimizer<FUNC>(), 5, new RandomGenerator());
        UnivariateRealPointValuePair result = optimizer.optimize(new UnivariateRealFunction(), GoalType.MINIMIZE, 0.0, 1.0);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() throws FunctionEvaluationException {
        // Test functionality of optimize() method that throws ConvergenceException
        MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(new BaseUnivariateRealOptimizer<FUNC>(), 5, new RandomGenerator());
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(new UnivariateRealFunction(), GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        // Test functionality of getOptima() method
        MultiStartUnivariateRealOptimizer<FUNC> optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(new BaseUnivariateRealOptimizer<FUNC>(), 5, new RandomGenerator());
        UnivariateRealPointValuePair result = optimizer.optimize(new UnivariateRealFunction(), GoalType.MINIMIZE, 0.0, 1.0);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(5, optima.length);
        assertEquals(result, optima[0]);
    }

}

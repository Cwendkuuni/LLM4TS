import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testSetConvergenceChecker() {
        // Create a test instance of BaseUnivariateRealOptimizer
        BaseUnivariateRealOptimizer<FUNC> optimizer = new TestOptimizer();
        // Create a multi-start optimizer and set its convergence checker
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new TestConvergenceChecker();
        multiStartOptimizer.setConvergenceChecker(checker);
        // Verify that the convergence checker has been set
        assertEquals(checker, multiStartOptimizer.getConvergenceChecker());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        // Create a test instance of BaseUnivariateRealOptimizer
        BaseUnivariateRealOptimizer<FUNC> optimizer = new TestOptimizer();
        // Create a multi-start optimizer and set its max evaluations
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        // Verify that the max evaluations has been set
        assertEquals(maxEvaluations, multiStartOptimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        // Create a test instance of BaseUnivariateRealOptimizer
        BaseUnivariateRealOptimizer<FUNC> optimizer = new TestOptimizer();
        // Create a multi-start optimizer and set its max evaluations
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        // Verify that the evaluations start at 0
        assertEquals(0, multiStartOptimizer.getEvaluations());
        // Optimize the objective function
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        // Verify that the evaluations have increased
        assertTrue(multiStartOptimizer.getEvaluations() > 0);
        assertEquals(maxEvaluations, multiStartOptimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetOptima() {
        // Create a test instance of BaseUnivariateRealOptimizer
        BaseUnivariateRealOptimizer<FUNC> optimizer = new TestOptimizer();
        // Create a multi-start optimizer and set its max evaluations
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        // Optimize the objective function
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        // Verify that the optima array is not null and has the correct length
        assertNotNull(optima);
        assertEquals(3, optima.length);
        // Verify that the first element of the optima array is the best point found
        assertEquals(result, optima[0]);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithNoConvergence() {
        // Create a test instance of BaseUnivariateRealOptimizer with ConvergenceException
        BaseUnivariateRealOptimizer<FUNC> optimizer = new NoConvergenceOptimizer();
        // Create a multi-start optimizer and set its max evaluations
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        // Optimize the objective function, should throw ConvergenceException
        multiStartOptimizer.optimize(f, goal, min, max);
    }
    
    // Additional test methods for other public methods in MultiStartUnivariateRealOptimizer class
}
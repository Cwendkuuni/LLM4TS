import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {
    
    @Test
    public void testSetConvergenceChecker() {
        // Create a new optimizer and set the convergence checker
        BaseUnivariateRealOptimizer<FUNC> optimizer = new ConcreteUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConcreteConvergenceChecker<>();
        multiStartOptimizer.setConvergenceChecker(checker);
        
        // Assert that the convergence checker has been set correctly
        assertEquals(checker, multiStartOptimizer.getConvergenceChecker());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        // Create a new optimizer and set the max evaluations
        BaseUnivariateRealOptimizer<FUNC> optimizer = new ConcreteUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator());
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        
        // Assert that the max evaluations value has been set correctly
        assertEquals(maxEvaluations, multiStartOptimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        // Create a new optimizer and set the max evaluations
        BaseUnivariateRealOptimizer<FUNC> optimizer = new ConcreteUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator());
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        
        // Optimize the function and get the number of evaluations
        FUNC function = new ConcreteUnivariateRealFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        double startValue = 5;
        multiStartOptimizer.optimize(function, goal, min, max, startValue);
        int evaluations = multiStartOptimizer.getEvaluations();
        
        // Assert that the number of evaluations is greater than 0 and less than or equal to the max evaluations
        assertTrue(evaluations > 0);
        assertTrue(evaluations <= maxEvaluations);
    }
    
    @Test
    public void testGetOptima() {
        // Create a new optimizer and set the max evaluations
        BaseUnivariateRealOptimizer<FUNC> optimizer = new ConcreteUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator());
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        
        // Optimize the function and get the optima
        FUNC function = new ConcreteUnivariateRealFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        double startValue = 5;
        multiStartOptimizer.optimize(function, goal, min, max, startValue);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        
        // Assert that the optima array is not null and has the correct length
        assertNotNull(optima);
        assertEquals(multiStartOptimizer.getStarts(), optima.length);
    }
    
    @Test
    public void testOptimize() {
        // Create a new optimizer and set the max evaluations
        BaseUnivariateRealOptimizer<FUNC> optimizer = new ConcreteUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator());
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        
        // Optimize the function and get the best point
        FUNC function = new ConcreteUnivariateRealFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        double startValue = 5;
        UnivariateRealPointValuePair bestPoint = multiStartOptimizer.optimize(function, goal, min, max, startValue);
        
        // Assert that the best point is not null and has the correct value
        assertNotNull(bestPoint);
        assertEquals(0, bestPoint.getValue(), 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() {
        // Create a new optimizer and set the max evaluations
        BaseUnivariateRealOptimizer<FUNC> optimizer = new ConcreteUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator());
        int maxEvaluations = 1;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        
        // Optimize the function with a small number of max evaluations and expect a convergence exception
        FUNC function = new ConcreteUnivariateRealFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;
        double startValue = 5;
        multiStartOptimizer.optimize(function, goal, min, max, startValue);
    }
}
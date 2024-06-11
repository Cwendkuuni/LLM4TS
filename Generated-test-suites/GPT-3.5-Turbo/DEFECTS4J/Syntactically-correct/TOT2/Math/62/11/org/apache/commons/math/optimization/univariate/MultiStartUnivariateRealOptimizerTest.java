import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.Sin;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        // Create a sine function for optimization
        Sin sinFunction = new Sin();
        
        // Create a MultiStartUnivariateRealOptimizer with BisectionOptimizer and 5 starts
        MultiStartUnivariateRealOptimizer<Sin> optimizer = new MultiStartUnivariateRealOptimizer<>(new BisectionOptimizer(), 5, new Well1024a());
        
        // Optimize the sine function to find the minimum between 0 and 2pi
        UnivariateRealPointValuePair result = optimizer.optimize(sinFunction, GoalType.MINIMIZE, 0, 2 * Math.PI);
        
        // Check if the result is not null
        assertNotNull(result);
        
        // Check if the optimized value is close to the actual minimum value of the sine function
        assertEquals(-1.0, result.getValue(), 0.01);
    }
    
    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        // Create a sine function for optimization
        Sin sinFunction = new Sin();
        
        // Create a MultiStartUnivariateRealOptimizer with BisectionOptimizer and 3 starts
        MultiStartUnivariateRealOptimizer<Sin> optimizer = new MultiStartUnivariateRealOptimizer<>(new BisectionOptimizer(), 3, new Well1024a());
        
        // Optimize the sine function to find the maximum between 0 and 2pi
        UnivariateRealPointValuePair result = optimizer.optimize(sinFunction, GoalType.MAXIMIZE, 0, 2 * Math.PI);
        
        // Get the list of optima found during optimization
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        
        // Check if the optima array is not null
        assertNotNull(optima);
        
        // Check if the number of optima matches the number of starts
        assertEquals(3, optima.length);
        
        // Check if the best result is stored at the first index
        assertEquals(result, optima[0]);
    }

    // Add more test cases for other public methods

}
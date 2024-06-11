import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.util.DefaultRandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        QuinticFunction quintic = new QuinticFunction();
        
        RandomGenerator generator = new DefaultRandomGenerator();
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = 
            new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 5, generator);
        
        try {
            optimizer.setConvergenceChecker(null);
            optimizer.setMaxEvaluations(1000);
            optimizer.optimize(quintic, GoalType.MINIMIZE, -10.0, 10.0);
            
            UnivariateRealPointValuePair[] optima = optimizer.getOptima();
            assertNotNull(optima);
            assertTrue(optima.length == 5);
            
            for (UnivariateRealPointValuePair pair : optima) {
                if (pair != null) {
                    assertTrue(pair.getValue() >= 0.0);
                }
            }
            
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    // Add more test cases for other methods as needed

}
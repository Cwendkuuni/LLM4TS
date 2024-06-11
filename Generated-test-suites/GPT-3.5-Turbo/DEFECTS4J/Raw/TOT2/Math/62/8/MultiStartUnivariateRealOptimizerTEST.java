import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.Sin;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.FunctionEvaluationException;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        MultiStartUnivariateRealOptimizer<Sin> optimizer = new MultiStartUnivariateRealOptimizer<>(new Sin(), 5, new RandomGenerator());

        try {
            UnivariateRealPointValuePair result = optimizer.optimize(new Sin(), GoalType.MINIMIZE, -10, 10, 0);
            assertNotNull(result);
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown unexpectedly");
        }
    }

    @Test
    public void testNegativeEvaluations() {
        MultiStartUnivariateRealOptimizer<Sin> optimizer = new MultiStartUnivariateRealOptimizer<>(new Sin(), 5, new RandomGenerator());
        
        try {
            optimizer.optimize(new Sin(), GoalType.MINIMIZE, -10, -5, 0);
            fail("FunctionEvaluationException expected for negative evaluations, but not thrown");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testConvergence() {
        MultiStartUnivariateRealOptimizer<Sin> optimizer = new MultiStartUnivariateRealOptimizer<>(new Sin(), 5, new RandomGenerator());
        
        try {
            optimizer.optimize(new Sin(), GoalType.MINIMIZE, -5, 5, 0);
            fail("ConvergenceException expected, but not thrown");
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    // Add more test cases for other methods as needed

}
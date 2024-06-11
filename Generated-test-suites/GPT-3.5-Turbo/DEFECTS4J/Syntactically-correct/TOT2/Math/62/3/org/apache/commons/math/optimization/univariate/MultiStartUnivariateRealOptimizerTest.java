import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimizeWithQuinticFunction() {
        QuinticFunction quintic = new QuinticFunction();
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 5, new JDKRandomGenerator());
        try {
            optimizer.setMaxEvaluations(100);
            UnivariateRealPointValuePair result = optimizer.optimize(quintic, GoalType.MINIMIZE, -10, 10);
            assertNotNull(result);
            assertEquals(0.0, result.getValue(), 1e-6);
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation exception occurred");
        }
    }

    @Test
    public void testOptimizeWithNullFunction() {
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 5, new JDKRandomGenerator());
        try {
            optimizer.setMaxEvaluations(100);
            UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MAXIMIZE, -10, 10);
            assertNull(result);
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation exception occurred");
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testMaxEvaluationsExceeded() throws FunctionEvaluationException {
        QuinticFunction quintic = new QuinticFunction();
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 5, new JDKRandomGenerator());
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(quintic, GoalType.MAXIMIZE, -10, 10);
    }

}
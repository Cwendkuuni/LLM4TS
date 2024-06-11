import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        QuinticFunction quintic = new QuinticFunction();
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 5, new JDKRandomGenerator());

        try {
            UnivariateRealPointValuePair result = optimizer.optimize(quintic, GoalType.MINIMIZE, 0.0, 1.0);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't");
        }
    }

    @Test
    public void testGetOptima() {
        QuinticFunction quintic = new QuinticFunction();
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 3, new JDKRandomGenerator());

        try {
            UnivariateRealPointValuePair[] optima = optimizer.getOptima();
            assertEquals(3, optima.length);
        } catch (MathIllegalStateException e) {
            fail("Exception thrown when it shouldn't");
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithInvalidBounds() throws FunctionEvaluationException {
        QuinticFunction quintic = new QuinticFunction();
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 1, new JDKRandomGenerator());

        optimizer.optimize(quintic, GoalType.MINIMIZE, 1.0, 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaBeforeOptimize() {
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 3, new JDKRandomGenerator());
        optimizer.getOptima();
    }
}
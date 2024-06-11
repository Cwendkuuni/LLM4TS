import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.MersenneTwister;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.analysis.Sin;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize_withValidInputs() {
        MultiStartUnivariateRealOptimizer<Sin> optimizer = new MultiStartUnivariateRealOptimizer<>(new Sin(), 3, new MersenneTwister());
        try {
            UnivariateRealPointValuePair result = optimizer.optimize(new Sin(), GoalType.MINIMIZE, 0.0, 2 * Math.PI);
            assertNotNull(result);
        } catch (FunctionEvaluationException e) {
            fail("Exception should not be thrown for valid inputs.");
        }
    }

    @Test
    public void testOptimize_withInvalidBounds() {
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new QuinticFunction(), 3, new MersenneTwister());
        try {
            UnivariateRealPointValuePair result = optimizer.optimize(new QuinticFunction(), GoalType.MAXIMIZE, 5.0, 1.0);
            fail("Exception should be thrown for invalid input bounds.");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetOptima_beforeOptimize() {
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new QuinticFunction(), 3, new MersenneTwister());
        assertThrows(MathIllegalStateException.class, optimizer::getOptima);
    }

    @Test
    public void testGetOptima_afterOptimize() {
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(new QuinticFunction(), 3, new MersenneTwister());
        try {
            optimizer.optimize(new QuinticFunction(), GoalType.MAXIMIZE, -5.0, 5.0);
            UnivariateRealPointValuePair[] optima = optimizer.getOptima();
            assertNotNull(optima);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Exception should not be thrown when retrieving optima after optimization.");
        }
    }
}
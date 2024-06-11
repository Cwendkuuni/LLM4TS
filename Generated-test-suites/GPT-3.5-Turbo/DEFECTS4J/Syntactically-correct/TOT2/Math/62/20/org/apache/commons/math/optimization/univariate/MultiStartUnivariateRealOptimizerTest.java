import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.random.Well19937c;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        QuinticFunction quintic = new QuinticFunction();
        RandomGenerator generator = new Well19937c(12345);
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(),
                3, generator);

        try {
            optimizer.setMaxEvaluations(1000);
            optimizer.setConvergenceChecker(new SimpleUnivariateRealPointChecker<>());

            double result = optimizer.optimize(quintic, GoalType.MAXIMIZE, -1.0, 1.0).getValue();

            assertTrue(result > 0.0); // Typical use case

            UnivariateRealPointValuePair[] optima = optimizer.getOptima();
            assertNotNull(optima);
            assertEquals(3, optima.length);

        } catch (FunctionEvaluationException e) {
            fail("Function evaluation exception occurred");
        } catch (ConvergenceException e) {
            fail("Convergence exception occurred");
        }
    }

    @Test
    public void testOptimizeWithInvalidBounds() {
        QuinticFunction quintic = new QuinticFunction();
        RandomGenerator generator = new Well19937c(12345);
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(),
                3, generator);

        try {
            optimizer.setMaxEvaluations(1000);
            optimizer.setConvergenceChecker(new SimpleUnivariateRealPointChecker<>());

            optimizer.optimize(quintic, GoalType.MINIMIZE, 1.0, -1.0); // Invalid bounds

            fail("Expected IllegalArgumentException for invalid bounds");

        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testOptimizeWithNoOptimum() {
        QuinticFunction quintic = new QuinticFunction();
        RandomGenerator generator = new Well19937c(12345);
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(),
                3, generator);

        try {
            optimizer.setMaxEvaluations(10);
            optimizer.setConvergenceChecker(new SimpleUnivariateRealPointChecker<>());

            optimizer.optimize(quintic, GoalType.MINIMIZE, -1.0, 1.0); // Not enough evaluations
            optimizer.getOptima(); // Try to retrieve optima without calling optimize

            fail("Expected MathIllegalStateException for no optimum computed yet");

        } catch (MathIllegalStateException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }
}
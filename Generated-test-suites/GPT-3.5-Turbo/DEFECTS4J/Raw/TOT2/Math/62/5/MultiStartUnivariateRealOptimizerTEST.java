import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.random.JDKRandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        QuinticFunction quintic = new QuinticFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<QuinticFunction>(
                new BrentOptimizer(), 5, new JDKRandomGenerator());
        optimizer.setMaxEvaluations(1000);
        
        try {
            optimizer.optimize(quintic, goal, min, max);
            UnivariateRealPointValuePair[] optima = optimizer.getOptima();

            assertNotNull(optima);
            assertEquals(5, optima.length);

            for (UnivariateRealPointValuePair pair : optima) {
                if (pair != null) {
                    assertTrue(pair.getValue() >= quintic.value(pair.getPoint()));
                }
            }
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetOptimaWhenNotOptimized() {
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<QuinticFunction>(
                new BrentOptimizer(), 5, new JDKRandomGenerator());

        try {
            optimizer.getOptima();
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // This is expected
        }
    }

    @Test
    public void testMaxEvaluations() {
        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer = new MultiStartUnivariateRealOptimizer<QuinticFunction>(
                new BrentOptimizer(), 5, new JDKRandomGenerator());

        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }
}
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.Well19937a;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimizeTypicalCase() throws Exception {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 5, new Well19937a(1000L));
        UnivariateRealPointValuePair result = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        }, GoalType.MINIMIZE, 0.0, 10.0);
        assertEquals(2.0, result.getPoint(), 1e-5);
    }

    @Test
    public void testOptimizeNoConvergence() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 5, new Well19937a(1000L));
        try {
            optimizer.optimize(new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4 * x + 4;
                }
            }, GoalType.MINIMIZE, 0.0, 10.0, 15.0);
            fail("ConvergenceException expected");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testOptimizeFunctionEvaluationException() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 5, new Well19937a(1000L));
        try {
            optimizer.optimize(new UnivariateRealFunction() {
                public double value(double x) {
                    throw new FunctionEvaluationException(x);
                }
            }, GoalType.MINIMIZE, 0.0, 10.0);
            fail("FunctionEvaluationException expected");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetOptimaNoOptimumComputed() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 5, new Well19937a(1000L));
        try {
            optimizer.getOptima();
            fail("MathIllegalStateException expected");
        } catch (MathIllegalStateException e) {
            // Expected exception
        }
    }
}
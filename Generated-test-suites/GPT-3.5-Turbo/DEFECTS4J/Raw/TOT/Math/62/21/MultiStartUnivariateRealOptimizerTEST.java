import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize_minimize() throws FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator());

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        double minValue = result.getValue();

        assertEquals(0.0, minValue, 0.001);
    }

    @Test
    public void testOptimize_maximize() throws FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return -1 * x * x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator());

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, GoalType.MAXIMIZE, -10, 10);
        double maxValue = result.getValue();

        assertEquals(0.0, maxValue, 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_error() throws FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(2);
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator());

        multiStartOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }

}
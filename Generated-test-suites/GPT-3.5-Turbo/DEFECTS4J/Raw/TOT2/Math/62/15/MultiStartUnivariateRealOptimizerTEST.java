import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 5, new RandomGenerator());

        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 10).getValue();

        assertEquals(0.0, result, 0.1);
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 2) * (x - 2);
            }
        };

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 3, new RandomGenerator());

        optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);

        assertNotNull(optimizer.getOptima());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                if (x < 0) {
                    throw new FunctionEvaluationException(x);
                }
                return 1;
            }
        };

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 2, new RandomGenerator());

        optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartsParameter() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 0, new RandomGenerator());
    }

    // Additional tests for edge cases and exceptions can be added here

}
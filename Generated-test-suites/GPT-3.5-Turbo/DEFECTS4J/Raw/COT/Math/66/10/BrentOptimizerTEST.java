
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

import org.junit.Test;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer = new BrentOptimizer();

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 2, 2);
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 1, 3, 2);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 2, 2);
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 1, 3);
        assertEquals(2.0, result, 0.001);
    }
}

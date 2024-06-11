import org.junit.Test;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer = new BrentOptimizer();

    @Test
    public void testOptimize_minimize() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testOptimize_maximize() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return -x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10, 0);
        assertEquals(0.0, result, 0.001);
    }
}
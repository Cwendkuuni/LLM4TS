import org.junit.Test;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

public class BrentOptimizerTest {
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // Sample function
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
        assert(result >= 0 && result <= 10);
    }
}
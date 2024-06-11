import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 3);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 2, 4, 1E-10, 1E-14);
        assertEquals(2.0, result, 0.001);
    }
}
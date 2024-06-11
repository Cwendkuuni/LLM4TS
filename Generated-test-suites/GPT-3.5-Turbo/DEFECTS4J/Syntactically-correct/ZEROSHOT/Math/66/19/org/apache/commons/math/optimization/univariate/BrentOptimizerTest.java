import org.junit.Test;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer = new BrentOptimizer();
    
    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 5);
            assertEquals(0.0, result, 0.000001);
        } catch (MaxIterationsExceededException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testOptimizeWithoutStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
            assertEquals(0.0, result, 0.000001);
        } catch (MaxIterationsExceededException e) {
            e.printStackTrace();
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }
}
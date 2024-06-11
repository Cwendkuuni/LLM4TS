import org.junit.Test;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, GoalType.MINIMIZE, 0, 1, 0.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, GoalType.MINIMIZE, 0, 1);
    }

    @Test
    public void testLocalMinValid() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, 0, 1);
        Assert.assertEquals(0, result, 1E-10);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.doOptimize();
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
}
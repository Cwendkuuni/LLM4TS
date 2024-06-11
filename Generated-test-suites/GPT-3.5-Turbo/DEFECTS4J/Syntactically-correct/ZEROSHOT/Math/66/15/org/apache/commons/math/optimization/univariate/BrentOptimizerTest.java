import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer brent = new BrentOptimizer();
        brent.doOptimize();
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer brent = new BrentOptimizer();
        UnivariateRealFunction f = null;
        GoalType goalType = GoalType.MINIMIZE;
        double lo = 0;
        double mid = 0;
        double hi = 0;
        double eps = 0;
        double t = 0;
        brent.localMin(false, f, goalType, lo, mid, hi, eps, t);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer brent = new BrentOptimizer();
        UnivariateRealFunction f = null;
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 0;
        double startValue = 0;
        brent.optimize(f, goalType, min, max, startValue);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithNoStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer brent = new BrentOptimizer();
        UnivariateRealFunction f = null;
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 0;
        brent.optimize(f, goalType, min, max);
    }
}
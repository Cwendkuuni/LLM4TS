import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new SimpleValueChecker(0.001, 0.001);
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, checker);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.optimize(1000);

        // Assert expected result
        // ...
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 2);
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);

        UnivariatePointValuePair result = optimizer.best(a, b, true);

        // Assert expected result
        // ...
    }
}
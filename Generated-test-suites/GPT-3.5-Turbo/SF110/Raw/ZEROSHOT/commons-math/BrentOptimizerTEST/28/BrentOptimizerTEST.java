import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        double min = 0.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        optimizer.optimize(max, goalType);
        // Make assertions here to test the optimize() method
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair result = optimizer.doOptimize();
        // Make assertions here to test the doOptimize() method
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        boolean isMinim = true;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        // Make assertions here to test the best() method
    }
}
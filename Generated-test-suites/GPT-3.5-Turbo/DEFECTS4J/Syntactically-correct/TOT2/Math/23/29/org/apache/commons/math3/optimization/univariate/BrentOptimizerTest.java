import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testBestMethod() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        boolean isMinim = true;

        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair best = optimizer.best(a, b, isMinim);

        assertEquals(b, best);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.1);
        assertEquals(0.0, result.getValue(), 0.1);
    }

    // Add more test methods for other public methods in BrentOptimizer class

}
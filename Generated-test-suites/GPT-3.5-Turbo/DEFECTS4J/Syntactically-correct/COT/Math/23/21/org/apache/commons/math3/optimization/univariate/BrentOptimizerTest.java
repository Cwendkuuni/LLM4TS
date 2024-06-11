import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);

        // Test case 1
        double expected1 = 3.0;
        double actual1 = optimizer.doOptimize();
        assertEquals(expected1, actual1, 0.0001);

        // Test case 2
        double expected2 = -2.5;
        double actual2 = optimizer.doOptimize();
        assertEquals(expected2, actual2, 0.0001);

        // Test case 3
        double expected3 = 0;
        double actual3 = optimizer.doOptimize();
        assertEquals(expected3, actual3, 0.0001);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);

        // Test case 1
        UnivariatePointValuePair a1 = new UnivariatePointValuePair(0, 5.0);
        UnivariatePointValuePair b1 = new UnivariatePointValuePair(1, 3.0);
        UnivariatePointValuePair expected1 = a1;
        UnivariatePointValuePair actual1 = optimizer.best(a1, b1, true);
        assertEquals(expected1, actual1);

        // Test case 2
        UnivariatePointValuePair a2 = new UnivariatePointValuePair(0, 3.0);
        UnivariatePointValuePair b2 = new UnivariatePointValuePair(1, 5.0);
        UnivariatePointValuePair expected2 = a2;
        UnivariatePointValuePair actual2 = optimizer.best(a2, b2, true);
        assertEquals(expected2, actual2);

        // Test case 3
        UnivariatePointValuePair a3 = new UnivariatePointValuePair(0, 5.0);
        UnivariatePointValuePair b3 = new UnivariatePointValuePair(1, 5.0);
        UnivariatePointValuePair expected3 = a3;
        UnivariatePointValuePair actual3 = optimizer.best(a3, b3, true);
        assertEquals(expected3, actual3);
    }
}
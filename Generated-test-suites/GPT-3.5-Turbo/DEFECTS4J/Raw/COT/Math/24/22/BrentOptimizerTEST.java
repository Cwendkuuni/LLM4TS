import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);

        // Set the objective function
        optimizer.setObjectiveFunction(new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        });

        // Optimize and check the result
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }
}
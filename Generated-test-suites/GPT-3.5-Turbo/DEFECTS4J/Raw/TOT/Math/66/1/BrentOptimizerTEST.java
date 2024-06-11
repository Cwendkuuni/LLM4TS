import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    
    @Before
    public void setup() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize_WithValidMinMax_ExpectedOutput() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
            assertEquals(0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testOptimize_WithInvalidMax_ThrowsException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, -20);
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NotStrictlyPositiveException);
        }
    }

    // Add more test cases for other methods here...

}
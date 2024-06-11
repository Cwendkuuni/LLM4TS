import static org.junit.Assert.*;

import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize_WithMinValue() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        double result = optimizer.optimize(f, GoalType.MINIMIZE, 1, 3);

        assertEquals(2, result, 1E-15);
    }

    @Test
    public void testOptimize_WithMaxValue() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        double result = optimizer.optimize(f, GoalType.MAXIMIZE, 1, 3);

        assertEquals(3, result, 1E-15);
    }

    @Test
    public void testOptimize_WithMinValue_WhenMinEqualsMax() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        double result = optimizer.optimize(f, GoalType.MINIMIZE, 2, 2);

        assertEquals(2, result, 1E-15);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_WithInvalidEpsValue() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        optimizer.optimize(f, GoalType.MINIMIZE, 1, 3, -0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_WithInvalidTValue() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };

        optimizer.optimize(f, GoalType.MINIMIZE, 1, 3, 0.1, -1);
    }

}
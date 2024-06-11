import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {
    private double epsilon = 1e-6;

    @Test
    public void testDoOptimize_minimize() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(epsilon, epsilon);
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);

        assertTrue(result.getValue() < epsilon);
        assertEquals(0, result.getPoint(), epsilon);
    }

    @Test
    public void testDoOptimize_maximize() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return -x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(epsilon, epsilon);
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10);

        assertTrue(-result.getValue() < epsilon);
        assertEquals(0, result.getPoint(), epsilon);
    }
}
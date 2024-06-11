import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);

        // Test a simple function
        double[] coef = new double[]{1, -2, -3}; // x^2 - 2x - 3
        UnivariatePointValuePair result = optimizer.optimize(100, (x) -> {
            double fx = 0;
            for (int i = 0; i < coef.length; i++) {
                fx += coef[i] * Math.pow(x, coef.length - 1 - i);
            }
            return fx;
        }, GoalType.MINIMIZE, 0.0, 10.0);

        assertEquals(-1.0, result.getPoint(), 1e-8);
        assertEquals(-4.0, result.getValue(), 1e-8);

        // Test function with multiple local minima
        double[] coef2 = new double[]{1, -2, -3, 2, -1}; // x^4 - 2x^3 - 3x^2 + 2x - 1
        result = optimizer.optimize(100, (x) -> {
            double fx = 0;
            for (int i = 0; i < coef2.length; i++) {
                fx += coef2[i] * Math.pow(x, coef2.length - 1 - i);
            }
            return fx;
        }, GoalType.MINIMIZE, 0.0, 10.0);

        assertEquals(3.0, result.getPoint(), 1e-8);
        assertEquals(-25.0, result.getValue(), 1e-8);

        // Test function with minimum at boundary
        double[] coef3 = new double[]{-2, 10}; // -2x + 10
        result = optimizer.optimize(100, (x) -> {
            double fx = 0;
            for (int i = 0; i < coef3.length; i++) {
                fx += coef3[i] * Math.pow(x, coef3.length - 1 - i);
            }
            return fx;
        }, GoalType.MINIMIZE, 0.0, 10.0);

        assertEquals(5.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }
}
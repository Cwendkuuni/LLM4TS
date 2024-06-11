import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        double[] x1 = new double[]{1, 2, 3, 4, 5};
        double[] y1 = new double[]{1, 4, 9, 16, 25};
        UnivariatePointValuePair result1 = optimizer.optimize(1000, (i) -> new UnivariatePointValuePair(x1[i], y1[i]));
        assertEquals(5, result1.getPoint(), 1e-6);
        assertEquals(25, result1.getValue(), 1e-6);

        double[] x2 = new double[]{-5, -4, -3, -2, -1};
        double[] y2 = new double[]{25, 16, 9, 4, 1};
        UnivariatePointValuePair result2 = optimizer.optimize(1000, (i) -> new UnivariatePointValuePair(x2[i], y2[i]));
        assertEquals(-5, result2.getPoint(), 1e-6);
        assertEquals(25, result2.getValue(), 1e-6);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 100);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 200);

        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);

        assertEquals(a, result1);
        assertEquals(b, result2);
    }
}
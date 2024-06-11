import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.optimization.univariate.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        double rel = 0.5;
        double abs = 0.1;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbs() {
        double rel = 0.5;
        double abs = 0.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRel() {
        double rel = 1e-16;
        double abs = 0.1;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testDoOptimize() {
        double rel = 0.5;
        double abs = 0.1;
        double lo = -10.0;
        double hi = 10.0;
        double startValue = 0.0;
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.optimize(function, GoalType.MINIMIZE, lo, hi, startValue);

        assertEquals(0.0, optimizer.getResult(), 0.0);
    }

    @Test
    public void testDoOptimizeNullChecker() {
        double rel = 0.5;
        double abs = 0.1;
        double lo = -10.0;
        double hi = 10.0;
        double startValue = 0.0;
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        optimizer.optimize(function, GoalType.MINIMIZE, lo, hi, startValue);

        assertEquals(0.0, optimizer.getResult(), 0.0);
    }

    @Test
    public void testDoOptimizeNullCheckerNoConvergence() {
        double rel = 0.5;
        double abs = 0.1;
        double lo = -10.0;
        double hi = 10.0;
        double startValue = 0.0;
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(function, GoalType.MINIMIZE, lo, hi, startValue);

        assertEquals(0.0, optimizer.getResult(), 0.0);
    }

    @Test
    public void testDoOptimizeMaxIterations() {
        double rel = 0.5;
        double abs = 0.1;
        double lo = -10.0;
        double hi = 10.0;
        double startValue = 0.0;
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMaxIterations(5);
        optimizer.optimize(function, GoalType.MINIMIZE, lo, hi, startValue);

        assertEquals(0.0, optimizer.getResult(), 0.0);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBestNullA() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(b, result);
    }

    @Test
    public void testBestNullB() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = null;
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBestEqualValues() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }
}
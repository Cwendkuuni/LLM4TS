import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinNegativeEpsilon() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        double eps = -1.0;
        double t = 0.1;
        optimizer.localMin(false, null, null, lo, mid, hi, eps, t);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinNegativeTolerance() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        double eps = 0.1;
        double t = -1.0;
        optimizer.localMin(false, null, null, lo, mid, hi, eps, t);
    }

    @Test
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        double eps = 1E-6;
        double t = 0.1;
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, lo, mid, hi, eps, t);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        double min = 0.0;
        double max = 2.0;
        double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
        assertEquals(2.0, result, 1E-6);
    }
}
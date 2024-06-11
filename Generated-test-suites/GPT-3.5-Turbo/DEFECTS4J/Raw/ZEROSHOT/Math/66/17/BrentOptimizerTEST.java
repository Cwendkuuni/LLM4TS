import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeMinMax() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) + Math.cos(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 2*Math.PI);
        assertEquals(-1.082, result, 0.001);
        result = optimizer.optimize(f, GoalType.MAXIMIZE, 0, 2*Math.PI);
        assertEquals(1.082, result, 0.001);
    }

    @Test
    public void testOptimize() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 3*x*x - 4*x + 2;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 2);
        assertEquals(1.333, result, 0.001);

        result = optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10, 2);
        assertEquals(3.333, result, 0.001);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testDoOptimize() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testLocalMinEpsZero() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(false, null, GoalType.MINIMIZE, 0, 1, 2, 0, 1);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testLocalMinTZero() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(false, null, GoalType.MINIMIZE, 0, 1, 2, 1, 0);
    }

    @Test(expected=MaxIterationsExceededException.class)
    public void testLocalMinMaxIterationsExceeded() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(10);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 0.5, 1, 0.001, 0.001);
    }
}
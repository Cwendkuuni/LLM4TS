import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimize_withStartValue_Success() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, -5);
        assertEquals(0, result, 1E-10);
    }

    @Test
    public void testOptimize_withoutStartValue_Success() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
        assertEquals(0, result, 1E-10);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_MaxIterationsExceededException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.setMaximalIterationCount(100);
        optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
    }

    @Test
    public void testOptimize_withMaxIterations_Success() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.setMaximalIterationCount(10000);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
        assertEquals(0, result, 1E-10);
    }

    @Test
    public void testOptimize_withRelativeAccuracy_Success() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.setRelativeAccuracy(1E-5);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
        assertEquals(0, result, 1E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_NotStrictlyPositiveException_Eps() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, -5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_NotStrictlyPositiveException_T() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
    }
}
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void optimize_withStartValue() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result1 = optimizer.optimize(f1, GoalType.MINIMIZE, -2, 2, -1);
        assertEquals(0.0, result1, 1e-10);

        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double result2 = optimizer.optimize(f2, GoalType.MAXIMIZE, 0, 3.14, 1.57);
        assertEquals(1.57, result2, 1e-10);
    }

    @Test
    public void optimize_withoutStartValue() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result1 = optimizer.optimize(f1, GoalType.MINIMIZE, -2, 2);
        assertEquals(0.0, result1, 1e-10);

        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double result2 = optimizer.optimize(f2, GoalType.MAXIMIZE, 0, 3.14);
        assertEquals(1.57, result2, 1e-10);
    }

    @Test(expected = org.apache.commons.math.MaxIterationsExceededException.class)
    public void optimize_exceedMaxIterations() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(5);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        optimizer.optimize(f, GoalType.MINIMIZE, -2, 2);
    }

    @Test(expected = org.apache.commons.math.exception.NotStrictlyPositiveException.class)
    public void localMin_invalidEps() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, -2, 0, 2, -1, 1e-10);
    }

    @Test(expected = org.apache.commons.math.exception.NotStrictlyPositiveException.class)
    public void localMin_invalidT() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.localMin(true, f, GoalType.MINIMIZE, -2, 0, 2, 1e-10, -1);
    }
}
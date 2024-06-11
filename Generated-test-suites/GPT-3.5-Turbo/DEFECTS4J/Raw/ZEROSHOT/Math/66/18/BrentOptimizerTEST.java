import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.cos(x);
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 2 * Math.PI);
        assertEquals(3 * Math.PI / 2, result, 1E-10);
    }

    @Test
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.cos(x);
            }
        };
        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, Math.PI / 2, Math.PI, 1.0e-14, 1E-10);
        assertEquals(Math.PI / 2, result, 1E-10);
    }

}
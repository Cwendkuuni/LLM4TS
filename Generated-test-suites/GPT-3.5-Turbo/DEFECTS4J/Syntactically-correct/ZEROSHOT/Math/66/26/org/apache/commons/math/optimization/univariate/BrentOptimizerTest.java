import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        
        BrentOptimizer optimizer = new BrentOptimizer();
        
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
            assertEquals(0.0, result, 1e-10);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException");
        }
    }

    @Test
    public void testOptimize2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            assertEquals(5.0, result, 1e-10);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException");
        }
    }

    @Test
    public void testOptimize3() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return -x + 5;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            double result = optimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
            assertEquals(5.0, result, 1e-10);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException");
        }
    }
}
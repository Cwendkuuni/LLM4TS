import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    @Test
    public void testOptimize_StartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 5);
            assertEquals(0.0, result, 1e-10);
        } catch (Exception e) {
            fail("An exception occurred during optimization");
        }
    }

    @Test
    public void testOptimize_NoStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
            assertEquals(0.0, result, 1e-10);
        } catch (Exception e) {
            fail("An exception occurred during optimization");
        }
    }

    @Test
    public void testLocalMin_Exception() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                if (x < 0) {
                    throw new NotStrictlyPositiveException(x);
                }
                return x;
            }
        };
        try {
            double result = optimizer.localMin(true, f, GoalType.MINIMIZE, -5, 0, 5, 1e-10, 1e-5);
            assertEquals(0.0, result, 1e-10);
        } catch (Exception e) {
            fail("An exception occurred during local minimum search");
        }
    }

}
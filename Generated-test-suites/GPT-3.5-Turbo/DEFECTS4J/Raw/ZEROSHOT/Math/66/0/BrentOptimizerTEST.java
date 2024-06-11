import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }
    
    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
        assertEquals(0.0, result, 1e-10);
    }
    
    @Test
    public void testOptimizeWithoutStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0.0, result, 1e-10);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithInvalidEps() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.5, -1.0, 0.5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithInvalidT() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.5, 0.5, -1.0);
    }
}
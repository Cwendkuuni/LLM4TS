import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.lang.reflect.Field;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }
    
    @Test
    public void testConstructor() {
        assertEquals(Integer.MAX_VALUE, getField("maxEvaluations"));
        assertEquals(100, getField("maximalIterationCount"));
        assertEquals(1E-10, getField("absoluteAccuracy"));
        assertEquals(1.0e-14, getField("relativeAccuracy"));
    }
    
    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 5;
        double result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(0, result, 0.0001);
    }
    
    @Test
    public void testOptimizeWithoutStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double result = optimizer.optimize(f, goalType, min, max);
        assertEquals(0, result, 0.0001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeEps() {
        try {
            optimizer.optimize(null, GoalType.MINIMIZE, 0, 0, 0);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("2.0 is not strictly positive.", e.getMessage());
            throw e;
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithNegativeT() {
        try {
            optimizer.optimize(null, GoalType.MINIMIZE, 0, 0, 0);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("2.0 is not strictly positive.", e.getMessage());
            throw e;
        }
    }
    
    private double getField(String fieldName) {
        try {
            Field field = optimizer.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (double) field.get(optimizer);
        } catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }
}
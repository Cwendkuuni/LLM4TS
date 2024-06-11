import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testConstructorException() {
        double rel = 0.001;
        double abs = -0.01;
        
        try {
            BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStartValue(5.0);
        
        // Test a typical use case
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(0.0, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
        
        // Test edge case where function value is negative
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.optimize();
        assertEquals(10.0, result.getPoint(), 0.01);
        assertEquals(-10.0, result.getValue(), 0.01);
    }

    @Test
    public void testDoOptimizeException() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        
        // Test an error scenario of invalid search bounds
        optimizer.setMin(10.0);
        optimizer.setMax(0.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        try {
            optimizer.optimize();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
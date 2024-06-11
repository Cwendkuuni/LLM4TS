import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        // Test constructor with convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutChecker() {
        // Test constructor without convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimize() {
        // Test the optimize method
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);

        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testBest() {
        // Test the best method
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);

        UnivariatePointValuePair result = optimizer.best(a, b, true);

        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(2.0, result.getValue(), 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidConstructorArguments() {
        // Test constructor with invalid arguments
        new BrentOptimizer(0.0, 0.001);
    }
}
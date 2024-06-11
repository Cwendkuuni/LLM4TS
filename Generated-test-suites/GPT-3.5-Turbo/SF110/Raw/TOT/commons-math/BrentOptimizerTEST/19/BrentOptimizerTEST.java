import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_Minimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals("Incorrect minimum value", 0.0, result.getValue(), 1e-12);
        assertEquals("Incorrect minimum point", 0.0, result.getPoint(), 1e-12);
    }

    @Test
    public void testDoOptimize_Maximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals("Incorrect maximum value", 14.0, result.getValue(), 1e-12);
        assertEquals("Incorrect maximum point", 10.0, result.getPoint(), 1e-12);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelTooSmall() {
        new BrentOptimizer(1e-12, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsNotPositive() {
        new BrentOptimizer(1e-6, 0);
    }

    @Test
    public void testConstructor_RelAndAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        assertEquals("Incorrect relative threshold", 1e-6, optimizer.getRelativeThreshold(), 1e-12);
        assertEquals("Incorrect absolute threshold", 1e-6, optimizer.getAbsoluteThreshold(), 1e-12);
    }

    @Test
    public void testBest_FirstIsNull() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(3.0, 3.0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertSame("Incorrect result", b, result);
    }

    @Test
    public void testBest_SecondIsNull() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = null;
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertSame("Incorrect result", a, result);
    }

    @Test
    public void testBest_IsMinim_True() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 0.0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertSame("Incorrect result", b, result);
    }

    @Test
    public void testBest_IsMinim_False() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        
        assertSame("Incorrect result", b, result);
    }
}
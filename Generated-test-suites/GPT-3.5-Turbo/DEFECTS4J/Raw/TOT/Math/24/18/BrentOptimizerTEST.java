import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    // Test constructor with valid inputs
    @Test
    public void testConstructor_ValidInputs() {
        double rel = 0.001;
        double abs = 0.01;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        assertNotNull(optimizer);
    }
    
    // Test constructor with negative relative threshold
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_NegativeRelativeThreshold() {
        double rel = -0.001;
        double abs = 0.01;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    // Test constructor with zero absolute threshold
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroAbsoluteThreshold() {
        double rel = 0.001;
        double abs = 0.0;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    // Test minimize function
    @Test
    public void testMinimize() {
        double rel = 0.001;
        double abs = 0.01;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> Math.pow(x - 2, 2));
        
        UnivariatePointValuePair result = optimizer.minimize(function, GoalType.MINIMIZE, 0, 4);
        
        assertNotNull(result);
        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }
    
    // Test maximize function
    @Test
    public void testMaximize() {
        double rel = 0.001;
        double abs = 0.01;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(Math::sqrt);
        
        UnivariatePointValuePair result = optimizer.maximize(function, GoalType.MAXIMIZE, 0, 4);
        
        assertNotNull(result);
        assertEquals(4.0, result.getPoint(), 0.001);
        assertEquals(2.0, result.getValue(), 0.001);
    }
    
}
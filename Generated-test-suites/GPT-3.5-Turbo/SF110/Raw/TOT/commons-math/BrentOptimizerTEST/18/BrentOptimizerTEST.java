import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.optim.ConvergenceChecker;

public class BrentOptimizerTest {
    
    // Test constructor with correct input values
    @Test
    public void testConstructorValidInput() {
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), Precision.EPSILON);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), Precision.EPSILON);
        assertNull(optimizer.getConvergenceChecker());
    }
    
    // Test constructor with negative relative threshold
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorNegativeRelThreshold() {
        double rel = -0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }
    
    // Test constructor with zero absolute threshold
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroAbsThreshold() {
        double rel = 0.01;
        double abs = 0.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }
    
    // Test doOptimize() method with valid input values
    @Test
    public void testDoOptimizeValidInput() {
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertTrue(Precision.equals(result.getPoint(), 5.0, Precision.EPSILON));
        assertTrue(Precision.equals(result.getValue(), 0.0, Precision.EPSILON));
    }
    
    // Test doOptimize() method with invalid input values
    @Test
    public void testDoOptimizeInvalidInput() {
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setMin(10.0);
        optimizer.setMax(0.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNull(result);
    }
}
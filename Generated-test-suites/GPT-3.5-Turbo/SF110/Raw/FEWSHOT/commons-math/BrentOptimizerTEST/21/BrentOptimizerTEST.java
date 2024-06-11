import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorRel() {
        new BrentOptimizer(-1.0, 0.0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorAbs() {
        new BrentOptimizer(1.0, 0.0);
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 1e-6);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(null);
        optimizer.setMaxIterations(100);
        optimizer.setStartValue(0.5);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        
        double x = result.getPoint();
        double fx = result.getValue();
        
        assertTrue(x >= 0.0 && x <= 1.0);
        assertTrue(fx >= -1.0 && fx <= 1.0);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 1e-6);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 0.4);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertSame(a, result);
        
        a = new UnivariatePointValuePair(0.0, 0.3);
        b = new UnivariatePointValuePair(1.0, 0.4);
        result = optimizer.best(a, b, true);
        assertSame(a, result);
        
        a = new UnivariatePointValuePair(0.0, 0.5);
        b = new UnivariatePointValuePair(1.0, 0.6);
        result = optimizer.best(a, b, true);
        assertSame(b, result);
        
        a = new UnivariatePointValuePair(0.0, 0.4);
        b = new UnivariatePointValuePair(1.0, 0.4);
        result = optimizer.best(a, b, true);
        assertSame(a, result);
        
        a = new UnivariatePointValuePair(0.0, 0.4);
        b = new UnivariatePointValuePair(1.0, 0.4);
        result = optimizer.best(a, b, false);
        assertSame(b, result);
    }
}
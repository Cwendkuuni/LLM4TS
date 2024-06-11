import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(0.0, result.getPoint(), 1e-8);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(b, result);
    }
}
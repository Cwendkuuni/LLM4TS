import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01, null);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testConstructorWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testDoOptimize() {
        // Test doOptimize method with typical values
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        // Test best method with typical values
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 20.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(10.0, result.getValue(), 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRel() {
        BrentOptimizer optimizer = new BrentOptimizer(-0.001, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDoOptimizeInvalidGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        optimizer.setGoalType(null);
        optimizer.doOptimize();
    }
    
}
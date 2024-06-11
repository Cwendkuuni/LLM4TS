import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 5.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 3.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(b, result);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorNegativeRelativeThreshold() {
        new BrentOptimizer(0.0, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNonPositiveAbsoluteThreshold() {
        new BrentOptimizer(0.001, 0.0);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}
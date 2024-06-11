import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructor() {
        double rel = 0.1;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testDoOptimize() {
        double rel = 0.1;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_withInvalidRelativeTolerance() {
        double rel = -0.1;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withInvalidAbsoluteTolerance() {
        double rel = 0.1;
        double abs = -0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

}
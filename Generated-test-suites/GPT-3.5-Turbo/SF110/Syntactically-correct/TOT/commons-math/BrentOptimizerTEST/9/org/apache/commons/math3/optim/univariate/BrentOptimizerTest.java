import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // test cases for constructor with rel, abs, checker parameters
    @Test(expected=NumberIsTooSmallException.class)
    public void testConstructor1_Exception1() {
        new BrentOptimizer(-0.1, 0.5, null);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructor1_Exception2() {
        new BrentOptimizer(0.1, 0, null);
    }
    
    @Test
    public void testConstructor1() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.1, 0.5, null);
        assertNotNull(brentOptimizer);
        assertEquals(0.1, brentOptimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.5, brentOptimizer.getAbsoluteThreshold(), 0.0001);
    }

    // test cases for constructor with rel, abs parameters
    @Test(expected=NumberIsTooSmallException.class)
    public void testConstructor2_Exception1() {
        new BrentOptimizer(-0.1, 0.5);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructor2_Exception2() {
        new BrentOptimizer(0.1, 0);
    }
    
    @Test
    public void testConstructor2() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.1, 0.5);
        assertNotNull(brentOptimizer);
        assertEquals(0.1, brentOptimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.5, brentOptimizer.getAbsoluteThreshold(), 0.0001);
    }

    // test case for doOptimize method
    @Test
    public void testDoOptimize() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.1, 0.5, null);
        
        brentOptimizer.setMin(-1);
        brentOptimizer.setStartValue(0);
        brentOptimizer.setMax(1);
        double result = brentOptimizer.doOptimize();
        assertEquals(0, result, 0.0001);
    }

    // test case for best method
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 100);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 200);

        BrentOptimizer brentOptimizer = new BrentOptimizer(0.1, 0.5, null);
        UnivariatePointValuePair result = brentOptimizer.best(a, b, true);
        
        assertEquals(b, result);
    }

}
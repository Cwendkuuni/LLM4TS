import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        double rel = 0.5;
        double abs = 1.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        assertEquals(rel, optimizer.relativeThreshold, 0.0);
        assertEquals(abs, optimizer.absoluteThreshold, 0.0);
    }

    @Test
    public void testConstructorWithNoChecker() {
        double rel = 0.5;
        double abs = 1.0;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        assertEquals(rel, optimizer.relativeThreshold, 0.0);
        assertEquals(abs, optimizer.absoluteThreshold, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        double rel = -0.5;
        double abs = 1.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        double rel = 0.5;
        double abs = -1.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test
    public void testDoOptimize() {
        // TODO: Implement test cases for doOptimize() method
    }

    @Test
    public void testBest() {
        // TODO: Implement test cases for best() method
    }

}
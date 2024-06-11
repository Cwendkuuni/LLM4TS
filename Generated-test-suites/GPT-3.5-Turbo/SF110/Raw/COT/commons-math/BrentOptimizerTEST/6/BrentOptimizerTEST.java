import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        double rel = 0.001;
        double abs = 0.0001;
        ConvergenceChecker<UnivariatePointValuePair> checker = new DummyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0001);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithoutChecker() {
        double rel = 0.001;
        double abs = 0.0001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0001);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        double rel = -0.001;
        double abs = 0.0001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        double rel = 0.001;
        double abs = -0.0001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testDoOptimize() {
        // TODO: Add test cases to test the doOptimize method
    }

    @Test
    public void testBest() {
        boolean isMinim = true;
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(b, result);
    }

    @Test
    public void testBestWithEqualValues() {
        boolean isMinim = true;
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 10.0);

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBestWithDifferentGoalType() {
        boolean isMinim = false;
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    private static class DummyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return false;
        }
    }
}
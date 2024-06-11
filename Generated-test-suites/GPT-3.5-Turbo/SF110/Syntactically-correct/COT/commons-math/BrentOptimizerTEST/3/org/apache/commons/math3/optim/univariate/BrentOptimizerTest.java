
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        // Arrange
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker();

        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        // Assert
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Arrange
        double rel = 1e-6;
        double abs = 1e-8;

        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Assert
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        // Arrange
        double rel = -1e-6;
        double abs = 1e-8;

        // Act
        new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        // Arrange
        double rel = 1e-6;
        double abs = 0.0;

        // Act
        new BrentOptimizer(rel, abs);
    }

    @Test
    public void testDoOptimize() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, new MyConvergenceChecker());

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        // Add assertions for the return value of doOptimize()
    }

    @Test
    public void testBest() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, new MyConvergenceChecker());
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 20.0);

        // Act
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        // Assert
        assertEquals(a, result);
    }

    // Add more test cases for other public methods
}

class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

    @Override
    public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
        // Implement custom convergence criteria based on previous and current values

        return false;
    }
}

import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testConstructorAndGetters() {
        double rel = 1e-5;
        double abs = 1e-8;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);

        // Check that the relative and absolute thresholds are correct
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);

        // Create a custom convergence checker to pass into the constructor
        ConvergenceChecker<PointValuePair> checker = (iteration, previous, current) -> {
            return false; // Always return false to never stop the optimization prematurely
        };
        PowellOptimizer optimizerWithChecker = new PowellOptimizer(rel, abs, checker);

        // Check that the convergence checker is set correctly
        assertEquals(checker, optimizerWithChecker.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelToleranceBelowMinimum() {
        // The relative tolerance is below the minimum allowed value (2 * Math.ulp(1d))
        double rel = 1e-17;
        double abs = 1e-8;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsToleranceEqualToZero() {
        // The absolute tolerance is equal to zero
        double rel = 1e-5;
        double abs = 0.0;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
    }

    // More test cases can be added for other methods and functionality of PowellOptimizer class

}